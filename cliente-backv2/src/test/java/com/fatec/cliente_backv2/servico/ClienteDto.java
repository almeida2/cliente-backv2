package com.fatec.cliente_backv2.servico;

public record ClienteDto(
	    String cpf,
	    String nome,
	    String cep,
	    String complemento,
	    String email
	    
	) { }
