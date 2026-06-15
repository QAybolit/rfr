package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.anyMatch;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SearchPeoplePage extends BasePage {

    private final SelenideElement friendsTab = $x("//button[text()='Friends']");
    private final SelenideElement allPeopleTab = $x("//button[text()='All People']");
    private final SelenideElement outcomeInvitationsTab = $x("//button[text()='Outcome invitations']");
    private final SelenideElement incomeInvitationsTab = $x("//button[text()='Income invitations']");
    private final SelenideElement searchPeopleInput = $("input[aria-label='search people']");
    private final SelenideElement searchPeopleButton = $("button[aria-label='search']");
    private final ElementsCollection friendsList = $$("#simple-tabpanel-friends tbody tr");
    private final ElementsCollection allPeopleList = $$("#simple-tabpanel-all tbody tr");
    private final ElementsCollection addButtons = $$x("//td//button[text()='Add']");
    private final ElementsCollection outcomeList = $$("#simple-tabpanel-outcome tbody tr");
    private final ElementsCollection outcomeStatuses = $$x("//td//span[text()='Waiting...']");
    private final ElementsCollection incomeList = $$("#simple-tabpanel-income tbody tr");
    private final ElementsCollection acceptButtons = $$x("//td/button[text()='Accept']");
    private final ElementsCollection declineButtons = $$x("//td/button[text()='Decline']");

    @Step("Find person with name {}")
    public SearchPeoplePage findPerson(String name) {
        this.allPeopleTab.click();
        this.searchPeopleInput.setValue(name);
        this.searchPeopleButton.click();
        this.allPeopleList.shouldHave(size(1))
                .shouldHave(anyMatch("List contains name " + name, e -> e.getText().contains(name)));;
        return this;
    }

    @Step("Invite friend with name {}")
    public SearchPeoplePage inviteFriend(String name) {
        this.addButtons.first().click();
        return this;
    }

    @Step("Go to friends tab")
    public SearchPeoplePage goToFriendsTab() {
        this.friendsTab.click();
        return this;
    }

    @Step("Check friends list is empty")
    public SearchPeoplePage checkFriendsListIsEmpty() {
        this.friendsList.shouldBe(empty);
        return this;
    }

    @Step("Check friends list contains {}")
    public SearchPeoplePage checkFriendsListContainsName(String name) {
        this.friendsList.shouldHave(sizeGreaterThan(0));
        this.friendsList.shouldHave(anyMatch("List contains name " + name, e -> e.getText().contains(name)));
        return this;
    }

    @Step("Go to outcome tab")
    public SearchPeoplePage goToOutcomeTab() {
        this.outcomeInvitationsTab.click();
        return this;
    }

    @Step("Check outcome list is empty")
    public SearchPeoplePage checkOutcomeListIsEmpty() {
        this.outcomeList.shouldBe(empty);
        return this;
    }

    @Step("Check outcome list contains {}")
    public SearchPeoplePage checkOutcomeListContainsText(String name) {
        this.outcomeList.shouldHave(sizeGreaterThan(0));
        this.outcomeList.shouldHave(anyMatch("List contains name " + name, e -> e.getText().contains(name)));
        return this;
    }

    @Step("Check all rows in outcome list contain status 'Waiting'")
    public SearchPeoplePage checkOutcomeListContainsStatus() {
        this.outcomeStatuses.shouldBe(size(this.outcomeList.size()));
        return this;
    }

    @Step("Go to income tab")
    public SearchPeoplePage goToIncomeTab() {
        this.incomeInvitationsTab.click();
        return this;
    }

    @Step("Check income list is empty")
    public SearchPeoplePage checkIncomeListIsEmpty() {
        this.incomeList.shouldBe(empty);
        return this;
    }

    @Step("Check income list contains {}")
    public SearchPeoplePage checkIncomeListContainsText(String name) {
        this.incomeList.shouldHave(sizeGreaterThan(0));
        this.incomeList.shouldHave(anyMatch("List contains name " + name, e -> e.getText().contains(name)));
        return this;
    }

    @Step("Check all rows in income list contain Accept button")
    public SearchPeoplePage checkIncomeListContainsAcceptButtons() {
        this.acceptButtons.shouldBe(size(this.incomeList.size()));
        return this;
    }

    @Step("Check all rows in income list contain Decline button")
    public SearchPeoplePage checkIncomeListContainsDeclineButtons() {
        this.declineButtons.shouldBe(size(this.incomeList.size()));
        return this;
    }

    @Step("Click Decline button at first row")
    public SearchPeoplePage clickDeclineButtonAtFirstRow() {
        this.declineButtons.first().click();
        return this;
    }

    @Step("Refresh page")
    public SearchPeoplePage refreshPage() {
        Selenide.refresh();
        return this;
    }
}
