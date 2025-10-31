package com.fatec.cliente_backv2.service;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fatec.cliente_backv2.model.Endereco;

@Service
public class EnderecoServiceMock implements IEnderecoService {

	// A API_MOCK deve retornar o formato de array: [{"cep": "..."}]
	private static final String API_MOCK = "http://localhost:3000/tempcep?cep={cep}";
    private static final Logger logger = LogManager.getLogger(EnderecoServiceMock.class);

       
    /**
     * consulta - comunicação síncrona com a api mock.
     * Espera um formato de resposta JSON que é um array: [{"cep": "...", ...}]
     */
    public Optional<Endereco> obtemLogradouroPorCep(String cep) {
    	RestTemplate restTemplate = new RestTemplate();
        logger.info(">>>>>> obtemLogradouroPorCep stub chamado para o CEP: " + cep);
        try {
            // 1. Mudar o tipo de retorno esperado para um array/List de Endereco
            ResponseEntity<List<Endereco>> response = restTemplate.exchange(
                API_MOCK,
            	HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Endereco>>() {}, // Define o tipo de retorno como List<Endereco>
                cep
            );

            HttpStatusCode statusCode = response.getStatusCode();
            logger.info(">>>>>> Código de status HTTP retornado: " + statusCode.value());

            List<Endereco> listaEnderecos = response.getBody();

            // 2. Verificar se a lista não está vazia e obter o primeiro elemento
            if (listaEnderecos != null && !listaEnderecos.isEmpty()) {
                Endereco endereco = listaEnderecos.get(0);

                if (endereco != null && endereco.getLogradouro() != null) {
                    logger.info(">>>>>> Logradouro encontrado");
                    return Optional.of(endereco);
                }
            }
            
            // Caso a lista esteja vazia ou o primeiro elemento não tenha logradouro
            logger.warn(">>>>>> Logradouro não encontrado para o CEP ou lista vazia.");
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            logger.warn(">>>>>> Erro retornado pela API ao buscar CEP: " + e.getMessage());
            return Optional.empty();
        }
        catch(ResourceAccessException e) {
        	logger.warn(">>>>>> Erro não esperado retornado pela API ao buscar CEP: " + e.getMessage());
        	return Optional.empty();
        }
    }
}