package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import io.student.rangiffler.data.extractor.AuthUserEntityExtractor;
import io.student.rangiffler.data.extractor.AuthUserEntityListExtractor;
import io.student.rangiffler.data.repository.AuthUserRepository;
import io.student.rangiffler.data.tpl.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(@NotNull AuthUserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            " VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, passwordEncoder.encode(user.getPassword()));
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);

        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        AuthUserEntityExtractor.INSTANCE,
                        id
                )
        );
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> users = template.query(
                "SELECT * FROM \"user\"",
                AuthUserEntityListExtractor.INSTANCE
        );
        return users;
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(@NotNull String username) {
        return Optional.ofNullable(
                template.query(
                        "SELECT * FROM \"user\" WHERE username = ?",
                        AuthUserEntityExtractor.INSTANCE,
                        username
                )
        );
    }
}
