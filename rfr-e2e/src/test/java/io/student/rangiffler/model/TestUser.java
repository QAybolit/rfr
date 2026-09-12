package io.student.rangiffler.model;

import io.student.rangiffler.model.types.User;
import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.util.Nullable;

import javax.annotation.Nonnull;

public record TestUser(
        User user,
        TestData testData
) {

    public @Nonnull TestUser addTestData(@Nullable TestData testData) {
        return new TestUser(
                user, testData
        );
    }
}
