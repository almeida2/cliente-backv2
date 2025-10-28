package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.IClienteService;
@SpringBootTest
class Req09CadastrarClienteTests {
	@Autowired
	IClienteService servico;
	ClienteDTO cliente;
	Cliente c = null;
	private static final String CPF_CLIENTE = "44015623053";
	@BeforeEach
	public void preRequisitoDeTeste() {
		cliente = new ClienteDTO(CPF_CLIENTE, "Jose da Silva", "01310-100", "Av. Paulista", "Bela Vista", "Sao Paulo","123", "jose@gmail.com");
	}
	
	@Test
	void ct01_quando_dados_validos_cliente_cadastrado_com_sucesso() {
		// Dado - que o vendedor forneceu as informações do novo cliente cpf, nome, cep, complemento e e-mail validos
		//Quando o vendedor confirma o cadastro
		c = servico.cadastrar(cliente);
		// Entao - o novo cliente é cadastrado com sucesso no sistema E as informações ficam disponiveis para consulta
		assertNotNull(c);
		assertEquals ("Avenida Paulista", c.getEndereco() );
	}
}
