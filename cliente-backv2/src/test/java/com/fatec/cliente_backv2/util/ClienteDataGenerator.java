package com.fatec.cliente_backv2.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/**
 * Gera o arquivo CSV de clientes.
 *
 */
public class ClienteDataGenerator {

    private static final int NUM_REGISTROS = 100;
    // Define o local onde o arquivo sera gerado para o Classpath
    private static final String NOME_ARQUIVO = "src/main/resources/dataset100.csv"; 
    
    // Arrays para dados de exemplo
    private static final String[] NOMES = {"Carlos", "Ana", "Bruno", "Celia", "Daniel", "Eduarda", "Fabio", "Gabriela", "Henrique", "Isabela"};
    private static final String[] SOBRENOMES = {"Silva", "Santos", "Rocha", "Xavier", "Lima", "Ferreira", "Gonzaga", "Helen", "Ibanez", "Jorge"};
    private static final String ENDERECO_BASE = "Rua Central";
    private static final String BAIRRO_BASE = "Vila Nair";
    private static final String CIDADE_BASE = "São Paulo";
    private static final String CEP_BASE = "04280130";
    private static final String EMAIL_DOMINIO = "@empresa.com";


    public static void main(String[] args) {
        System.out.println("Iniciando geração de " + NUM_REGISTROS + " registros...");
        
        // Usa hash Set para garantir que os CPFs sejam únicos
        Set<String> cpfsGerados = new HashSet<>();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            
            // 1. Escreve o cabeçalho
        
            bw.write("cpf,nome,cep,endereco,bairro,cidade,complemento,email,re"); 
            bw.newLine();

            // 2. Gera os registros
            for (int i = 1; i <= NUM_REGISTROS; i++) {
                
                String cpf;
                do {
                    cpf = CpfGenerator.generateValidCpf();
                // Garante unicidade e a validade (evitando sequências repetidas)
                } while (cpfsGerados.contains(cpf)); 
                
                cpfsGerados.add(cpf);
                
                String nome = NOMES[i % NOMES.length] + " " + SOBRENOMES[i % SOBRENOMES.length];
                String email = nome.toLowerCase().replace(" ", ".") + EMAIL_DOMINIO;
                String complemento = "Apto " + (i + 10);
                
                // Constrói a linha CSV
                String linha = String.format("%s,%s,%s,%s,%s,%s,%s,%s,", 
                    cpf,
                    nome,
                    CEP_BASE,
                    ENDERECO_BASE,
                    BAIRRO_BASE,
                    CIDADE_BASE,
                    complemento,
                    email);
                
                bw.write(linha);
                bw.newLine();
            }

            System.out.println("✅ Geração de dados concluída! Arquivo salvo em: " + NOME_ARQUIVO);
            System.out.println("Total de CPFs únicos gerados: " + cpfsGerados.size());

        } catch (IOException e) {
            System.err.println("❌ Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }
}