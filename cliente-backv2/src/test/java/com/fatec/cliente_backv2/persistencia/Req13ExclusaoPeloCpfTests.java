package com.fatec.cliente_backv2.persistencia;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fatec.cliente_backv2.model.Cliente;
import com.fatec.cliente_backv2.service.IClienteRepository;
@DataJpaTest
class Req13ExclusaoPeloCpfTests {
	private Cliente cliente;
	@Autowired
	private IClienteRepository clienteRepository;
	
	

}
