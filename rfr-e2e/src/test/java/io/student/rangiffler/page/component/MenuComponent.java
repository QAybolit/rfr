package io.student.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MenuComponent extends BaseComponent<MenuComponent> {

    private final SelenideElement profileButton = $("[data-testid='AccountCircleRoundedIcon']");
    private final SelenideElement feedPageButton = $("[data-testid='PublicRoundedIcon']");
    private final SelenideElement peoplePageButton = $("[data-testid='PersonSearchRoundedIcon']");

    public MenuComponent() {
        super($("div.MuiPaper-elevation ul"));
    }

    @Step("Validate menu is visible")
    public void validateMenuIsVisible() {
        this.profileButton.shouldBe(visible);
        this.feedPageButton.shouldBe(visible);
        this.peoplePageButton.shouldBe(visible);
    }

    @Step("Click profile menu button")
    public void clickProfileMenuButton() {
        this.profileButton.click();
    }

    @Step("Click feed page menu button")
    public void clickFeedPageMenuButton() {
        this.feedPageButton.click();
    }

    @Step("Click people page menu button")
    public void clickPeoplePageButton() {
        this.peoplePageButton.click();
    }
}
