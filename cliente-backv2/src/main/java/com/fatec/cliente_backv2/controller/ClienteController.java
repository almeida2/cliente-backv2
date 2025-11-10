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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteCpfDto;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.model.Endereco;
import com.fatec.cliente_backv2.service.EnderecoServiceMock;
import com.fatec.cliente_backv2.service.IClienteService;
import com.fatec.cliente_backv2.service.IEnderecoService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
	Logger logger = LogManager.getLogger(this.getClass());

	IClienteService clienteService;
	IEnderecoService enderecoService;

	// injecao da dependencia pelo metodo construtor
	public ClienteController(IClienteService clienteService, IEnderecoService enderecoService) {
		this.clienteService = clienteService;
		//this.enderecoService = enderecoService;
		this.enderecoService = new EnderecoServiceMock(); //stub para o consulta cep excluir do import
	}

	/*
	 * As informacoes do cliente sao recebidas em um arquivo DTO entidades podem
	 * conter informacoes sensiveis que não devem ser expostas diretamente para o
	 * app cliente
	 * As informacoes de endereco sao fornecidas automaticamente na interface
	 * alteracoes do usuario na interface serão cadastradas.
	 */
	
	@PostMapping
	
	public ResponseEntity<ResponseApi<Cliente>> saveCliente(@RequestBody ClienteDTO clienteDTO) {
		logger.info(">>>>>> apicontroller cadastro de cliente iniciado...");

		try {
			Cliente novoCliente = clienteService.cadastrar(clienteDTO);
			ResponseApi<Cliente> response = new ResponseApi<>(novoCliente, "Cliente cadastrado com sucesso.");
			logger.info(">>>>>> apicontroller cliente cadastrado");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (IllegalArgumentException e) {
			// Captura exceção de CEP inválido
			ResponseApi<Cliente> response = new ResponseApi<>(null, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		} catch (Exception e) {
			// Captura qualquer outro erro inesperado
			ResponseApi<Cliente> response = new ResponseApi<>("Erro inesperado ao cadastrar cliente.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseApi<List<Cliente>>> getAll() {
		logger.info(">>>>>> api cliente controller consulta todos iniciado...");
		List<Cliente> clientes = clienteService.consultaTodos();
		ResponseApi<List<Cliente>> response = new ResponseApi<>(clientes, "Lista de clientes cadastrados");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * Consulta por cpf O cpf eh enviado no arquivo json clientedto com os outros
	 * atributos em branco
	 */
	@PostMapping("/cpf")
	public ResponseEntity<ResponseApi<Cliente>> getCliente(@RequestBody ClienteCpfDto cliente) {
		try {
			Optional<Cliente> c = clienteService.consultarPorCpf(cliente.getCpf());
			logger.info(">>>>>> apicontroller getCliente consulta servico iniciado");
			if (c.isPresent()) {
				ResponseApi<Cliente> response = new ResponseApi<>(c.get(), "");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				ResponseApi<Cliente> response = new ResponseApi<>("CPF não encontrado.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (Exception e) {
			logger.info(">>>>>>apicontroller getCliente erro nao esperado => " + e.getMessage());
			ResponseApi<Cliente> response = new ResponseApi<>(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	/**
	 * exclusao do cliente pelo cpf
	 * 
	 * @param cpf O CPF do cliente a ser excluído, extraído da URL.
	 */
	@DeleteMapping("/{cpf}") // path variable-o parametro e envidado no endpoint
	public ResponseEntity<ResponseApi<Cliente>> excluirCliente(@PathVariable String cpf) {
		logger.info(">>>>>>apicontroller excluir cliente iniciado " + cpf);
		// 1. Chama o servico para executar a exclusao
		boolean excluido = clienteService.excluir(cpf);

		// 2. Envelopamento da resposta HTTP
		if (excluido) {
			// Se a exclusão foi bem-sucedida, retorna o status HTTP 204 (No Content)
			// 204 é o status padrão para deleções bem-sucedidas que não retornam um corpo
			// de resposta.
			logger.info(">>>>>>apicontroller cliente excluido ");
			return ResponseEntity.noContent().build();
		} else {
			// Se o recurso não foi encontrado para exclusão, retorna 404 (Not Found)
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{cep}")
	public ResponseEntity<ResponseApi<Endereco>> consultarCep(@PathVariable String cep) {
		Optional<Endereco> e = enderecoService.obtemLogradouroPorCep(cep);
		if (e.isPresent()) {
			ResponseApi<Endereco> response = new ResponseApi<>(e.get(), "Endereco obtido com sucesso.");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			ResponseApi<Endereco> response = new ResponseApi<>("CEP não encontrado.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	@PutMapping("/up")
	public ResponseEntity<ResponseApi<Cliente>> atualizar(@RequestBody ClienteDTO clienteDTO) {
		try {
			Cliente clienteAtualizado = clienteService.atualizar(clienteDTO).get();
			ResponseApi<Cliente> response = new ResponseApi<>(clienteAtualizado, "Informações de clieten atualizada com sucesso.");
			logger.info(">>>>>> apicontroller cliente cadastrado");
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (IllegalArgumentException e) {
			// Captura exceção de CEP inválido
			ResponseApi<Cliente> response = new ResponseApi<>(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		} catch (Exception e) {
			// Captura qualquer outro erro inesperado
			ResponseApi<Cliente> response = new ResponseApi<>("Erro inesperado ao cadastrar cliente.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}