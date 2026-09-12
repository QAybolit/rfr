package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.FriendshipStatus;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.jpa.EntityManagers;
import io.student.rangiffler.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.apiJdbcUrl());

    @Override
    public @Nonnull UserEntity createUser(@Nonnull UserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public @Nonnull UserEntity updateUser(@Nonnull UserEntity user) {
        entityManager.joinTransaction();
        return entityManager.merge(user);
    }

    @Override
    public @Nonnull Optional<UserEntity> findById(@Nonnull UUID id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public @Nonnull Optional<UserEntity> findByUsername(@Nonnull String username) {
        try {
            return Optional.of(entityManager.createQuery("select u from UserEntity u where u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<UserEntity> findAll() {
        return entityManager.createQuery("select u from UserEntity u", UserEntity.class)
                .getResultList();
    }

    @Override
    public void addFriendshipRequest(@Nonnull UserEntity requester, @Nonnull UserEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.PENDING, addressee);
    }

    @Override
    public void addFriend(@Nonnull UserEntity requester, @Nonnull UserEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }
}
