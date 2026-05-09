package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement header = $("h1.MuiTypography-root");
    private final SelenideElement menuButton = $("button[aria-label='open drawer']");
    private final SelenideElement profileButton = $("[data-testid='AccountCircleRoundedIcon']");
    private final SelenideElement travelsMapButton = $("[data-testid='PublicRoundedIcon']");
    private final SelenideElement personSearchButton = $("[data-testid='PersonSearchRoundedIcon']");
    private final SelenideElement worldMapImage = $(".worldmap__figure-container");
    private final SelenideElement logoutButton = $("[aria-label='Logout']");

    @Step("Validate Main Page elements")
    public MainPage validateMainPage() {
        this.header.shouldBe(visible);
        this.menuButton.shouldBe(visible);
        this.profileButton.shouldBe(visible);
        this.travelsMapButton.shouldBe(visible);
        this.personSearchButton.shouldBe(visible);
        this.worldMapImage.shouldBe(visible);
        this.logoutButton.shouldBe(visible);
        return this;
    }
}
