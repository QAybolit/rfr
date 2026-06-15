package io.student.rangiffler.test;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.extension.BrowserExtension;
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

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    static final Config CONFIG = Config.getInstance();

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
                .goToIncomeTab()
                .checkIncomeListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListIsEmpty()
                .goToFriendsTab()
                .checkFriendsListContainsName(user.friend())
                .checkFriendsListContainsName("Russian Federation");
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
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListIsEmpty()
                .goToOutcomeTab()
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
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListContainsText(user.income())
                .checkIncomeListContainsText("Russian Federation")
                .checkIncomeListContainsAcceptButtons()
                .checkIncomeListContainsDeclineButtons();
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
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListContainsText(user.outcome())
                .checkOutcomeListContainsText("Russian Federation")
                .checkOutcomeListContainsStatus();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    @DisplayName("Outcome invitation be declined by another user")
    public void outcomeInvitationBeDecline(@UserType(value = WITH_OUTCOME_REQUEST) StaticUser outcomeUser,
                                           @UserType(value = EMPTY) StaticUser incomeUser) {
        open(CONFIG.frontUrl(), EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(outcomeUser.username())
                .enterPassword(outcomeUser.password())
                .submitLoginForm()
                .validateTravelMapPage()
                .clickPersonSearchButton()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListIsEmpty()
                .findPerson(incomeUser.username())
                .inviteFriend(incomeUser.username())
                .refreshPage()
                .goToOutcomeTab()
                .checkOutcomeListContainsText(incomeUser.username())
                .clickLogoutButton()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(incomeUser.username())
                .enterPassword(incomeUser.password())
                .submitLoginForm()
                .clickPersonSearchButton()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListContainsText(outcomeUser.username())
                .clickDeclineButtonAtFirstRow()
                .refreshPage()
                .goToIncomeTab()
                .checkIncomeListIsEmpty();
    }
}
