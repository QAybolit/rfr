package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rangiffler.page.component.HeaderComponent;

import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected final SelenideElement menuButton = $("button[aria-label='open drawer']");
    protected final SelenideElement profileButton = $("[data-testid='AccountCircleRoundedIcon']");
    protected final SelenideElement feedPageButton = $("[data-testid='PublicRoundedIcon']");
    protected final SelenideElement peoplePageButton = $("[data-testid='PersonSearchRoundedIcon']");
    protected final HeaderComponent header = new HeaderComponent();

    @Step("Click profile menu button")
    public ProfilePage clickProfileMenuButton() {
        this.profileButton.click();
        return new ProfilePage();
    }

    @Step("Click feed page menu button")
    public FeedPage clickFeedPageMenuButton() {
        this.feedPageButton.click();
        return new FeedPage();
    }

    @Step("Click people page menu button")
    public PeoplePage clickPeoplePageButton() {
        this.peoplePageButton.click();
        return new PeoplePage();
    }

    @Step("Click logout button")
    public EnterPage clickLogoutButton() {
        this.header.clickLogoutButton();
        return new EnterPage();
    }
}
