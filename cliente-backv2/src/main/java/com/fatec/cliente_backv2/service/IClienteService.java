package com.fatec.cliente_backv2.service;

import java.util.List;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;

public interface IClienteService {
	public List<Cliente> consultaTodos();
	public ClienteResponse cadastrar(ClienteDTO cliente);
	public ClienteResponse consultarPorCpf(String cpf);
	public ClienteResponse atualizar(String cpf, Cliente cliente);
	public ClienteResponse excluir(String cpf);
	public Double estoqueImobilizado();
}
