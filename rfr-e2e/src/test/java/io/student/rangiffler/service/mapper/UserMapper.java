package io.student.rangiffler.service.mapper;

import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.model.types.FriendStatus;
import io.student.rangiffler.model.types.User;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;

import static io.student.rangiffler.service.mapper.CountryMapper.countryFromEntity;

@ParametersAreNonnullByDefault
public class UserMapper {

    public static @Nonnull User userFromEntity(UserEntity entity, FriendStatus friendStatus) {
        return User.newBuilder()
                .id(entity.getId().toString())
                .username(entity.getUsername())
                .firstname(entity.getFirstname())
                .surname(entity.getLastName())
                .avatar(entity.getAvatar() != null && entity.getAvatar().length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null)
                .friendStatus(friendStatus)
                .location(entity.getCountry() != null ? countryFromEntity(entity.getCountry()) : null)
                .build();
    }

}
