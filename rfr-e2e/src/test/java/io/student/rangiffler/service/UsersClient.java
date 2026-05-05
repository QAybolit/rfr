package io.student.rangiffler.service;

import io.student.rangiffler.model.UserJson;

public interface UsersClient {

    UserJson registerUser(UserJson user);
}
