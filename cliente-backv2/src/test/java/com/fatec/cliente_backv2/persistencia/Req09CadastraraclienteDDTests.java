package com.fatec.cliente_backv2.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.fatec.cliente_backv2.model.Cliente;

class Req09CadastraraclienteDDTests {

	Cliente cliente;

	@ParameterizedTest
	@CsvFileSource(resources = "/dataset1.csv", numLinesToSkip = 1)
	public void ct_verifica_comportamento_cadastro(String cpf, String nome, String cep, String complemento,
			String email, String re) {
		try {
			cliente = new Cliente();
			cliente.setCpf(cpf);
			cliente.setNome(nome);
			cliente.setCep(cep);
			cliente.setEndereco("Rua Augusta");
			cliente.setComplemento(complemento);
			cliente.setDataCadastro();
			cliente.setEmail(email);
			assertNotNull(cliente);
			assertEquals(re, "satisfatorio");
		} catch (Exception e) {
			assertEquals(re, e.getMessage());
		}
	}


}
