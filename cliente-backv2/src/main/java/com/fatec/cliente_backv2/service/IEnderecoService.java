package com.fatec.cliente_backv2.service;

import java.util.Optional;

import com.fatec.cliente_backv2.model.Endereco;

public interface IEnderecoService {
	 public Optional<Endereco> obtemLogradouroPorCep(String cep);
	 
}