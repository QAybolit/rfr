package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement logo = $(".header__logo");
    private final SelenideElement header = $("h1.header");
    private final SelenideElement mainImage = $(".main__hero");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $(".form__submit");
    private final SelenideElement registrationLink = $(".form__link");
    private final SelenideElement formError = $(".form__error");

    @Step("Validate Login Page elements")
    public LoginPage validateLoginPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.usernameInput.shouldBe(visible);
        this.passwordInput.shouldBe(visible);
        this.registrationLink.shouldBe(visible);
        return this;
    }

    @Step("Enter username")
    public LoginPage enterUsername(String username) {
        this.usernameInput.setValue(username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        this.passwordInput.setValue(password);
        return this;
    }

    @Step("Submit login form")
    public MainPage submitLoginForm() {
        this.submitButton.click();
        return new MainPage();
    }

    @Step("Submit login form with bad credentials")
    public LoginPage submitLoginFormWithBadCredentials() {
        this.submitButton.click();
        return this;
    }

    @Step("Check form error is displayed")
    public MainPage checkFormError(String errorText) {
        this.formError.shouldBe(visible).shouldHave(text(errorText));
        return new MainPage();
    }
}
