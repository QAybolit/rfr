package io.student.rangiffler.utils;

import com.github.javafaker.Faker;

public class DataUtils {

    private static final Faker faker = new Faker();

    public static String getRandomName() {
        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        return faker.regexify("[a-zA-Z0-9!?]{5,8}");
    }
}
