package com.fatec.cliente_backv2.servico;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.model.ClienteDTO;
import com.fatec.cliente_backv2.service.IClienteService;
@SpringBootTest
class Req12AtualizarInformacoesDeClienteTests {

	@Autowired
	IClienteService servico;
	ClienteDTO cliente;
	Cliente c = null;
	private static final String CPF_CLIENTE = "44603843020";
	
	
}
