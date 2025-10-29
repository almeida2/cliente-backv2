package com.fatec.cliente_backv2.util;
import java.util.Random;

public class CpfGenerator {

    private static final Random random = new Random();

    /**
     * Verifica se todos os dígitos de um array são iguais (e.g., [1,1,1,...]).
     */
    private static boolean isAllEqualDigits(int[] digits) {
        if (digits.length == 0) return false;
        int first = digits[0];
        for (int digit : digits) {
            if (digit != first) return false;
        }
        return true;
    }

    /**
     * Gera um número de CPF válido e que não seja uma sequência de dígitos iguais.
     * @return String com o CPF válido (apenas dígitos).
     */
    public static String generateValidCpf() {
        int[] base;

        // 1. Gera 9 dígitos base aleatórios, garantindo que não sejam todos iguais
        do {
            base = new int[9];
            for (int i = 0; i < 9; i++) {
                base[i] = random.nextInt(10);
            }
        } while (isAllEqualDigits(base)); // Verifica se todos os dígitos são iguais

        // 2. Calcula o primeiro dígito verificador (D10)
        int d1 = calculateVerificationDigit(base, 9);
        
        // 3. Adiciona o primeiro dígito verificador para calcular o segundo
        int[] withD1 = new int[10];
        System.arraycopy(base, 0, withD1, 0, 9);
        withD1[9] = d1;
        
        // 4. Calcula o segundo dígito verificador (D11)
        int d2 = calculateVerificationDigit(withD1, 10);
        
        // 5. Constrói o CPF completo (11 dígitos)
        StringBuilder cpfBuilder = new StringBuilder();
        for (int digit : base) {
            cpfBuilder.append(digit);
        }
        cpfBuilder.append(d1).append(d2);

        return cpfBuilder.toString();
    }

    /**
     * Calcula um dígito verificador do CPF.
     * @param digits Os dígitos atuais (9 ou 10).
     * @param length O número de dígitos a considerar.
     * @return O dígito verificador calculado.
     */
    private static int calculateVerificationDigit(int[] digits, int length) {
        int sum = 0;
        // O peso inicia em 10 para o primeiro DV (length=9) e em 11 para o segundo DV (length=10)
        int weight = length + 1; 
        
        for (int i = 0; i < length; i++) {
            sum += digits[i] * weight;
            weight--;
        }

        int remainder = sum % 11;
        // A regra é: Se o resto é menor que 2, o DV é 0. Caso contrário, é 11 - resto.
        return (remainder < 2) ? 0 : (11 - remainder);
    }
}
