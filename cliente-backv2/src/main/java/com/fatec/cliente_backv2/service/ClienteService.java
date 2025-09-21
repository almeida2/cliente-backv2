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
	final ClienteRepository clienteRepository;
	
	private IEnderecoService enderecoService;

	// Injeção de dependências pelo construtor
	public ClienteService(ClienteRepository clienteRepository, 
			IEnderecoService enderecoService) {
		this.clienteRepository = clienteRepository;
		
		this.enderecoService = enderecoService;
	}
	@Override
	public List<Cliente> consultaTodos() {
		logger.info(">>>>>> clienteservico - consulta todos iniciado");
		return clienteRepository.findAll();
	}

	@Transactional
	public Optional<Cliente> cadastrar(ClienteDTO cliente) {
		try {
			Cliente novoCliente = new Cliente();
			novoCliente.setCpf(cliente.cpf());
			novoCliente.setNome(cliente.nome());
			novoCliente.setCep(cliente.cep());
			novoCliente.setComplemento(cliente.complemento());
			novoCliente.setEmail(cliente.email());
			// Verifica se o cliente já existe com base no CPF
			if (clienteRepository.findByCpf(novoCliente.getCpf()).isPresent()) {
				logger.info(">>>>>> clienteservico - cliente já cadastrado");
				throw new IllegalArgumentException("Cliente já cadastrado");
			}
			//chama a classe servico de endereco para obter o cep
			Optional<String> endereco = enderecoService.obtemLogradouroPorCep(novoCliente.getCep());
			if (endereco.isEmpty()) {
				logger.info(">>>>>> Endereço não encontrado para o CEP");
				throw new IllegalArgumentException("Endereço não encontrado.");
			} else {
				novoCliente.setDataCadastro();
				novoCliente.setEndereco(endereco.get());
				logger.info(">>>>>> clienteservico - cliente salvo com sucesso no repositório");
				return Optional.of(clienteRepository.save(novoCliente));
			}
		
		} catch (Exception e) {
			logger.info(">>>>>> clienteservico - erro nao esperado metodo cadastrar => " + e.getMessage());
		    return Optional.empty();
		}
	}


	@Override
	public Optional<Cliente> consultarPorCpf(String cpf) {
		return clienteRepository.findByCpf(cpf);
	}

	@Override
	public Optional<Cliente> atualizar(String cpf, Cliente cliente) {
		return Optional.empty();
	}

	@Override
	public void excluir(String cpf) {
		Optional<Cliente> c = clienteRepository.findByCpf(cpf);
		if (c.isEmpty()) {
			throw new IllegalArgumentException("Cliente não encontrado.");
		} else {
			clienteRepository.deleteByCpf(cpf);
		
		}
	}

	@Override
	public Double estoqueImobilizado() {
		// TODO Auto-generated method stub
		return null;
	}

}
