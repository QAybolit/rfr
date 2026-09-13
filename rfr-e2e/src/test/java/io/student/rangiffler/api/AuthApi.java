package io.student.rangiffler.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthApi {

    @GET("/register")
    Call<ResponseBody> requestRegisterForm();

    @FormUrlEncoded
    @POST("/register")
    Call<ResponseBody> registerUser(
            @Field("_csrf") String csrfToken,
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit,
            @Header("Cookie") String cookies
    );

    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> loginUser(
            @Field("_csrf") String csrfToken,
            @Field("username") String username,
            @Field("password") String password,
            @Header("Cookie") String cookies
    );
}
