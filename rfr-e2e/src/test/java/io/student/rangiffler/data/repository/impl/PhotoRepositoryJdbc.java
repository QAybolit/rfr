package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.PhotoRepository;
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

public class PhotoRepositoryJdbc implements PhotoRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public PhotoEntity create(@NotNull PhotoEntity photo) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "INSERT INTO photo (user_id, country_id, description, photo, created_date)" +
                        " VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, photo.getUser().getId());
            ps.setObject(2, photo.getCountry().getId());
            ps.setString(3, photo.getDescription());
            ps.setObject(4, photo.getPhoto());
            ps.setObject(5, new java.sql.Date(new Date().getTime()));

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            photo.setId(generatedKey);
            return photo;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PhotoEntity update(@NotNull PhotoEntity photo) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "UPDATE photo SET user_id = ?, country_id = ?, description = ?, photo = ?" +
                        " WHERE id = ?"
        )) {
            ps.setObject(1, photo.getUser().getId());
            ps.setObject(2, photo.getCountry().getId());
            ps.setString(3, photo.getDescription());
            ps.setObject(4, photo.getPhoto());
            ps.setObject(5, photo.getId());

            ps.executeUpdate();

            return photo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PhotoEntity> findById(@NotNull UUID id) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM photo p WHERE p.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    PhotoEntity photo = photoEntity(rs);

                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("user_id", UUID.class));
                    photo.setUser(user);

                    CountryEntity country = new CountryEntity();
                    country.setId(rs.getObject("country_id", UUID.class));
                    photo.setCountry(country);


                    return Optional.of(photo);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PhotoEntity> findByUsernameAndDescription(@NotNull String username, @NotNull String description) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id WHERE u.username = ? AND p.description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    PhotoEntity photo = photoEntity(rs);
                    UserEntity user = userEntity(rs);
                    photo.setUser(user);

                    CountryEntity country = new CountryEntity();
                    country.setId(rs.getObject("p.country_id", UUID.class));
                    photo.setCountry(country);

                    return Optional.of(photo);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PhotoEntity> findByUsernameAndCountry(@NotNull String username, @NotNull String code) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id JOIN country c ON p.country_id = c.id" +
                        " WHERE u.username = ? AND c.code = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, code);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    PhotoEntity photo = photoEntity(rs);
                    UserEntity user = userEntity(rs);
                    photo.setUser(user);

                    CountryEntity country = new CountryEntity();
                    country.setId(rs.getObject("c.id", UUID.class));
                    country.setName(rs.getString("c.name"));
                    country.setCode(rs.getString("c.code"));
                    country.setFlag(rs.getBytes("c.flag"));

                    photo.setCountry(country);

                    return Optional.of(photo);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PhotoEntity> findAllUserPhotos(@NotNull String username) {
        List<PhotoEntity> photos = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM photo p JOIN \"user'\" u ON p.user_id = u.id WHERE u.username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    PhotoEntity photo = photoEntity(rs);

                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("p.user_id", UUID.class));

                    photo.setUser(user);

                    CountryEntity country = new CountryEntity();
                    country.setId(rs.getObject("p.country_id", UUID.class));
                    photo.setCountry(country);

                    photos.add(photo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return photos;
    }

    @Override
    public void remove(@NotNull PhotoEntity photo) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
                "DELETE FROM photo WHERE id = ?"
        )) {
            ps.setObject(1, photo.getId());
            int result = ps.executeUpdate();

            if (result <= 0) {
                throw new SQLException("Can't find spend id in table 'spend'");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PhotoEntity photoEntity(ResultSet rs) throws SQLException {
        PhotoEntity photo = new PhotoEntity();
        photo.setId(rs.getObject("p.id", UUID.class));
        photo.setDescription(rs.getString("p.description"));
        photo.setPhoto(rs.getObject("p.photo", byte[].class));
        photo.setCreatedDate(rs.getObject("p.created_date", java.sql.Date.class));
        return photo;
    }

    private UserEntity userEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("u.id", UUID.class));
        user.setUsername(rs.getString("u.username"));
        user.setFirstname(rs.getString("u.firstname"));
        user.setLastName(rs.getString("u.last_name"));
        user.setAvatar(rs.getBytes("u.avatar"));

        CountryEntity cn = new CountryEntity();
        cn.setId(rs.getObject("u.country_id", UUID.class));

        user.setCountry(cn);
        return user;
    }
}
