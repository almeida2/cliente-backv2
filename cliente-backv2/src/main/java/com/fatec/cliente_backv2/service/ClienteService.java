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
	public ClienteResponse cadastrar(ClienteDTO cliente) {
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
				return new ClienteResponse(false, "Cliente já cadastrado.", null);
			}
			//chama a classe servico de endereco para obter o cep
			Optional<String> endereco = enderecoService.obtemLogradouroPorCep(novoCliente.getCep());
			if (endereco.isEmpty()) {
				logger.info(">>>>>> Endereço não encontrado para o CEP");
				return new ClienteResponse(false, "Endereço não encontrado.", null);
			} else {
				novoCliente.setDataCadastro();
				novoCliente.setEndereco(endereco.get());
				Cliente c = clienteRepository.save(novoCliente);
				logger.info(">>>>>> clienteservico - cliente salvo com sucesso no repositório");
				
				return new ClienteResponse(true, "Cliente cadastrado com sucesso.", c);
			}
		} catch (IllegalArgumentException e) {
			return new ClienteResponse(false, e.getMessage(), null);
		} catch (Exception e) {
			logger.info(">>>>>> clienteservico - erro nao esperado metodo cadastrar => " + e.getMessage());
			return new ClienteResponse(false, "Erro não esperado ao cadastrar cliente.", null);
		}
	}


	@Override
	public ClienteResponse consultarPorCpf(String cpf) {
		Optional<Cliente> c = clienteRepository.findByCpf(cpf);
		if (c.isPresent()) {
			return new ClienteResponse(true, null, c.get());
		} else {
			logger.info(">>>>>> clienteservico cliente nao encontrado => " + cpf);
			return new ClienteResponse(false, "Cliente não cadastrado", null);
		}
	}

	@Override
	public ClienteResponse atualizar(String cpf, Cliente cliente) {
		return new ClienteResponse(false, "Nao implementado", null);
	}

	@Override
	public ClienteResponse excluir(String cpf) {
		Optional<Cliente> c = clienteRepository.findByCpf(cpf);
		if (c.isEmpty()) {
			return new ClienteResponse(false, "Cliente não cadastrado", null);
		} else {
			clienteRepository.deleteByCpf(cpf);
			return new ClienteResponse(true, "Cliente excluido", null);
		}
	}

	@Override
	public Double estoqueImobilizado() {
		// TODO Auto-generated method stub
		return null;
	}

}
