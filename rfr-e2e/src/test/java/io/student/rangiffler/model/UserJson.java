package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("enabled")
        boolean enabled,
        @JsonProperty("account_non_expired")
        boolean accountNonExpired,
        @JsonProperty("account_non_locked")
        boolean accountNonLocked,
        @JsonProperty("credentials_non_expired")
        boolean credentialsNonExpired
) {
}
