package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.impl.UserRepositoryHibernate;
import io.student.rangiffler.data.repository.impl.UserRepositoryJdbc;
import io.student.rangiffler.data.repository.impl.UserRepositorySpringJdbc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserRepository {

    @Nonnull
    static UserRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jdbc" -> new UserRepositoryJdbc();
            case "spring-jdbc" -> new UserRepositorySpringJdbc();
            default -> new UserRepositoryHibernate();
        };
    }

    UserEntity createUser(UserEntity user);

    UserEntity updateUser(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAll();

    void addFriendshipRequest(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);
}
