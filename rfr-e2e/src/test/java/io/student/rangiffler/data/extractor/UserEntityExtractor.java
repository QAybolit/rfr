package io.student.rangiffler.data.extractor;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserEntityExtractor implements ResultSetExtractor<UserEntity> {

    public static final UserEntityExtractor INSTANCE = new UserEntityExtractor();

    private UserEntityExtractor() {
    }
    
    @Override
    public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, UserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            UserEntity user = userMap.computeIfAbsent(userId, id -> {
                try {
                    UserEntity newUser = new UserEntity();
                    newUser.setId(rs.getObject("id", UUID.class));
                    newUser.setUsername(rs.getString("username"));
                    newUser.setFirstname(rs.getString("firstname"));
                    newUser.setLastName(rs.getString("last_name"));
                    newUser.setAvatar(rs.getObject("avatar", byte[].class));

                    return newUser;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            CountryEntity country = new CountryEntity();
            country.setId(rs.getObject("country_id", UUID.class));
            country.setName(rs.getString("name"));
            country.setCode(rs.getString("code"));
            country.setFlag(rs.getObject("flag", byte[].class));
            
            user.setCountry(country);
        }
        return userMap.get(userId);
    }
}
