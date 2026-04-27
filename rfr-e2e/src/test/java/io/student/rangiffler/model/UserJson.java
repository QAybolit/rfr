package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password
) {
}
