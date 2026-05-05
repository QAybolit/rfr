package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.model.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

    private static final Config CONFIG = Config.getInstance();

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(
            new SingleConnectionDataSource(
                    CONFIG.registerJdbcUrl(),
                    CONFIG.dbUsername(),
                    CONFIG.dbPassword(),
                    true
            )
    );

    @Override
    public UserJson registerUser(UserJson user) {
        final UUID uuid = UUID.randomUUID();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            """
                                    INSERT INTO `user` (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES (UUID_TO_BIN(?, true),?, ?, ?, ?, ?, ?)
                                    """,
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, uuid.toString());
                    ps.setString(2, user.username());
                    ps.setString(3, passwordEncoder.encode(user.password()));
                    ps.setBoolean(4, user.enabled());
                    ps.setBoolean(5, user.accountNonExpired());
                    ps.setBoolean(6, user.accountNonLocked());
                    ps.setBoolean(7, user.credentialsNonExpired());
                    return ps;
                }
        );

        return new UserJson(
                uuid,
                user.username(),
                user.password(),
                user.enabled(),
                user.accountNonExpired(),
                user.accountNonLocked(),
                user.credentialsNonExpired()
        );
    }
}
