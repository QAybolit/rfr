package io.student.rangiffler.data.mapper;

import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserEntityRowMapper implements RowMapper<AuthUserEntity> {

    public static final AuthUserEntityRowMapper INSTANCE = new AuthUserEntityRowMapper();

    private AuthUserEntityRowMapper() {
    }

    @Override
    public @Nonnull AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUserEntity result = new AuthUserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setPassword(rs.getString("password"));
        result.setEnabled(rs.getBoolean("enabled"));
        result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return null;
    }
}
