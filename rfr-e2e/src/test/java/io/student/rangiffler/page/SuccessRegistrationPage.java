package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SuccessRegistrationPage {

    private final SelenideElement logo = $(".header__logo");
    private final SelenideElement header = $("h1.header");
    private final SelenideElement mainImage = $(".main__hero");
    private final SelenideElement successMessage = $(".form__paragraph_success");
    private final SelenideElement signInButton = $(".form_sign-in");

    @Step("Validate Success Registration Page elements")
    public SuccessRegistrationPage validateSuccessRegistrationPage() {
        this.header.shouldBe(visible);
        this.logo.shouldBe(visible);
        this.mainImage.shouldBe(visible);
        this.signInButton.shouldBe(visible);
        this.successMessage.shouldBe(visible).shouldHave(text("Congratulations! You've registered!"));
        return this;
    }
}
