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
public class ProfileTest {

    @User(
            username = "Андрей"
    )
    @Test
    @DisplayName("Update profile info")
    public void updateProfileInfo(TestUser user) {
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
                .clickProfileMenuButton()
                .validateProfilePage()
                .changeFirstName(name)
                .changeSurname(surname)
                .changeLocation(location)
                .clickSaveButton()
                .checkSavedFirstName(name)
                .checkSavedSurname(surname)
                .checkSavedLocation(location);

    }
}
