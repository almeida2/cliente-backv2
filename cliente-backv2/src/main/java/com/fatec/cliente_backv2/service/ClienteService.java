package com.fatec.cliente_backv2.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;

import jakarta.transaction.Transactional;

@Service
public class ClienteService implements IClienteService {
	Logger logger = LogManager.getLogger(this.getClass());
	final IClienteRepository repository;

	private IEnderecoService enderecoService;

	// Injeção de dependências pelo construtor
	public ClienteService(IClienteRepository clienteRepository, IEnderecoService enderecoService) {
		this.repository = clienteRepository;
		this.enderecoService = enderecoService;
	}

	@Override
	public List<Cliente> consultaTodos() {
		logger.info(">>>>>> clienteservico - consulta todos iniciado");
		return repository.findAll();
	}

	@Transactional
	public Cliente cadastrar(ClienteDTO clienteDTO) {
		// 1. Verifica se o cliente já existe com base no CPF
		if (repository.findByCpf(clienteDTO.cpf()).isPresent()) {
			logger.info(">>>>>> clienteservico - cliente já cadastrado: " + clienteDTO.cpf());
			// Lança uma exceção personalizada para CPF duplicado
			throw new IllegalArgumentException("Cliente com este CPF já cadastrado.");
		}

		// 2. Busca o endereço pelo CEP. Se não encontrar, lança exceção.
		Optional<String> endereco = enderecoService.obtemLogradouroPorCep(clienteDTO.cep());
		if (endereco.isEmpty()) {
			logger.info(">>>>>> Endereço não encontrado para o CEP: " + clienteDTO.cep());
			throw new IllegalArgumentException("Endereço não encontrado para o CEP informado.");
		}

		// 3. Converte DTO para entidade e persiste
		Cliente novoCliente = new Cliente();
		novoCliente.setCpf(clienteDTO.cpf());
		novoCliente.setNome(clienteDTO.nome());
		novoCliente.setCep(clienteDTO.cep());
		novoCliente.setComplemento(clienteDTO.complemento());
		novoCliente.setEmail(clienteDTO.email());
		novoCliente.setDataCadastro();
		novoCliente.setEndereco(endereco.get());

		logger.info(">>>>>> clienteservico - cliente salvo com sucesso no repositório.");
		return repository.save(novoCliente);
	}

	@Override
	public Optional<Cliente> consultarPorCpf(String cpf) {
		logger.info(">>>>>> clienteservico - metodo consultaPorCpf iniciado => " + cpf);
		return repository.findByCpf(cpf);
	}

	@Override
	public Optional<Cliente> atualizar(String cpf, Cliente cliente) {
		return Optional.empty();
	}

	@Override
    @Transactional // Garante que a operação de deleção seja atômica
    public boolean excluir(String cpf) {
        
        // 1. Verificar se o cliente existe
        Optional<Cliente> clienteExistente = repository.findByCpf(cpf);
        
        if (clienteExistente.isPresent()) {
            // 2. Se o cliente existe, realiza a exclusão
            repository.deleteByCpf(cpf);
            return true; // Exclusão bem-sucedida
        } else {
            // 3. Se o cliente não existe
            return false; // Não foi possível excluir (não encontrado)
        }
    }
	
}
