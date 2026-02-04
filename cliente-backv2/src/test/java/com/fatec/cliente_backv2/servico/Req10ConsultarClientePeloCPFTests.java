package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.service.IClienteRepository;
import com.fatec.cliente_backv2.service.IClienteService;

@SpringBootTest
class Req10ConsultarClientePeloCPFTests {
	@Autowired
	IClienteService servico;
	@Autowired
	IClienteRepository repository;
	Cliente cliente; // cliente cadastrado
	private static final String CPF = "83021772021";

	@BeforeEach // salva um cliente antes de consultar
	public void preRequisitoDeTeste() {
		cliente = new Cliente();
		cliente.setCpf(CPF);
		cliente.setNome("Jose Francisco");
		cliente.setCep("01310100");
		cliente.setEndereco("Av. Paulista");
		cliente.setComplemento("123");
		cliente.setBairro("BelaVista");
		cliente.setCidade("São Paulo");
		cliente.setEmail("jose@gmail.com");
		repository.save(cliente);
	}

	@Test
	void ct01_consultar_cliente_com_sucesso() {
		Optional<Cliente> clienteRetornado = servico.consultarPorCpf(CPF);
		assertTrue(clienteRetornado.isPresent());
		assertEquals(CPF, clienteRetornado.get().getCpf());
	}

}
