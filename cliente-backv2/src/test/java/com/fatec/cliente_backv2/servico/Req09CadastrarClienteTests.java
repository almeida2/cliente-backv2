package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.model.Endereco;
import com.fatec.cliente_backv2.service.IClienteService;
import com.fatec.cliente_backv2.service.IEnderecoService;
@SpringBootTest
class Req09CadastrarClienteTests {
	@Autowired
	IClienteService clienteServico;
	@Autowired
	IEnderecoService enderecoServico;
	ClienteDTO cliente;
	Cliente c = null;
	Endereco e = null;
	private static final String CPF = "44015623053";
	private static final String CEP = "01310-100";
	@BeforeEach //simula a entrada de usuario pela interface
	public void preRequisitoDeTeste() {
		e = enderecoServico.obtemLogradouroPorCep(CEP).get();
		cliente = new ClienteDTO(CPF, "Jose da Silva", CEP, e.getLogradouro(),e.getBairro(), e.getLocalidade(),"123", "jose@gmail.com");
	}
	@Test
	void ct01_quando_dados_validos_cliente_cadastrado_com_sucesso() {
		//Dado - que o vendedor forneceu as informações do novo cliente cpf, nome, cep, complemento e e-mail validos
		//Quando o vendedor confirma o cadastro
		c = clienteServico.cadastrar(cliente);
		// Entao - o novo cliente é cadastrado com sucesso no sistema E as informações ficam disponiveis para consulta
		assertNotNull(c);
		assertEquals ("Avenida Paulista", c.getEndereco() );
	}
}
