package io.student.rangiffler.test;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.jupiter.annotation.meta.WebTest;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.student.rangiffler.utils.FakeDataUtils.randomPassword;
import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;

@WebTest
public class LoginTest {

    static final Config CONFIG = Config.getInstance();

    @User(username = "Maximus-4")
    @Test
    @DisplayName("Successful login")
    public void validateMainPageAfterSuccessLogin(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateTravelMapPage();
    }

    @User(username = "Maximus-5")
    @Test
    @DisplayName("Unsuccessful login with invalid username")
    public void shouldNotLoginWithInvalidUsername(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(randomUsername())
                .enterPassword(user.testData().password())
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }

    @User(username = "Maximus-6")
    @Test
    @DisplayName("Unsuccessful login with invalid password")
    public void shouldNotLoginWithInvalidPassword(UserJson user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(randomPassword())
                .submitLoginFormWithBadCredentials()
                .checkFormError("Неверные учетные данные пользователя");
    }
}
