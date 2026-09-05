package io.student.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rangiffler.data.entity.user.PhotoEntity;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public record PhotoJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("country_id")
        UUID countryId,
        @JsonProperty("description")
        String description,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("created_date")
        Date createdDate
) {

    public static PhotoJson fromEntity(PhotoEntity entity) {
        return new PhotoJson(
                entity.getId(),
                entity.getUser().getId(),
                entity.getCountry().getId(),
                entity.getDescription(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
                entity.getCreatedDate()
        );
    }
}
