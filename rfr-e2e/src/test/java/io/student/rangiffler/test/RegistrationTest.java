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
public class RegistrationTest {

    static final Config CONFIG = Config.getInstance();

    @Test
    @DisplayName("Successful registration of a new user")
    public void shouldRegisterNewUser() {
        String password = randomPassword();

        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername(randomUsername())
                .enterPassword(password)
                .enterPasswordSubmit(password)
                .submitRegistrationForm()
                .validateSuccessRegistrationPage();
    }

    @Test
    @DisplayName("Unsuccessful registration of a new user with an invalid password")
    public void shouldNotRegisterNewUserWithInvalidPasswordSubmit() {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername(randomUsername())
                .enterPassword(randomPassword())
                .enterPasswordSubmit(randomPassword())
                .submitRegistrationFormWithBadCredentials()
                .checkFormError("Passwords should be equal");
    }

    @User(username = "Alexandr")
    @Test
    @DisplayName("Unsuccessful registration of a new user with an existing username")
    public void shouldNotRegisterNewUserWithExistingUsername(UserJson user) {
        String password = randomPassword();

        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername(user.username())
                .enterPassword(password)
                .enterPasswordSubmit(password)
                .submitRegistrationFormWithBadCredentials()
                .checkFormError("Username `" + user.username() + "` already exists");
    }
}
