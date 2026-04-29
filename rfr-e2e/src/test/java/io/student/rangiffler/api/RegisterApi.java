package io.student.rangiffler.api;

import io.student.rangiffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApi {

    @POST("/register")
    Call<UserJson> registerUser(@Body UserJson userJson);
}
