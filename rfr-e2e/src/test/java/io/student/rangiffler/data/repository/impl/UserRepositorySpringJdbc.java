package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.FriendshipStatus;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.extractor.UserEntityExtractor;
import io.student.rangiffler.data.extractor.UserEntityListExtractor;
import io.student.rangiffler.data.repository.UserRepository;
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

public class UserRepositorySpringJdbc implements UserRepository {

    private final JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(@NotNull UserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, firstname, last_name, avatar, country_id)" +
                            " VALUES (?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastName());
            ps.setObject(4, user.getAvatar());
            ps.setObject(5, user.getCountry().getId());
            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);

        return user;
    }

    @Override
    public UserEntity updateUser(@NotNull UserEntity user) {
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"user\" SET username = ?, firstname = ?, last_name = ?, avatar = ?, country_id = ?" +
                            " WHERE id = ?"
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastName());
            ps.setObject(4, user.getAvatar());
            ps.setObject(5, user.getCountry().getId());
            ps.setObject(6, user.getId());
            return ps;
        });

        return user;
    }

    @Override
    public Optional<UserEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM \"user\" u JOIN country c ON u.country_id = c.id WHERE u.id = ?",
                        UserEntityExtractor.INSTANCE,
                        id
                )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(@NotNull String username) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM \"user\" u JOIN country c ON u.country_id = c.id WHERE u.username = ?",
                        UserEntityExtractor.INSTANCE,
                        username
                )
        );
    }

    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> users = template.query(
                "SELECT * FROM \"user\"",
                UserEntityListExtractor.INSTANCE
        );
        return users;
    }

    @Override
    public void addFriendshipRequest(@NotNull UserEntity requester, @NotNull UserEntity addressee) {
        template.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)"
                    );
                    ps.setObject(1, requester.getId());
                    ps.setObject(2, addressee.getId());
                    ps.setString(3, FriendshipStatus.PENDING.name());
                    ps.setObject(4, new java.sql.Date(new Date().getTime()));
                    return ps;
                }
        );
    }

    @Override
    public void addFriend(@NotNull UserEntity requester, @NotNull UserEntity addressee) {
        List<UserEntity> users = template.query(
                "SELECT * FROM \"user\" WHERE requester_id = ? AND addressee_id = ? OR requester_id = ? AND addressee_id = ?",
                UserEntityListExtractor.INSTANCE
        );

        if (users.size() != 2) {
            throw new RuntimeException("Both Income Invitation and Outcome Invitation must be present");
        }

        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE friendship SET status = ?" +
                            " WHERE requester_id = ? AND addressee_id = ? OR requester_id = ? AND addressee_id = ?"
            );
            ps.setObject(1, FriendshipStatus.ACCEPTED.name());
            ps.setObject(2, requester.getId());
            ps.setObject(3, addressee.getId());
            ps.setObject(4, addressee.getId());
            ps.setObject(5, requester.getId());

            return ps;
        });
    }
}
