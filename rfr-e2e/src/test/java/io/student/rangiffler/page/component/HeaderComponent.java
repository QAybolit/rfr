package io.student.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent {

    private final SelenideElement self = $("body header");

    private final SelenideElement logoutButton = self.$("[aria-label='Logout']");
    private final SelenideElement header = self.$("h1");
    private final SelenideElement menuButton = self.$("button[aria-label='open drawer']");

    @Step("Click logout button")
    public void clickLogoutButton() {
        this.logoutButton.click();
    }

    @Step("Check the header is visible")
    public void checkHeaderVisibility() {
        this.header.shouldBe(visible);
        this.menuButton.shouldBe(visible);
        this.logoutButton.shouldBe(visible);
    }
}
