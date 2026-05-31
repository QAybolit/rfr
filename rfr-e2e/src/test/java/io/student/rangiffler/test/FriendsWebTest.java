package io.student.rangiffler.test;

import io.student.rangiffler.jupiter.extension.UsersQueueExtension;
import io.student.rangiffler.jupiter.extension.UsersQueueExtension.StaticUser;
import io.student.rangiffler.jupiter.extension.UsersQueueExtension.UserType;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static io.student.rangiffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static io.student.rangiffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIENDS;
import static io.student.rangiffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static io.student.rangiffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

public class FriendsWebTest extends BaseTest {

    @Test
    @ExtendWith(UsersQueueExtension.class)
    @DisplayName("Display friends in friends list for user")
    public void friendShouldBePresentInFriendsTable(@UserType(value = WITH_FRIENDS) StaticUser user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.password())
                .submitLoginForm()
                .validateTravelMapPage()
                .clickPersonSearchButton()
                .checkIncomeListIsEmpty()
                .checkOutcomeListIsEmpty()
                .checkFriendsListContainsName(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    @DisplayName("No people in friends list, income list and outcome list for user")
    public void friendsTableShouldBeEmptyForNewUser(@UserType(value = EMPTY) StaticUser user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.password())
                .submitLoginForm()
                .validateTravelMapPage()
                .clickPersonSearchButton()
                .checkFriendsListIsEmpty()
                .checkIncomeListIsEmpty()
                .checkOutcomeListIsEmpty();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    @DisplayName("Display income invitation in income list for user")
    public void incomeInvitationBePresentInFriendsTable(@UserType(value = WITH_INCOME_REQUEST) StaticUser user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.password())
                .submitLoginForm()
                .validateTravelMapPage()
                .clickPersonSearchButton()
                .checkFriendsListIsEmpty()
                .checkOutcomeListIsEmpty()
                .checkIncomeListContainsName(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    @DisplayName("Display outcome invitation in outcome list for user")
    public void outcomeInvitationBePresentInAllPeoplesTable(@UserType(value = WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.username())
                .enterPassword(user.password())
                .submitLoginForm()
                .validateTravelMapPage()
                .clickPersonSearchButton()
                .checkFriendsListIsEmpty()
                .checkIncomeListIsEmpty()
                .checkOutcomeListContainsName(user.outcome());
    }
}
