package io.student.rangiffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    private final SelenideElement alert = $("[role='presentation']");
    private final SelenideElement alertMessage = $("div.MuiAlert-message");

    @Step("Alert shoul have text '{text}'")
    public T checkAlert(String text) {
        alert.shouldBe(Condition.visible).should(text(text));
        return (T) this;
    }
}
