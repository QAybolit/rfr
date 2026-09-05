package io.student.rangiffler.service;

import io.student.rangiffler.api.RegisterApi;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.model.UserJson;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class UsersApiClient implements UsersClient {

    private static final Config CONFIG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONFIG.registerUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final RegisterApi registerApi = retrofit.create(RegisterApi.class);

    @Override
    public UserJson createUser(String username, String password) {
//        try {
//            Response<UserJson> response = registerApi.registerUser(user).execute();
//            if (response.code() != 201) throw new RuntimeException("Error registering user");
//
//            return response.body();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    @Override
    public List<UserJson> createIncomeInvitation(UserJson targetUser, int count) {
        return List.of();
    }

    @Override
    public List<UserJson> createOutcomeInvitation(UserJson targetUser, int count) {
        return List.of();
    }

    @Override
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        return List.of();
    }

}
