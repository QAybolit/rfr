package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.FriendshipStatus;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rangiffler.data.tpl.Connections.holder;

public class UserRepositoryJdbc implements UserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(@NotNull UserEntity user) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, firstname, last_name, avatar, country_id)" +
                        " VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastName());
            ps.setObject(4, user.getAvatar());
            ps.setObject(5, user.getCountry().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateUser(@NotNull UserEntity user) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "UPDATE \"user\" SET username = ?, firstname = ?, last_name = ?, avatar = ?, country_id = ?" +
                        " WHERE id = ?"
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastName());
            ps.setObject(4, user.getAvatar());
            ps.setObject(5, user.getCountry().getId());
            ps.setObject(6, user.getId());

            ps.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(@NotNull UUID id) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN country c ON u.country_id = c.id WHERE u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity ue = userEntity(rs);
                    CountryEntity ce = countryEntity(rs);
                    ue.setCountry(ce);

                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(@NotNull String username) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN country c ON u.country_id = c.id WHERE u.username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity ue = userEntity(rs);
                    CountryEntity ce = countryEntity(rs);
                    ue.setCountry(ce);

                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    UserEntity ue = userEntity(rs);
                    users.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void addFriendshipRequest(@NotNull UserEntity requester, @NotNull UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, created_date, status)" +
                        " VALUES (?, ?, ?, ?)"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setObject(3, new java.sql.Date(new Date().getTime()));
            ps.setString(4, FriendshipStatus.PENDING.name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(@NotNull UserEntity requester, @NotNull UserEntity addressee) {
        try (PreparedStatement checkPs = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT COUNT(*) AS total FROM friendship" +
                        " WHERE requester_id = ? AND addressee_id = ? OR requester_id = ? AND addressee_id = ?");
             PreparedStatement friendPs = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                     "UPDATE friendship SET status = ?" +
                             "  WHERE requester_id = ? AND addressee_id = ? OR requester_id = ? AND addressee_id = ?"
             )) {
            checkPs.setObject(1, requester.getId());
            checkPs.setObject(2, addressee.getId());
            checkPs.setObject(3, addressee.getId());
            checkPs.setObject(4, requester.getId());
            checkPs.execute();

            try (ResultSet rs = checkPs.getResultSet()) {
                int count = rs.getInt("total");
                if (count != 2) {
                    throw new RuntimeException("Both Income Invitation and Outcome Invitation must be present");
                }
            }
            friendPs.setObject(1, FriendshipStatus.ACCEPTED.name());
            friendPs.setObject(2, requester.getId());
            friendPs.setObject(3, addressee.getId());
            friendPs.setObject(4, addressee.getId());
            friendPs.setObject(5, requester.getId());

            friendPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity userEntity(ResultSet rs) throws SQLException {
        UserEntity ue = new UserEntity();
        ue.setId(rs.getObject("id", UUID.class));
        ue.setUsername(rs.getString("username"));
        ue.setFirstname(rs.getString("firstname"));
        ue.setLastName(rs.getString("last_name"));
        ue.setAvatar(rs.getObject("avatar", byte[].class));
        return ue;
    }

    private CountryEntity countryEntity(ResultSet rs) throws SQLException {
        CountryEntity ce = new CountryEntity();
        ce.setId(rs.getObject("country_id", UUID.class));
        ce.setName(rs.getString("name"));
        ce.setCode(rs.getString("code"));
        ce.setFlag(rs.getObject("flag", byte[].class));
        return ce;
    }
}
