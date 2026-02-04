package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.IClienteService;

@SpringBootTest
class Req12AtualizarInformacoesDeClienteTests {

	@Autowired
	IClienteService servico;
	ClienteDTO cliente;
	Cliente c = null;
	private static final String CPF_CLIENTE = "44015623053";

	@BeforeEach
	public void preRequisitoDeTeste() {
		cliente = new ClienteDTO(CPF_CLIENTE, "Jose da Silva", "01310-100", "Av. Paulista", "Bela Vista", "Sao Paulo",
				"123", "jose@gmail.com");
		servico.cadastrar(cliente);
	}

	/*
	 * Objetivo - verificar o comportamento da app na atualizacao das informacoes do
	 * cliente
	 * Pré-requisitos - o cliente a ser atualizado esta cadastrado
	 */
	@Test
	void ct01_quando_atualiza_informacoes_cliente_retorna_atualizado() {
		// Dado - que o cliente esta cadastrado (ver @BeforeEach)

		// Quando - informacoes do cliente sao atualizadas
		ClienteDTO d = new ClienteDTO(CPF_CLIENTE, "Jose da Silva Junior", "01301-000", "Rua da Consolação",
				"Consolação",
				"Sao Paulo", "123", "jose.junior@gmail.com");
		Optional<Cliente> result = servico.atualizar(d);

		// Entao - retorna informacao atualizada
		assertTrue(result.isPresent());
		assertEquals("Rua da Consolação", result.get().getEndereco());
		assertEquals("Jose da Silva Junior", result.get().getNome());
		assertEquals("jose.junior@gmail.com", result.get().getEmail());
	}
}
