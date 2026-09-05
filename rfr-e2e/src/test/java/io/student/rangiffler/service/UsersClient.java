package io.student.rangiffler.service;

import io.student.rangiffler.model.UserJson;

import java.util.List;

public interface UsersClient {

    UserJson createUser(String username, String password);

    List<UserJson> createIncomeInvitation(UserJson targetUser, int count);

    List<UserJson> createOutcomeInvitation(UserJson targetUser, int count);

    List<UserJson> createFriends(UserJson targetUser, int count);
}
