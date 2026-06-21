package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected final SelenideElement header = $("h1.MuiTypography-root");
    protected final SelenideElement menuButton = $("button[aria-label='open drawer']");
    protected final SelenideElement profileButton = $("[data-testid='AccountCircleRoundedIcon']");
    protected final SelenideElement travelsMapButton = $("[data-testid='PublicRoundedIcon']");
    protected final SelenideElement personSearchButton = $("[data-testid='PersonSearchRoundedIcon']");
    protected final SelenideElement logoutButton = $("[aria-label='Logout']");

    @Step("Click profile menu button")
    public ProfilePage clickProfileMenuButton() {
        this.profileButton.click();
        return new ProfilePage();
    }

    @Step("Click travel map menu button")
    public TravelMapPage clickTravelMapMenuButton() {
        this.travelsMapButton.click();
        return new TravelMapPage();
    }

    @Step("Click person search menu button")
    public SearchPeoplePage clickPersonSearchButton() {
        this.personSearchButton.click();
        return new SearchPeoplePage();
    }

    @Step("Click logout button")
    public EnterPage clickLogoutButton() {
        this.logoutButton.click();
        return new EnterPage();
    }
}
