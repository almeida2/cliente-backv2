package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.ClienteService;
import com.fatec.cliente_backv2.service.IClienteRepository;
@SpringBootTest
class Req09CadastrarClienteTests {
	
	@Autowired
	IClienteRepository repository;
	@Autowired
	ClienteService service;
	ClienteDTO cliente;
	Cliente c = null;
	private static final String CPF = "48400451007";
	private static final String CEP = "01310000";
	@BeforeEach //simula a entrada de usuario pela interface
	public void preRequisitoDeTeste() {
		cliente = new ClienteDTO(CPF, "Jose da Silva", CEP, "Avenida Paulista","Bela Vista","São Paulo","123", "jose@gmail.com");
	}
	@Test
	void ct01_quando_dados_validos_cliente_cadastrado_com_sucesso() {
		//Dado - que o vendedor forneceu as informações do novo cliente cpf, nome, cep, complemento e e-mail validos
		//Quando o vendedor confirma o cadastro
		repository.deleteAll();
		c = service.cadastrar(cliente);
		// Entao - o novo cliente é cadastrado com sucesso no sistema E as informações ficam disponiveis para consulta
		assertNotNull(c);
		assertEquals ("Avenida Paulista", c.getEndereco() );
	}
	 @Test
	    void deveCadastrarEConsultarClientePorCpf() {
	        // Arrange
	        ClienteDTO clienteDTO = new ClienteDTO(
	                "12345678901", // CPF
	                "Fulano da Silva", // Nome
	                "01001000", // CEP (utiliza valor mock para passar pelo EnderecoServiceMock)
	                "Praça da Sé", // Endereço
	                "Sé", // Bairro
	                "São Paulo", // Cidade
	                "Apto 10", // Complemento
	                "fulano@email.com" // Email
	        );

	        // Act
	        Cliente clienteSalvo = service.cadastrar(clienteDTO);
	        Optional<Cliente> buscado = service.consultarPorCpf("12345678901");

	        // Assert
	        Assertions.assertTrue(buscado.isPresent());
	        Assertions.assertEquals(clienteSalvo.getId(), buscado.get().getId());
	        Assertions.assertEquals(clienteDTO.nome(), buscado.get().getNome());
	        Assertions.assertEquals(clienteDTO.cep(), buscado.get().getCep());
	        Assertions.assertEquals(clienteDTO.cpf(), buscado.get().getCpf());
	    }
}
