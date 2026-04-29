package io.student.rangiffler.test;

import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest extends BaseTest {

    @Test
    public void validateMainPageAfterSuccessLogin() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername("Alex")
                .enterPassword("Alex123!")
                .submitLoginForm()
                .validateMainPage();
    }

    @Test
    public void shouldNotLoginWithInvalidUsername() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername("ПУПУПУ")
                .enterPassword("Alex123!")
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }

    @Test
    public void shouldNotLoginWithInvalidPassword() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername("Alex")
                .enterPassword("Alex123")
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }
}
