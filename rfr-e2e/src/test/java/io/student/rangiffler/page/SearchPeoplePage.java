package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
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
    private final ElementsCollection outcomeList = $$("#simple-tabpanel-outcome tbody tr");
    private final ElementsCollection incomeList = $$("#simple-tabpanel-income tbody tr");

    @Step("Check friends list is empty")
    public SearchPeoplePage checkFriendsListIsEmpty() {
        this.friendsTab.click();
        this.friendsList.isEmpty();
        return this;
    }

    @Step("Check friends list is not empty")
    public SearchPeoplePage checkFriendsListIsNotEmpty() {
        this.friendsTab.click();
        this.friendsList.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Check outcome list is empty")
    public SearchPeoplePage checkOutcomeListIsEmpty() {
        this.outcomeInvitationsTab.click();
        this.outcomeList.isEmpty();
        return this;
    }

    @Step("Check outcome list is not empty")
    public SearchPeoplePage checkOutcomeListIsNotEmpty() {
        this.outcomeInvitationsTab.click();
        this.outcomeList.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Check income list is empty")
    public SearchPeoplePage checkIncomeListIsEmpty() {
        this.incomeInvitationsTab.click();
        this.incomeList.isEmpty();
        return this;
    }

    @Step("Check income list is not empty")
    public SearchPeoplePage checkIncomeListIsNotEmpty() {
        this.incomeInvitationsTab.click();
        this.incomeList.shouldHave(sizeGreaterThan(0));
        return this;
    }
}
