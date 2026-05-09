package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    private final SelenideElement logo = $(".header__logo");
    private final SelenideElement header = $("h1.header");
    private final SelenideElement mainImage = $(".main__hero");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $(".form__submit");
    private final SelenideElement loginLink = $(".form__link");
    private final SelenideElement formError = $(".form__error");

    @Step("Validate Registration Page elements")
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

    @Step("Enter username")
    public RegistrationPage enterUsername(String username) {
        this.usernameInput.setValue(username);
        return this;
    }

    @Step("Enter password")
    public RegistrationPage enterPassword(String password) {
        this.passwordInput.setValue(password);
        return this;
    }

    @Step("Enter password submit")
    public RegistrationPage enterPasswordSubmit(String password) {
        this.passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Submit registration form")
    public SuccessRegistrationPage submitRegistrationForm() {
        this.submitButton.click();
        return new SuccessRegistrationPage();
    }

    @Step("Submit registration form with bad credentials")
    public RegistrationPage submitRegistrationFormWithBadCredentials() {
        this.submitButton.click();
        return this;
    }

    @Step("Check form error is displayed")
    public RegistrationPage checkFormError(String errorText) {
        this.formError.shouldBe(visible).shouldHave(text(errorText));
        return this;
    }
}
