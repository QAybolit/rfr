package io.student.rangiffler.test.api;

import io.student.rangiffler.api.UserApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiRegistrationTest {

    @Test
    @DisplayName("User registers and logs in using API")
    public void apiRegistrationTest() {
        UserApiClient client = new UserApiClient();
        try {
            client.registerUser("Ivan-10", "12345");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
