package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BaseStartPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $(".form__submit");
    private final SelenideElement loginLink = $(".form__link");

    public RegistrationPage validateRegistrationPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.usernameInput.shouldBe(visible);
        this.passwordInput.shouldBe(visible);
        this.passwordSubmitInput.shouldBe(visible);
        this.loginLink.shouldBe(visible);
        return this;
    }

    public RegistrationPage enterUsername(String username) {
        this.usernameInput.setValue(username);
        return this;
    }

    public RegistrationPage enterPassword(String password) {
        this.passwordInput.setValue(password);
        return this;
    }

    public RegistrationPage submitPassword(String password) {
        this.passwordSubmitInput.setValue(password);
        return this;
    }

    public LoginPage submitRegistrationForm() {
        this.submitButton.clear();
        return new LoginPage();
    }
}
