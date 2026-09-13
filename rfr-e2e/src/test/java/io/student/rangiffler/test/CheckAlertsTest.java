package io.student.rangiffler.test;

import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.jupiter.annotation.meta.WebTest;
import io.student.rangiffler.model.TestUser;
import io.student.rangiffler.page.EnterPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.student.rangiffler.utils.FakeDataUtils.randomName;
import static io.student.rangiffler.utils.FakeDataUtils.randomSurname;

@WebTest
public class CheckAlertsTest {

    @User(username = "Варвара")
    @Test
    @DisplayName("Check alert message at Profile page")
    public void checkAlertAtProfilePageTest(TestUser user) {
        String name = randomName();
        String surname = randomSurname();
        String location = "Sweden";

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .goToProfilePage()
                .validateProfilePage()
                .changeFirstName(name)
                .changeSurname(surname)
                .changeLocation(location)
                .clickSaveButton()
                .checkAlert("Your profile is successfully updated");
    }

    @User(
            username = "Эльвира",
            incomeInvitations = 1
    )
    @Test
    @DisplayName("Check alert message by income invitation")
    public void checkAlertByIncomeInvitationTest(TestUser user) {
        String requesterName = user.user().getIncomeInvitations().stream().findFirst().get().getUsername();

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .goToPeoplePage()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToIncomeTab()
                .checkIncomeListContainsText(requesterName)
                .clickAcceptButtonAtRowWithText(requesterName)
                .checkAlert("Invitation accepted");
    }

    @User(
            username = "София",
            otherPeoples = 1
    )
    @Test
    @DisplayName("Check alert message by outcome invitation")
    public void checkAlertByOutcomeInvitationTest(TestUser user) {
        String addressee = user.testData().otherPeople().get(0).getUsername();

        open(EnterPage.URL, EnterPage.class)
                .validateEnterPage()
                .clickLoginButton()
                .validateLoginPage()
                .enterUsername(user.user().getUsername())
                .enterPassword(user.testData().password())
                .submitLoginForm()
                .validateFeedPage()
                .goToPeoplePage()
                .goToFriendsTab()
                .checkFriendsListIsEmpty()
                .goToOutcomeTab()
                .checkOutcomeListIsEmpty()
                .goToAllPeopleTab()
                .findPerson(addressee)
                .inviteFriend(addressee)
                .checkAlert("Invitation sent");
    }
}
