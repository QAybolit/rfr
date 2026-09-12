package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.LikeEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.jpa.EntityManagers;
import io.student.rangiffler.data.repository.PhotoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PhotoRepositoryHibernate implements PhotoRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.apiJdbcUrl());

    @Override
    public @Nonnull PhotoEntity create(@Nonnull PhotoEntity photo) {
        entityManager.joinTransaction();
        entityManager.persist(photo);
        return photo;
    }

    @Override
    public @Nonnull PhotoEntity update(@Nonnull PhotoEntity photo) {
        entityManager.joinTransaction();
        return entityManager.merge(photo);
    }

    @Override
    public @Nonnull Optional<PhotoEntity> findById(@Nonnull UUID id) {
        return Optional.ofNullable(entityManager.find(PhotoEntity.class, id));
    }

    @Override
    public @Nonnull Optional<PhotoEntity> findByUsernameAndDescription(@Nonnull String username, @Nonnull String description) {
        try {
            return Optional.of(entityManager.createQuery("select p from PhotoEntity p join p.user u where u.username = :username and p.description = :description",
                            PhotoEntity.class)
                    .setParameter("username", username)
                    .setParameter("description", description)
                    .getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull Optional<PhotoEntity> findByUsernameAndCountry(@Nonnull String username, @Nonnull String code) {
        try {
            return Optional.of(entityManager.createQuery("select p from PhotoEntity p join p.user u join p.country c where u.username = :username and c.code = :code",
                            PhotoEntity.class)
                    .setParameter("username", username)
                    .setParameter("code", code)
                    .getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<PhotoEntity> findAllUserPhotos(@Nonnull String username) {
        return entityManager.createQuery("select p from PhotoEntity p join p.user u where u.username = :username", PhotoEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public void remove(@Nonnull PhotoEntity photo) {
        entityManager.joinTransaction();
        entityManager.remove(photo);
    }

    @Override
    public void addLike(@Nonnull UUID photoId, @Nonnull UUID userId) {
        entityManager.joinTransaction();
        PhotoEntity photo = entityManager.find(PhotoEntity.class, photoId);
        UserEntity user = entityManager.find(UserEntity.class, userId);
        LikeEntity like = new LikeEntity();
        like.setUser(user);
        like.setCreatedDate(new java.sql.Date(new Date().getTime()));
        photo.getLikes().add(like);
        entityManager.merge(photo);
    }

    @Override
    public void removeLike(@Nonnull UUID photoId, @Nonnull UUID likeId) {
        entityManager.joinTransaction();
        PhotoEntity photo = entityManager.find(PhotoEntity.class, photoId);
        LikeEntity like = entityManager.find(LikeEntity.class, likeId);
        photo.getLikes().remove(like);
        entityManager.merge(photo);
    }
}
