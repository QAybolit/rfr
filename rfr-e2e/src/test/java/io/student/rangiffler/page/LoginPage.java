package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BaseStartPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $(".form__submit");
    private final SelenideElement registrationLink = $(".form__link");

    public LoginPage validateLoginPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.usernameInput.shouldBe(visible);
        this.passwordInput.shouldBe(visible);
        this.registrationLink.shouldBe(visible);
        return this;
    }

    public LoginPage enterUsername(String username) {
        this.usernameInput.setValue(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        this.passwordInput.setValue(password);
        return this;
    }

    public MainPage submitRegistrationForm() {
        this.submitButton.clear();
        return new MainPage();
    }
}
