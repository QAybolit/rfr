package io.student.rangiffler.service.mapper;

import io.student.rangiffler.data.entity.user.LikeEntity;
import io.student.rangiffler.model.types.Like;
import io.student.rangiffler.model.types.Likes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class LikesMapper {

    public static @Nonnull Likes likesFromEntity(Set<LikeEntity> entities) {
        int total = entities.size();
        List<Like> likes = entities.stream()
                .map(e -> Like.newBuilder()
                        .user(e.getUser().getId().toString())
                        .username(e.getUser().getUsername())
                        .creationDate(e.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .build()
                ).toList();
        return new Likes(total, likes);
    }
}
