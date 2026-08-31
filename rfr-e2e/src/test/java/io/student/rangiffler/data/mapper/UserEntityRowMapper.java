package io.student.rangiffler.data.mapper;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

    public static final UserEntityRowMapper INSTANCE = new UserEntityRowMapper();

    private UserEntityRowMapper() {
    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity result = new UserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setFirstname(rs.getString("firstname"));
        result.setLastName(rs.getString("last_name"));
        result.setAvatar(rs.getBytes("avatar"));

        CountryEntity country = new CountryEntity();
        country.setId(rs.getObject("country_id", UUID.class));
        country.setName(rs.getString("name"));
        country.setCode(rs.getString("code"));
        country.setFlag(rs.getBytes("flag"));

        result.setCountry(country);
        return result;
    }
}

