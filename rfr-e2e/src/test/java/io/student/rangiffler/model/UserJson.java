package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rangiffler.data.entity.user.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("avatar")
        String avatar,
        @JsonProperty("country_id")
        UUID countryId,
        @JsonIgnore
        TestData testData
) {
    public static UserJson fromEntity(UserEntity entity) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastName(),
                entity.getAvatar() != null && entity.getAvatar().length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null,
                entity.getCountry().getId(),
                null
        );
    }

    public UserJson addTestData(TestData testData) {
        return new UserJson(
                id, username, firstname, lastName, avatar, countryId, testData
        );
    }
}
