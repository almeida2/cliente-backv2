package com.fatec.cliente_backv2.view;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
public class Req09CadastrarClienteTests {
  @Test
  void test(Page page) {
    page.navigate("http://localhost:5173/");
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cadastrar cliente")).click();
    page.getByTestId("cpf").click();
    page.getByTestId("cpf").fill("99504993052");
    page.getByTestId("nome").click();
    page.getByTestId("nome").fill("jose");
    page.getByTestId("nome").press("Tab");
    page.getByTestId("cep").fill("08545160");
    page.getByTestId("complemento").click();
    page.getByTestId("complemento").fill("123");
    page.getByTestId("email").click();
    page.getByTestId("email").fill("jose@gmail.com");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirmar")).click();
    assertThat(page.getByTestId("mensagem")).containsText("Cliente cadastrado com sucesso");
  }
}
