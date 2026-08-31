package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import io.student.rangiffler.data.entity.auth.Authority;
import io.student.rangiffler.data.entity.auth.AuthorityEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.AuthUserRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.apiJdbcUrl()
    );

    @Override
    public UserJson createUser(String username, String password) {
        return xaTxTemplate.execute(() -> {
            AuthUserEntity authUser = getAuthUserEntity(username, password);
            authUserRepository.create(authUser);
            return UserJson.fromEntity(
                    userRepository.createUser(userEntity(username))
            );
        });
    }

    private AuthUserEntity getAuthUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(passwordEncoder.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values())
                        .map(e -> {
                                    AuthorityEntity ae = new AuthorityEntity();
                                    ae.setUser(authUser);
                                    ae.setAuthority(e);
                                    return ae;
                                }
                        ).toList()
        );
        return authUser;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        return ue;
    }
}
