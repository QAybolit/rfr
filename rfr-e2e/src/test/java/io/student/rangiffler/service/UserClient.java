package io.student.rangiffler.service;

import io.student.rangiffler.model.UserJson;

public interface UserClient {

    UserJson registerUser(UserJson user);
}
