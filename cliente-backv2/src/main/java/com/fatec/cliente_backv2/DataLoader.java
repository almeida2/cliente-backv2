package com.fatec.cliente_backv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.ClienteService;
import com.fatec.cliente_backv2.service.IClienteRepository;

/**
 * Componente que executa a lógica de carregamento do CSV 
 * assim que o Spring Boot é inicializado.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DataLoader.class);
    
    private final IClienteRepository repository;

    /**
     * Injeta o arquivo CSV do Classpath (src/main/resources) 
     * no objeto Resource do Spring. 
     * Setup de teste 
     * (1) No teste do backend utilizar o dataset1.csv
     * (2) No teste do frontend utilizar o dataset100.csv
     */
    @Value("classpath:dataset100.csv")
    private Resource dataFile;

    /**
     * Injeção de dependência do ClienteService via construtor.
     */
    public DataLoader(IClienteRepository repository) {
        this.repository = repository;
    }

    /**
     * Este método é executado na inicialização da aplicação Spring Boot.
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info(">>> Iniciando carregamento de dados do CSV para o DB...");
        
        if (dataFile == null || !dataFile.exists()) {
             logger.warn("Arquivo dataset2.csv não encontrado no classpath. Pulando inicialização de dados.");
             return;
        }

        // Abre um fluxo de leitura a partir do Resource
        try (BufferedReader br = new BufferedReader(
                 new InputStreamReader(dataFile.getInputStream(), StandardCharsets.UTF_8))) {
            
            String linha;
            
            // Pula o cabeçalho
            if ((linha = br.readLine()) != null) {
                logger.debug("Cabeçalho ignorado: {}", linha);
            }

            while ((linha = br.readLine()) != null) {
                // Lógica de parsing do CSV (adaptada do seu exemplo anterior)
                String[] dados = linha.split(",");

                if (dados.length >= 6) { 
                    
                	// Extrai e limpa os dados
					String cpf = dados[0].trim();
					String nome = dados[1].trim();
					String cep = dados[2].trim();
					String endereco = dados[3].trim();
					String bairro = dados[4].trim();
					String cidade = dados[5].trim();
					String complemento = dados[6].trim();
					String email = dados[7].trim();

					// Cria o DTO
					ClienteDTO clienteDTO = new ClienteDTO(cpf, nome, cep, endereco, bairro, cidade,
							complemento, email);
					// Converte DTO para entidade e persiste
					Cliente novoCliente = new Cliente();
					novoCliente.setCpf(clienteDTO.cpf());
					novoCliente.setNome(clienteDTO.nome());
					novoCliente.setCep(clienteDTO.cep());
					novoCliente.setEndereco(clienteDTO.endereco());
					novoCliente.setBairro(clienteDTO.bairro());
					novoCliente.setCidade(clienteDTO.cidade());
					novoCliente.setComplemento(clienteDTO.complemento());
					novoCliente.setEmail(clienteDTO.email());
					novoCliente.setDataCadastro();
					novoCliente.setEndereco(clienteDTO.endereco());
                    try {
                        // Chama o repository injetado
                        repository.save(novoCliente);
                        logger.info("✅ Cliente carregado e cadastrado: {}", cpf);
                    } catch (IllegalArgumentException e) {
                        // Captura exceções como CPF duplicado ou CEP inválido
                        logger.warn("⚠️ Não foi possível cadastrar cliente {}: {}", cpf, e.getMessage());
                    } catch (Exception e) {
                        logger.error("❌ Erro inesperado ao persistir cliente {}: {}", cpf, e.getMessage());
                    }
                } else {
                    logger.warn("Linha ignorada por formato inválido: {}", linha);
                }
            }
            logger.info(">>> Carregamento de dados iniciais concluído.");

        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo CSV de dados iniciais: {}", e.getMessage(), e);
        }
    }
}
