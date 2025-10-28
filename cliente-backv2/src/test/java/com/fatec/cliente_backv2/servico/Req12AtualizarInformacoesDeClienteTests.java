package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.*;

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
		cliente = new ClienteDTO(CPF_CLIENTE, "Jose da Silva", "01310-100", "Av. Paulista", "Bela Vista", "Sao Paulo","123", "jose@gmail.com");
		servico.cadastrar(cliente);
	}
	@Test
	void ct01_quando_atualiza_informacoes_cliente_retorna_cliente_atulizado() {
		// Dado - que o cliente esta cadastrado
		//Quando o vendedor atualiza as informacoes do cep
		cliente = new ClienteDTO(CPF_CLIENTE, "Jose da Silva", "01416001", "Rua da Consolação", "Bela Vista", "Sao Paulo","123", "jose@gmail.com");
		Optional<Cliente> clienteAtualizado = servico.atualizar(cliente);
		// Entao - retorna os detalhes do cliente atualizado
		assertTrue(clienteAtualizado.isPresent());
		assertEquals ("Rua da Consolação", clienteAtualizado.get().getEndereco() );
	}

	
}
