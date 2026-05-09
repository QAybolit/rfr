package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class EnterPage {

    private final SelenideElement logo = $(".landing__logo");
    private final SelenideElement header = $("h2");
    private final SelenideElement mainImage = $(".landing__hero");
    private final SelenideElement loginButton = $x("//button[text()='Login']");
    private final SelenideElement registerButton = $x("//a[text()='Register']");

    @Step("Validate Enter Page elements")
    public EnterPage validateEnterPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.loginButton.shouldBe(visible);
        this.registerButton.shouldBe(visible);
        return this;
    }

    @Step("Click login button")
    public LoginPage clickLoginButton() {
        this.loginButton.click();
        return new LoginPage();
    }

    @Step("Click registration button")
    public RegistrationPage clickRegistrationButton() {
        this.registerButton.click();
        return new RegistrationPage();
    }
}
