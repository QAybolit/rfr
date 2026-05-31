package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class TravelMapPage extends BasePage {

    private final SelenideElement mapTitle = $x("//h2[text()='Travels map']");
    private final SelenideElement worldMapImage = $(".worldmap__figure-container");
    private final SelenideElement myTravelsButton = $x("//button[text()='Only my travels']");
    private final SelenideElement withFriendsTravelsButton = $x("//button[text()='With friends']");
    private final SelenideElement addPhotoButton = $x("//button[text()='Add photo']");
    private final SelenideElement previousButton = $x("//button[text()='Previous']");
    private final SelenideElement nextButton = $x("//button[text()='Next']");

    @Step("Validate Main Page elements")
    public TravelMapPage validateMainPage() {
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
