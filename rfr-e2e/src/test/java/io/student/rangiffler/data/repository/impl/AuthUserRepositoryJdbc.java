package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import io.student.rangiffler.data.entity.auth.Authority;
import io.student.rangiffler.data.entity.auth.AuthorityEntity;
import io.student.rangiffler.data.mapper.AuthUserEntityRowMapper;
import io.student.rangiffler.data.repository.AuthUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rangiffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(@NotNull AuthUserEntity user) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        " VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO authority (user_id, authority)" +
                             " VALUES (?, ?)")
        ) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, passwordEncoder.encode(user.getPassword()));
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            user.setId(generatedKey);

            for (AuthorityEntity authority : user.getAuthorities()) {
                authorityPs.setObject(1, generatedKey);
                authorityPs.setString(2, authority.getAuthority().name());
                authorityPs.addBatch();
                authorityPs.clearParameters();
            }
            authorityPs.executeBatch();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(@NotNull UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN authority a on u.id = a.user_id WHERE u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorities = new ArrayList<>();

                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.INSTANCE.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = authorityEntity(rs);
                    ae.setUser(user);
                    authorities.add(ae);
                    AuthUserEntity ue = authUserEntity(rs);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity ue = authUserEntity(rs);
                    users.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(@NotNull String username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN authority a on u.id = a.user_id WHERE u.username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorities = new ArrayList<>();

                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.INSTANCE.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = authorityEntity(rs);
                    ae.setUser(user);
                    authorities.add(ae);

                    AuthUserEntity ue = authUserEntity(rs);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthUserEntity authUserEntity(ResultSet rs) throws SQLException {
        AuthUserEntity ue = new AuthUserEntity();
        ue.setId(rs.getObject("id", UUID.class));
        ue.setUsername(rs.getString("username"));
        ue.setEnabled(rs.getBoolean("enabled"));
        ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return ue;
    }

    private AuthorityEntity authorityEntity(ResultSet rs) throws SQLException {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setId(rs.getObject("a.id", UUID.class));
        ae.setAuthority(Authority.valueOf(rs.getString("authority")));
        return ae;
    }
}
