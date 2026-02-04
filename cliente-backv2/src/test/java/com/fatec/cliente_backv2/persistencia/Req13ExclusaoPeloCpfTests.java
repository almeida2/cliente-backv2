package com.fatec.cliente_backv2.persistencia;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.service.IClienteRepository;

@DataJpaTest
class Req13ExclusaoPeloCpfTests {
	private Cliente cliente;
	@Autowired
	private IClienteRepository clienteRepository;

	public void setup() {
		cliente = new Cliente();
		cliente.setCpf("80983098000");
		cliente.setNome("Jose da Silva");
		cliente.setCep("01310-100");
		cliente.setEndereco("Av. Paulista");
		cliente.setBairro("Bela Vista");
		cliente.setCidade("Sao Paulo");
		cliente.setComplemento("123");
		cliente.setEmail("jose@gmail.com");
		cliente.setDataCadastro();
		clienteRepository.save(cliente);
	}

	/*
	 * Objetivo - verificar o comportamento da app na exclusao do cliente pelo cpf
	 * Pré-requisitos - o cliente a ser excluido esta cadastrado
	 */
	@Test
	void ct01_quando_exclui_cliente_pelo_cpf_retorna_excluido() {
		// Dado - que o cliente esta cadastrado
		setup();
		// Quando - o cliente e excluido pelo cpf
		clienteRepository.deleteByCpf("80983098000");
		Optional<Cliente> c2 = clienteRepository.findByCpf("80983098000");
		// Entao - retorna cliente nao encontrado
		assertFalse(c2.isPresent());

	}

}
