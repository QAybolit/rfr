package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage extends BasePage {

    private final SelenideElement updateAvatarButton = $x("//button[text()='Update avatar']");
    private final SelenideElement resetButton = $x("//button[text()='Reset']");
    private final SelenideElement saveButton = $x("//button[text()='Save']");
    private final SelenideElement avatarImage = $("svg[data-testid='PersonIcon']");
    private final SelenideElement firstNameInput = $("input#firstname");
    private final SelenideElement surnameInput = $("input#surname");
    private final SelenideElement usernameInput = $("input#username");
    private final SelenideElement locationDropdown = $("div#location");
    private final ElementsCollection locationOptions = $$("li[role='option']");
}
