package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
