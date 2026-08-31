package io.student.rangiffler.data.mapper;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PhotoEntityRowMapper implements RowMapper<PhotoEntity> {

    public static final PhotoEntityRowMapper INSTANCE = new PhotoEntityRowMapper();

    private PhotoEntityRowMapper() {
    }

    @Override
    public PhotoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoEntity result = new PhotoEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setDescription(rs.getString("description"));
        result.setPhoto(rs.getBytes("photo"));
        result.setCreatedDate(rs.getDate("created_date"));

        UserEntity user = new UserEntity();
        user.setId(rs.getObject("user_id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastName(rs.getString("last_name"));
        user.setAvatar(rs.getBytes("avatar"));

        CountryEntity country = new CountryEntity();
        country.setId(rs.getObject("country_id", UUID.class));
        country.setName(rs.getString("name"));
        country.setCode(rs.getString("code"));
        country.setFlag(rs.getBytes("flag"));

        user.setCountry(country);
        result.setCountry(country);
        result.setUser(user);

        return result;
    }
}
