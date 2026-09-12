package io.student.rangiffler.test;

import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.jupiter.annotation.meta.WebTest;
import io.student.rangiffler.model.TestUser;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class InvitationsTest {

    @User(
            username = "Тихон-15",
            incomeInvitations = 1
    )
    @Test
    @DisplayName("Accept income invitation")
    public void acceptIncomeInvitationTest(TestUser user) {
        String requesterName = user.user().getIncomeInvitations().stream().findFirst().get().getUsername();

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .clickPeoplePageButton()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListContainsText(requesterName)
                .clickAcceptButtonAtRowWithText(requesterName)
                .refreshPage()
                .checkIncomeListIsEmpty()
                .goToFriendsTab()
                .checkFriendsListContainsName(requesterName);
    }

    @User(
            username = "Игорь-2",
            incomeInvitations = 1
    )
    @Test
    @DisplayName("Decline income invitation")
    public void declineIncomeInvitationTest(TestUser user) {
        String requesterName = user.user().getIncomeInvitations().stream().findFirst().get().getUsername();

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .clickPeoplePageButton()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListContainsText(requesterName)
                .clickDeclineButtonAtRowWithText(requesterName)
                .refreshPage()
                .checkIncomeListIsEmpty()
                .goToFriendsTab()
                .checkFriendsListIsEmpty();
    }

    @User(
            username = "Мирон-10",
            otherPeoples = 1
    )
    @Test
    @DisplayName("Create outcome invitation")
    public void createOutcomeInvitationTest(TestUser user) {
        String addressee = user.testData().otherPeople().get(0).getUsername();

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .clickPeoplePageButton()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListIsEmpty()
                .goToAllPeopleTab()
                .findPerson(addressee)
                .inviteFriend(addressee)
                .refreshPage()
                .goToOutcomeTab()
                .checkOutcomeListContainsText(addressee);
    }
}
