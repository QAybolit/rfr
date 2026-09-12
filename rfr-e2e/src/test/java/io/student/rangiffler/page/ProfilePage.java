package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.page.component.SelectField;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage extends BasePage {

    public static String URL = Config.getInstance().frontUrl() + "/profile";

    private final SelenideElement profileTitle = $("div.MuiGrid-item h2");
    private final SelenideElement updateAvatarButton = $x("//button[text()='Update avatar']");
    private final SelenideElement resetButton = $x("//button[text()='Reset']");
    private final SelenideElement saveButton = $x("//button[text()='Save']");
    private final SelenideElement avatarImage = $("svg[data-testid='PersonIcon']");
    private final SelenideElement firstNameInput = $("input#firstname");
    private final SelenideElement surnameInput = $("input#surname");
    private final SelenideElement usernameInput = $("input#username");

    private final SelectField locationDropdown = new SelectField($("div#location"));

    @Step("Validate Profile Page elements")
    public ProfilePage validateProfilePage() {
        this.header.checkHeaderVisibility();
        this.profileTitle.shouldBe(visible).shouldHave(text("My profile"));
        return this;
    }

    @Step("Change first name to {firstName}")
    public ProfilePage changeFirstName(String firstName) {
        this.firstNameInput.setValue(firstName);
        return this;
    }

    @Step("Change surname to {surname}")
    public ProfilePage changeSurname(String surname) {
        this.surnameInput.setValue(surname);
        return this;
    }

    @Step("Change location to {location}")
    public ProfilePage changeLocation(String location) {
        this.locationDropdown.selectOptionFromDropdownByText(location);
        return this;
    }

    @Step("Click save button")
    public ProfilePage clickSaveButton() {
        this.saveButton.click();
        return this;
    }

    @Step("Check saved first name is equals to {firstName}")
    public ProfilePage checkSavedFirstName(String firstName) {
        this.firstNameInput.shouldBe(visible).shouldHave(value(firstName));
        return this;
    }

    @Step("Check saved surname is equals to {surname}")
    public ProfilePage checkSavedSurname(String surname) {
        this.surnameInput.shouldBe(visible).shouldHave(value(surname));
        return this;
    }

    @Step("Check saved location is equals to {location}")
    public ProfilePage checkSavedLocation(String location) {
        this.locationDropdown.selectOptionFromDropdownByText(location);
        return this;
    }


}
