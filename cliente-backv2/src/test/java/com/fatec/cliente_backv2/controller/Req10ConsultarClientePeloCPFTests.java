package com.fatec.cliente_backv2.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteCpfDto;
import com.fatec.cliente_backv2.service.IClienteRepository;
import com.google.gson.Gson;
//Indica que é um teste de integração Spring Boot e inicia o servidor em uma porta aleatória
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Req10ConsultarClientePeloCPFTests {
	Cliente cliente;
	@Autowired
	private IClienteRepository clienteRepository;
	// A URL base do endpoint deve ser adaptado para ser local)
    private final String BASE_URL = "/api/v1/clientes/cpf";
 // Injeta a TestRestTemplate para fazer chamadas HTTP
    @Autowired
    private TestRestTemplate restTemplate;
    
    // O CPF de teste
    private final String CPF_TESTE = "44015623053";
	@BeforeEach
	void preRequisitoDeTeste() {
		// Dado - que o vendedor forneceu as informações do novo cliente cpf, nome, cep, complemento e e-mail validos
		cliente = new Cliente();
		cliente.setCpf(CPF_TESTE);
		cliente.setNome("Jose da Silva");
		cliente.setCep("01310-100"); //não é validadado
		cliente.setEndereco("Av. Paulista"); //stub
		cliente.setBairro("Bela Vista");
		cliente.setCidade("São Paulo");
		cliente.setComplemento("123");
		cliente.setEmail("jose@gmail.com");
		cliente.setDataCadastro();
		clienteRepository.save(cliente);
		
	}
	/**
     * Testa o endpoint POST /v1/clientes/cpf para um cliente cadastrado.
     */
    @Test
    void ct01_deveRetornarClienteCadastradoQuandoCpfValidoForEnviado() {
        // 1. Prepara o JSON de requisição
        ClienteCpfDto cpfDto = new ClienteCpfDto();
        cpfDto.setCpf(CPF_TESTE);
        String cpf = new Gson().toJson(cpfDto);
        // 2. Define os cabeçalhos (Headers) da requisição
        HttpHeaders headers = new HttpHeaders();
        // Deve-se definir o Content-Type como application/json
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Cria a entidade HTTP com o corpo (JSON) e os cabeçalhos
        HttpEntity<String> request = new HttpEntity<>(cpf, headers);

        // 4. Executa a chamada POST e recebe a resposta como String 
        ResponseEntity<String> response = restTemplate.postForEntity(
        		BASE_URL, //como a porta eh randomica a url base é relativa se passar absoluta da erro.
                request, 
                String.class
        );

        // 5. Verifica as asserções (Validações)
        
        // Verifica o código de status HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status HTTP deve ser OK (200)");
        
        // Verifica se o corpo da resposta não é nulo
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");

        // Verifica o Content-Type da resposta
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType(), "O Content-Type da resposta deve ser JSON");

        // Asserções no corpo da resposta (Verifica se o CPF esperado está presente no JSON retornado)
        // Verificar se a string do CPF está contida no corpo da resposta
        assertThat(response.getBody()).contains("\"status\":\"success\"");
        assertThat(response.getBody()).contains("\"cpf\":\"" + CPF_TESTE + "\"");
        assertThat(response.getBody()).contains("\"nome\":\"Jose da Silva\"");
       
    }
}


