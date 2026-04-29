package io.student.rangiffler.test;

import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class RegistrationTest extends BaseTest {

    @Test
    public void shouldRegisterNewUser() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername("Nikita1")
                .enterPassword("Nik123!")
                .enterPasswordSubmit("Nik123!")
                .submitRegistrationForm()
                .validateSuccessRegistrationPage();
    }

    @Test
    public void shouldNotRegisterNewUserWithInvalidPasswordSubmit() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername("Pasha")
                .enterPassword("Pasha123!")
                .enterPasswordSubmit("Pasha!123")
                .submitRegistrationFormWithBadCredentials()
                .checkFormError("Passwords should be equal");
    }

    @Test
    public void shouldNotRegisterNewUserWithExistingUsername() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername("Alex")
                .enterPassword("Pasha123!")
                .enterPasswordSubmit("Pasha123!")
                .submitRegistrationFormWithBadCredentials()
                .checkFormError("Username `" + "Alex" + "` already exists");
    }
}
