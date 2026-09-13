package io.student.rangiffler.service.impl;

import io.qameta.allure.Step;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.auth.AuthUserEntity;
import io.student.rangiffler.data.entity.auth.Authority;
import io.student.rangiffler.data.entity.auth.AuthorityEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.AuthUserRepository;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.data.repository.impl.CountryRepositoryHibernate;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.types.FriendStatus;
import io.student.rangiffler.model.types.User;
import io.student.rangiffler.service.UsersClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.student.rangiffler.service.mapper.UserMapper.userFromEntity;
import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;


public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();
    private final CountryRepository countryRepository = new CountryRepositoryHibernate();
    private final String DEFAULT_PASSWORD = "12345";

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.apiJdbcUrl()
    );

    @Step("Create new user with username {username} in DB")
    @Override
    public @Nonnull User createUser(@Nonnull String username, @Nonnull String password) {
        return xaTxTemplate.execute(() -> {
            AuthUserEntity authUser = getAuthUserEntity(username, password);
            authUserRepository.create(authUser);
            UserEntity user = userRepository.createUser(getUserEntity(username));
            return userFromEntity(user, null);
        });
    }

    @Step("Create new income invitations in DB")
    @Override
    public @Nonnull List<User> createIncomeInvitation(@Nonnull User targetUser, int count) {
        List<User> users = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(UUID.fromString(targetUser.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Не найден пользователь  с ID " + targetUser.getId()));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String requesterUsername = randomUsername();
                    AuthUserEntity authRequester = getAuthUserEntity(requesterUsername, DEFAULT_PASSWORD);
                    authUserRepository.create(authRequester);
                    UserEntity requester = userRepository.createUser(getUserEntity(requesterUsername));
                    userRepository.addFriendshipRequest(requester, targetEntity);
                    users.add(userFromEntity(requester, FriendStatus.INVITATION_SENT));
                });
            }
        }
        return users;
    }

    @Step("Create new outcome invitations in DB")
    @Override
    public @Nonnull List<User> createOutcomeInvitation(@Nonnull User targetUser, int count) {
        List<User> users = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(UUID.fromString(targetUser.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Не найден пользователь  с ID " + targetUser.getId()));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String addresseeUsername = randomUsername();
                    AuthUserEntity authAddressee = getAuthUserEntity(addresseeUsername, DEFAULT_PASSWORD);
                    authUserRepository.create(authAddressee);
                    UserEntity addressee = userRepository.createUser(getUserEntity(addresseeUsername));
                    userRepository.addFriendshipRequest(targetEntity, addressee);
                    users.add(userFromEntity(addressee, FriendStatus.INVITATION_RECEIVED));
                });
            }
        }
        return users;
    }

    @Step("Create new friends invitations in DB")
    @Override
    public @Nonnull List<User> createFriends(@Nonnull User targetUser, int count) {
        List<User> users = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(UUID.fromString(targetUser.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Не найден пользователь  с ID " + targetUser.getId()));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String friendUsername = randomUsername();
                    AuthUserEntity authFriend = getAuthUserEntity(friendUsername, DEFAULT_PASSWORD);
                    authUserRepository.create(authFriend);
                    UserEntity friend = userRepository.createUser(getUserEntity(friendUsername));
                    userRepository.addFriend(targetEntity, friend);
                    users.add(userFromEntity(friend, FriendStatus.FRIEND));
                });
            }
        }
        return users;
    }

    private @Nonnull AuthUserEntity getAuthUserEntity(@Nonnull String username, @Nonnull String password) {
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

    private @Nonnull UserEntity getUserEntity(@Nonnull String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCountry(
                countryRepository.findByCode("ru")
                        .orElseThrow(() -> new IllegalArgumentException("Страна не найдена по коду: ru"))
        );
        return ue;
    }
}
