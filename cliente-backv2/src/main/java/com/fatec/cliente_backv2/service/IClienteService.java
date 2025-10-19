package com.fatec.cliente_backv2.service;

import java.util.List;
import java.util.Optional;
import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;

public interface IClienteService {
	//Req10 Consultar Cliente
	public List<Cliente> consultaTodos();
	//Req09 Cadastrar Cliente
	public Cliente cadastrar(ClienteDTO cliente);
	//Req10 Consultar Cliente pelo CPF
	public Optional<Cliente> consultarPorCpf(String cpf);
	//Req11 Consulta agrupada pelo CEP (nao implementado)
	//Req12 Atualizar informações do cliente 
	public Optional<Cliente> atualizar(String cpf, Cliente cliente);
	//Req13 Exclusão pelo CPF
	public boolean excluir(String cpf);
	
}
