package com.fatec.cliente_backv2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.IClienteRepository;
import com.fatec.cliente_backv2.service.IClienteService;

/**
 * Classe para leitura de um arquivo CSV e o cadastro de clientes utilizando o
 * ClienteService.
 *
 */
public class ClienteDataLoad {

	private static final Logger logger = LogManager.getLogger(ClienteDataLoad.class);
	// private final IClienteService clienteService;
	private final IClienteRepository repository;

	/**
	 * Construtor que recebe o ClienteService, garantindo a Inversão de Controle
	 * (IoC) e Injeção de Dependência (DI).
	 * 
	 * @param clienteService A instância do serviço a ser testada.
	 */
//	public ClienteDataLoad(IClienteService clienteService) {
//		this.clienteService = clienteService;
//	}
	public ClienteDataLoad(IClienteRepository repository) {
		this.repository = repository;
	}

	/**
	 * Executa a leitura do arquivo CSV do Classpath (src/main/resources) e tenta
	 * cadastrar os clientes.
	 * 
	 * @param nomeArquivo O nome do arquivo CSV a ser lido (ex: "dataset2.csv").
	 */
	public void executarCadastroEmLote(String nomeArquivo) {
		logger.info(">>> Iniciando carregamento do recurso do classpath: {}", nomeArquivo);

		// 1. Usa try-with-resources para obter o arquivo
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(nomeArquivo)) {

			// Verifica se o recurso foi encontrado
			if (is == null) {
				logger.fatal(
						"ERRO FATAL: Arquivo '{}' não encontrado no Classpath. Verifique se está em src/main/resources/",
						nomeArquivo);
				return; // Sai do método se o arquivo não for encontrado
			}

			// 2. Cria o BufferedReader a partir do InputStream, especificando a codificação
			// UTF-8
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

				String linha;
				// Pula o cabeçalho
				if ((linha = br.readLine()) != null) {
					logger.info("Cabeçalho ignorado: {}", linha);
				}

				// Início do loop de leitura e processamento
				while ((linha = br.readLine()) != null) {
					// ... (O restante da sua lógica de processamento da linha CSV) ...

					String[] dados = linha.split(",");

					if (dados.length >= 6) {
						try {
							// Extrai e limpa os dados
							String cpf = dados[0].trim();
							String nome = dados[1].trim();
							String cep = dados[2].trim();
							String endereco = dados[3].trim();
							String bairro = dados[4].trim();
							String cidade = dados[5].trim();
							String complemento = dados[6].trim();
							String email = dados[7].trim();

							// 1. Cria o DTO
							ClienteDTO clienteDTO = new ClienteDTO(cpf, nome, cep, endereco, bairro, cidade,
									complemento, email);
							// 3. Converte DTO para entidade e persiste
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

							
							
							// 2. Chama o método de cadastro
							//Cliente clienteCadastrado = clienteService.cadastrar(clienteDTO);
							Cliente clienteCadastrado = repository.save(novoCliente);
							logger.info("✅ Cliente cadastrado com SUCESSO! CPF: {}", clienteCadastrado.getCpf());

						} catch (IllegalArgumentException e) {
							logger.error("⚠️ ERRO ao cadastrar cliente (linha: {}): {}", linha, e.getMessage());
						} catch (Exception e) {
							logger.error("❌ ERRO INESPERADO ao processar linha (linha: {}): {}", linha, e.getMessage());
						}
					} else {
						logger.warn("Linha ignorada por formato inválido (menos de 6 colunas): {}", linha);
					}

				}

				logger.info(">>> Leitura e processamento do arquivo CSV CONCLUÍDOS.");

			} // O BufferedReader é fechado automaticamente aqui

		} catch (IOException e) {
			logger.fatal("Falha durante a leitura do arquivo {}.", nomeArquivo, e);
		}

	}

}
