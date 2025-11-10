package com.fatec.cliente_backv2.view;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;

@UsePlaywright
public class MantemClienteE2E {
	@Test
	void test(Page page) {
		page.navigate("http://localhost:5173/");
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cadastrar cliente")).click();
		page.getByTestId("cpf").click();
		page.getByTestId("cpf").fill("99504993052");
		page.getByTestId("cpf").press("Tab");
		page.getByTestId("nome").fill("Carlos Silva");
		page.getByTestId("nome").press("Tab");
		page.getByTestId("cep").fill("04280130");
		page.getByTestId("cep").press("Tab");
		page.getByTestId("endereco").press("Tab");
		page.getByTestId("bairro").press("Tab");
		page.getByTestId("cidade").press("Tab");
		page.getByTestId("complemento").fill("123");
		page.getByTestId("complemento").press("Tab");
		page.getByTestId("email").fill("jose@gmail.com");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirmar")).click();
		assertThat(page.getByTestId("mensagem")).containsText("Cliente cadastrado com sucesso");
		System.out.println(String.format("Cliente cadastrado"));
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Voltar")).click();
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Excluir cliente por CPF")).click();
		page.getByTestId("excluir-cpf-input").click();
		page.getByTestId("excluir-cpf-input").fill("99504993052");
		page.onceDialog(dialog -> {
			System.out.println(String.format("Dialog message (alert): %s", dialog.message()));
			dialog.accept();
		});
		page.getByTestId("excluir-cliente-button").click();
		assertThat(page.getByRole(AriaRole.STATUS)).containsText("Cliente excluído com sucesso.");
		page.getByTestId("excluir-voltar-button").click();
		 page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Consultar cliente por CPF")).click();
		System.out.println(String.format("Opção consulta cliente por CPF selecionada"));
		page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("CPF:")).click();
	    page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("CPF:")).fill("99504993052");
	    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Consultar")).click();
	    assertThat(page.getByRole(AriaRole.PARAGRAPH)).containsText("CPF não encontrado.");
		// Salva o arquivo de screenshot na raiz do projeto
		page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
		// Salva o arquivo de screenshot configurando nome e local definido na classe
		// ScreenshotUtil
		ScreenshotUtil.takeScreenshot(page, "ManterClienteE2E");

	}
}