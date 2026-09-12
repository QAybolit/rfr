package io.student.rangiffler.service.mapper;

import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.model.types.Photo;
import io.student.rangiffler.model.types.PhotoInput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.UUID;

import static io.student.rangiffler.service.mapper.CountryMapper.countryFromEntity;
import static io.student.rangiffler.service.mapper.LikesMapper.likesFromEntity;

@ParametersAreNonnullByDefault
public class PhotoMapper {

    public static @Nonnull Photo photoFromEntity(PhotoEntity entity) {
        return Photo.newBuilder()
                .id(entity.getId().toString())
                .src(entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null)
                .country(entity.getCountry() != null ? countryFromEntity(entity.getCountry()) : null)
                .description(entity.getDescription())
                .creationDate(entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .likes(entity.getLikes() != null ? likesFromEntity(entity.getLikes()) : null)
                .isOwner(false)
                .build();
    }

    public static @Nonnull PhotoEntity entityFromPhoto(PhotoInput photo) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setId(UUID.fromString(photo.getId()));
        photoEntity.setDescription(photo.getDescription());
        photoEntity.setPhoto(photo.getSrc() != null ? photo.getSrc().getBytes(StandardCharsets.UTF_8) : loadPhoto());
        return photoEntity;
    }

    private static @Nullable byte[] loadPhoto() {
        try {
            Resource resource = new ClassPathResource("images/default.jpg");
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            // NOP
            return null;
        }
    }
}
