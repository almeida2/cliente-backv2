package com.fatec.cliente_backv2.service;

import java.util.List;
import java.util.Optional;
import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;

public interface IClienteService {
	
	 /**
     * Req10 Consultar Cliente - Retorna a lista de todos os clientes cadastrados no sistema.
     * @return uma lista com todos os clientes registrados
     */
	public List<Cliente> consultaTodos();
	
	/**
     * Req09 Cadastrar Cliente- Cadastra um novo cliente a partir dos dados informados.
     * @param cliente objeto ClienteDTO contendo os dados do cliente a ser cadastrado
     * @return o objeto Cliente cadastrado
     * @throws IllegalArgumentException se algum dado do cliente for inválido
     */
	public Cliente cadastrar(ClienteDTO cliente);
	 /**
     * Req10 Consultar Cliente pelo CPF - Consulta um cliente pelo CPF.
     * @param cpf o CPF do cliente a ser consultado
     * @return um Optional contendo o cliente encontrado, ou vazio se não existir
     */
	public Optional<Cliente> consultarPorCpf(String cpf);
	//Req11 Consulta agrupada pelo CEP (nao implementado)
	/**
     * Req12 Atualizar informações do cliente - Atualiza os dados de um cliente a partir do objeto ClienteDTO.
     * @param cliente objeto ClienteDTO contendo os dados atualizados do cliente
     * @return um Optional contendo o cliente atualizado, ou vazio se não existir
     * @throws IllegalArgumentException se os dados informados forem inválidos
     */
	public Optional<Cliente> atualizar(ClienteDTO cliente);
	/**
     * Req13 Exclusão pelo CPF - Exclui um cliente pelo CPF.
     * @param cpf o CPF do cliente a ser excluído
     * @return true se o cliente foi excluído com sucesso, ou false se não encontrado
     */
	public boolean excluir(String cpf);
}
