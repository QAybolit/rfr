package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.extractor.PhotoEntityExtractor;
import io.student.rangiffler.data.extractor.PhotoEntityListExtractor;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.tpl.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PhotoRepositorySpringJdbc implements PhotoRepository {

    private final JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));
    private static final Config CFG = Config.getInstance();

    @Override
    public PhotoEntity create(@NotNull PhotoEntity photo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO photo (user_id, country_id, description, photo, created_date)" +
                            " VALUES (?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, photo.getUser().getId());
            ps.setObject(2, photo.getCountry().getId());
            ps.setString(3, photo.getDescription());
            ps.setObject(4, photo.getPhoto());
            ps.setObject(5, new java.sql.Date(new Date().getTime()));
            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        photo.setId(generatedKey);

        return photo;
    }

    @Override
    public PhotoEntity update(@NotNull PhotoEntity photo) {
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE photo SET user_id = ?, country_id = ?, description = ?, photo = ?" +
                            " WHERE id = ?"
            );
            ps.setObject(1, photo.getUser().getId());
            ps.setObject(2, photo.getCountry().getId());
            ps.setString(3, photo.getDescription());
            ps.setObject(4, photo.getPhoto());
            ps.setObject(5, photo.getId());
            return ps;
        });

        return photo;
    }

    @Override
    public Optional<PhotoEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM photo p JOIN photo_like pl ON p.id = pl.photo_id JOIN like l ON pl.like_id = l.id WHERE p.id = ?",
                        PhotoEntityExtractor.INSTANCE,
                        id
                )
        );
    }

    @Override
    public Optional<PhotoEntity> findByUsernameAndDescription(@NotNull String username, @NotNull String description) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id JOIN photo_like pl ON p.id = pl.photo_id" +
                                " JOIN like l ON pl.like_id = l.id WHERE u.username = ? AND p.description = ?",
                        PhotoEntityExtractor.INSTANCE,
                        username, description
                )
        );
    }

    @Override
    public Optional<PhotoEntity> findByUsernameAndCountry(@NotNull String username, @NotNull String code) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id JOIN country c ON p.country_id = c.id" +
                                " JOIN photo_like pl ON p.id = pl.photo_id JOIN like l ON pl.like_id = l.id WHERE u.username = ? AND c.code = ?",
                        PhotoEntityExtractor.INSTANCE,
                        username, code
                )
        );
    }

    @Override
    public List<PhotoEntity> findAllUserPhotos(@NotNull String username) {
        List<PhotoEntity> photos = template.query(
                "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id WHERE u.username = ?",
                PhotoEntityListExtractor.INSTANCE,
                username
        );
        return photos;
    }

    @Override
    public void remove(@NotNull PhotoEntity photo) {
        template.update("DELETE FROM photo WHERE id = ?", photo.getId());
    }

    @Override
    public void addLike(@NotNull UUID photoId, @NotNull UUID userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        java.sql.Date date = new java.sql.Date(new Date().getTime());

        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO like (user_id, created_date) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, userId);
            ps.setObject(2, date);
            return ps;
        }, keyHolder);
        final UUID likeId = (UUID) keyHolder.getKeys().get("id");

        template.update(con -> {
            PreparedStatement likePs = con.prepareStatement(
                    "INSERT INTO photo_like (photo_id, like_id) VALUES (?, ?)"
            );
            likePs.setObject(1, photoId);
            likePs.setObject(2, likeId);
            return likePs;
        });
    }

    @Override
    public void removeLike(@NotNull UUID photoId, @NotNull UUID likeId) {
        template.update("DELETE FROM like WHERE id = ?", likeId);
        template.update("DELETE FROM photo_like WHERE like_id = ? AND photo_id = ?", likeId, photoId);
    }
}
