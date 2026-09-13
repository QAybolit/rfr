package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.page.component.HeaderComponent;
import io.student.rangiffler.page.component.MenuComponent;
import io.student.rangiffler.page.component.PhotoCard;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class FeedPage extends BasePage<FeedPage> {

    public static String URL = Config.getInstance().frontUrl() + "/my-travels";

    private final SelenideElement mapTitle = $x("//h2[text()='Travels map']");
    private final SelenideElement worldMapImage = $(".worldmap__figure-container");
    private final SelenideElement myTravelsButton = $x("//button[text()='Only my travels']");
    private final SelenideElement withFriendsTravelsButton = $x("//button[text()='With friends']");
    private final SelenideElement addPhotoButton = $x("//button[text()='Add photo']");
    private final SelenideElement previousButton = $x("//button[text()='Previous']");
    private final SelenideElement nextButton = $x("//button[text()='Next']");

    private final HeaderComponent header = new HeaderComponent();
    private final MenuComponent menu = new MenuComponent();
    private final PhotoCard photoCard = new PhotoCard();

    @Step("Validate Feed Page elements")
    public FeedPage validateFeedPage() {
        this.header.checkHeaderVisibility();
        this.menu.validateMenuIsVisible();
        this.worldMapImage.shouldBe(visible);
        return this;
    }

    @Step("Go to people page")
    public PeoplePage goToPeoplePage() {
        this.menu.clickPeoplePageButton();
        return new PeoplePage();
    }

    @Step("Go to profile page")
    public ProfilePage goToProfilePage() {
        this.menu.clickProfileMenuButton();
        return new ProfilePage();
    }
}
