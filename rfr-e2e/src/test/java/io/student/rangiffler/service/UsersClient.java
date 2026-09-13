package io.student.rangiffler.service;

import io.student.rangiffler.model.types.User;

import java.util.List;

public interface UsersClient {

    User createUser(String username, String password);

    List<User> createIncomeInvitation(User targetUser, int count);

    List<User> createOutcomeInvitation(User targetUser, int count);

    List<User> createFriends(User targetUser, int count);
}
