package io.student.rangiffler.utils;

import com.github.javafaker.Faker;

public class FakeDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.commerce().productName();
    }

    public static String randomSentence(int wordCount) {
        return faker.lorem().sentence(wordCount);
    }

    public static String randomPassword() {
        return faker.internet().password(4, 8);
    }
}