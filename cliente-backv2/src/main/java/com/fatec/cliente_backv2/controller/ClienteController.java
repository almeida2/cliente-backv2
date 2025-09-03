package com.fatec.cliente_backv2.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.ClienteResponse;
import com.fatec.cliente_backv2.service.IClienteService;

@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
	Logger logger = LogManager.getLogger(this.getClass());

	IClienteService clienteService;

	@Autowired
	public ClienteController(IClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/*
	 * As informacoes do cliente sao recebidas em um arquivo DTO para o uso do
	 * objeto POJO na assinatura do metodo.
	 */
	@PostMapping
	public ResponseEntity<Object> saveCliente(@RequestBody ClienteDTO cliente) {
		logger.info(">>>>>> apicontroller cadastro de cliente iniciado...");
		
		ClienteResponse c = clienteService.cadastrar(cliente);
		if (!c.isSucesso()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(c.getMensagem());
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body(c.getCliente());
		}

	}

	@GetMapping("/all")
	public List<Cliente> getAll() {
		logger.info(">>>>>> apicontroller consulta todos iniciado...");
		return clienteService.consultaTodos();
	}

	/*
	 * O cpf eh enviado no arquivo json clientedto com os outros atributos em branco
	 * para nao trafegar com o cpf na url
	 */
	@PostMapping("/cpf")
	public ResponseEntity<Object> getCliente(@RequestBody ClienteDTO cliente) {
		ClienteResponse c = new ClienteResponse(false, "CPF Invalido", null);
		try {
			c = clienteService.consultarPorCpf(cliente.cpf()); // obtem o cpf
			logger.info(">>>>>> apicontroller getCliente consulta servico iniciado");
			if (c.isSucesso()) {
				return ResponseEntity.status(HttpStatus.OK).body(c.getCliente());
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(c.getMensagem());
			}
		} catch (Exception e) {
			logger.info(">>>>>>apicontroller getCliente erro nao esperado => " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro interno ao processar a requisição.");

		}
	}
}