package com.fatec.cliente_backv2.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.IClienteService;

@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
	Logger logger = LogManager.getLogger(this.getClass());

	IClienteService clienteService;

	//injecao da dependencia pelo metodo construtor
	public ClienteController(IClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/*
	 * As informacoes do cliente sao recebidas em um arquivo DTO entidades podem conter informacoes
	 * sensiveis que não devem ser expostas diretamente para o app cliente
	 */
	@PostMapping
    public ResponseEntity<ApiResponse<Cliente>> saveCliente(@RequestBody ClienteDTO clienteDTO) {
        logger.info(">>>>>> apicontroller cadastro de cliente iniciado...");

        try {
            Cliente novoCliente = clienteService.cadastrar(clienteDTO);
            ApiResponse<Cliente> response = new ApiResponse<>(novoCliente, "Cliente cadastrado com sucesso.");
            logger.info(">>>>>> apicontroller cliente cadastrado");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            // Captura exceção de CEP inválido
            ApiResponse<Cliente> response = new ApiResponse<>(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        }  catch (Exception e) {
            // Captura qualquer outro erro inesperado
            ApiResponse<Cliente> response = new ApiResponse<>("Erro inesperado ao cadastrar cliente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<Cliente>>> getAll() {
		logger.info(">>>>>> api cliente controller consulta todos iniciado...");
		List<Cliente> clientes = clienteService.consultaTodos();
		ApiResponse<List<Cliente>> response = new ApiResponse<>(clientes, "Lista de clientes cadastrados");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * O cpf eh enviado no arquivo json clientedto com os outros atributos em branco
	 */
	@PostMapping("/cpf")
	public ResponseEntity<ApiResponse<Cliente>> getCliente(@RequestBody ClienteDTO cliente) {
		try {
			Optional<Cliente> c = clienteService.consultarPorCpf(cliente.cpf());
			logger.info(">>>>>> apicontroller getCliente consulta servico iniciado");
			if (c.isPresent()) {
				ApiResponse<Cliente> response = new ApiResponse<>(c.get(), "");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				ApiResponse<Cliente> response = new ApiResponse<>("CPF não encontrado.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (Exception e) {
			logger.info(">>>>>>apicontroller getCliente erro nao esperado => " + e.getMessage());
			ApiResponse<Cliente> response = new ApiResponse<>(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
    /**
     * exclusao do cliente pelo cpf
     * @param cpf O CPF do cliente a ser excluído, extraído da URL.
     */
    @DeleteMapping("/{cpf}") // path variable-o parametro e envidado no endpoint
    public ResponseEntity<ApiResponse<Cliente>> excluirCliente(@PathVariable String cpf) {
    	logger.info(">>>>>>apicontroller excluir cliente iniciado " + cpf );
        // 1. Chama o servico para executar a exclusao
        boolean excluido = clienteService.excluir(cpf);

        // 2. Envelopamento da resposta HTTP
        if (excluido) {
            // Se a exclusão foi bem-sucedida, retorna o status HTTP 204 (No Content)
            // 204 é o status padrão para deleções bem-sucedidas que não retornam um corpo de resposta.
        	logger.info(">>>>>>apicontroller cliente excluido " );
             return ResponseEntity.noContent().build();            
        } else {
            // Se o recurso não foi encontrado para exclusão, retorna 404 (Not Found)
        	return ResponseEntity.notFound().build(); 
        }
    }
	
}