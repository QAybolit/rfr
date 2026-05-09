package io.student.rangiffler.test;

import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.student.rangiffler.utils.DataUtils.getRandomName;
import static io.student.rangiffler.utils.DataUtils.getRandomPassword;

public class LoginTest extends BaseTest {

    @User
    @Test
    @DisplayName("Successful login")
    public void validateMainPageAfterSuccessLogin(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.password())
                .submitLoginForm()
                .validateMainPage();
    }

    @User
    @Test
    @DisplayName("Unsuccessful login with invalid username")
    public void shouldNotLoginWithInvalidUsername(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(getRandomName())
                .enterPassword(user.password())
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }

    @User
    @Test
    @DisplayName("Unsuccessful login with invalid password")
    public void shouldNotLoginWithInvalidPassword(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(getRandomPassword())
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }
}
