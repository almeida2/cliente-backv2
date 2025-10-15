package com.fatec.cliente_backv2.servico;

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
class Req13ExclusaoPeloCpfTest {
	@Autowired
	IClienteService servico;
	ClienteDTO cliente;
	private static final String CPF_CLIENTE = "80983098000";
	@BeforeEach
	public void preRequisitoDeTeste() {
		cliente = new ClienteDTO(CPF_CLIENTE, "Jose da Silva", "01310-100", "123", "jose@gmail.com");
		servico.cadastrar(cliente);
	}

	@Test
	void ct01_exclusao_com_cpf_cadastrado_deve_ficar_indisponivel_para_consulta() {
		// Dado - que o vendedor forneceu as informações do novo cliente cpf, nome, cep, complemento e e-mail validos
		// Quando o vendedor confirma a exclusao
		servico.excluir(CPF_CLIENTE);
		// Entao - o cliente fica indisponivel para consulta
		Optional<Cliente> c = servico.consultarPorCpf("80983098000");
		assertTrue(c.isEmpty());
	}

}
