package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import io.student.rangiffler.data.jpa.EntityManagers;
import io.student.rangiffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

    @Override
    public @Nonnull AuthUserEntity create(@Nonnull AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findById(@Nonnull UUID id) {
        return Optional.ofNullable(
                entityManager.find(AuthUserEntity.class, id)
        );
    }

    @Override
    public @Nonnull List<AuthUserEntity> findAll() {
        return entityManager.createQuery("select u from AuthUserEntity u", AuthUserEntity.class)
                .getResultList();
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findByUsername(@Nonnull String username) {
        try {
            return Optional.of(entityManager.createQuery("select u from AuthUserEntity u where u.username = :username", AuthUserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
