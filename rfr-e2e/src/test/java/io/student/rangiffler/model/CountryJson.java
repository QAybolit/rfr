package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rangiffler.data.entity.user.CountryEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("code")
        String code,
        @JsonProperty("flag")
        String flag
) {

    public static CountryJson fromEntity(CountryEntity entity) {
        return new CountryJson(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getFlag() != null && entity.getFlag().length > 0 ? new String(entity.getFlag(), StandardCharsets.UTF_8) : null
        );
    }
}
