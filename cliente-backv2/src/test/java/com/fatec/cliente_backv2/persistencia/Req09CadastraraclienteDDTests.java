package com.fatec.cliente_backv2.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.service.IClienteRepository;

@DataJpaTest
class Req09CadastraraclienteDDTests {
	// entidade
	private Cliente cliente;
	@Autowired
	private IClienteRepository clienteRepository;

	@ParameterizedTest
	@CsvFileSource(resources = "/dataset1.csv", numLinesToSkip = 1)
	public void ct_verifica_comportamento_cadastro(String cpf, String nome, String cep, String endereco, String bairro, String cidade, String complemento,
			String email, String re) {
		try {
			cliente = new Cliente();
			cliente.setCpf(cpf);
			cliente.setNome(nome);
			cliente.setCep(cep); 
			cliente.setEndereco(endereco); 
			cliente.setComplemento(complemento);
			cliente.setBairro(bairro);
			cliente.setCidade(cidade);
			cliente.setDataCadastro();
			cliente.setEmail(email);
			Cliente novoCliente = clienteRepository.save(cliente); //CRUD na camada de repository
			assertNotNull(novoCliente);
			assertEquals(re, "satisfatorio");
		} catch (Exception e) {
			assertEquals(re, e.getMessage());
		}
	}

}
