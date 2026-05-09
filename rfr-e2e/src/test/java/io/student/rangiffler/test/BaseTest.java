package io.student.rangiffler.test;

import io.student.rangiffler.config.Config;
import org.junit.jupiter.api.AfterEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {

    static final Config CONFIG = Config.getInstance();

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
}
