package io.student.rangiffler.data.extractor;

import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserEntityListExtractor implements ResultSetExtractor<List<UserEntity>> {

    public static final UserEntityListExtractor INSTANCE = new UserEntityListExtractor();

    private UserEntityListExtractor() {
    }

    @Override
    public List<UserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<UserEntity> users = new ArrayList<>();
        while (rs.next()) {
            try {
                UserEntity newUser = new UserEntity();
                newUser.setId(rs.getObject("id", UUID.class));
                newUser.setUsername(rs.getString("username"));
                newUser.setFirstname(rs.getString("firstname"));
                newUser.setLastName(rs.getString("last_name"));
                newUser.setAvatar(rs.getObject("avatar", byte[].class));

                users.add(newUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }
}
