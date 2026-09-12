package io.student.rangiffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FakeDataUtils {

    private static final Faker faker = new Faker();

    public static @Nonnull String randomUsername() {
        return faker.name().username();
    }

    public static @Nonnull String randomName() {
        return faker.name().firstName();
    }

    public static @Nonnull String randomSurname() {
        return faker.name().lastName();
    }

    public static @Nonnull String randomCategoryName() {
        return faker.commerce().productName();
    }

    public static @Nonnull String randomSentence(int wordCount) {
        return faker.lorem().sentence(wordCount);
    }

    public static @Nonnull String randomPassword() {
        return faker.internet().password(4, 8);
    }
}