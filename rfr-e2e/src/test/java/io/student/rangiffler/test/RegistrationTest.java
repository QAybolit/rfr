package io.student.rangiffler.test;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.jupiter.extension.BrowserExtension;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static io.student.rangiffler.utils.FakeDataUtils.getRandomName;
import static io.student.rangiffler.utils.FakeDataUtils.getRandomPassword;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    static final Config CONFIG = Config.getInstance();

    @Test
    @DisplayName("Successful registration of a new user")
    public void shouldRegisterNewUser() {
        String password = getRandomPassword();

        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickRegistrationButton()
                .validateRegistrationPage()
                .enterUsername(getRandomName())
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
                .enterUsername(getRandomName())
                .enterPassword(getRandomPassword())
                .enterPasswordSubmit(getRandomPassword())
                .submitRegistrationFormWithBadCredentials()
                .checkFormError("Passwords should be equal");
    }

    @User
    @Test
    @DisplayName("Unsuccessful registration of a new user with an existing username")
    public void shouldNotRegisterNewUserWithExistingUsername(UserJson user) {
        String password = getRandomPassword();

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
