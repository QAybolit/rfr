package io.student.rangiffler.data.extractor;

import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthUserEntityListExtractor implements ResultSetExtractor<List<AuthUserEntity>> {

    public static final AuthUserEntityListExtractor INSTANCE = new AuthUserEntityListExtractor();

    private AuthUserEntityListExtractor() {
    }

    @Override
    public List<AuthUserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<AuthUserEntity> users = new ArrayList<>();
        while (rs.next()) {
            try {
                AuthUserEntity authUser = new AuthUserEntity();
                authUser.setId(rs.getObject("user_id", UUID.class));
                authUser.setUsername(rs.getString("username"));
                authUser.setEnabled(rs.getBoolean("enabled"));
                authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

                users.add(authUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }
}
