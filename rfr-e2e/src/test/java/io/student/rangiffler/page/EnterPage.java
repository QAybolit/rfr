package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class EnterPage extends BaseStartPage {

    private final SelenideElement loginButton = $("button.MuiButton-contained");
    private final SelenideElement registerButton = $("a.MuiButton-outlinedSizeMedium");

    public EnterPage validateLoginPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.loginButton.shouldBe(visible);
        this.registerButton.shouldBe(visible);
        return this;
    }

    public LoginPage clickLoginButton() {
        this.loginButton.click();
        return new LoginPage();
    }

    public RegistrationPage clickRegistrationButton() {
        this.registerButton.click();
        return new RegistrationPage();
    }
}
