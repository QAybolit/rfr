package io.student.rangiffler.api;

import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient {

    private final AuthApi authApi;

    public UserApiClient() {
        super(CFG.registerUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void registerUser(String username, String password) throws IOException {
        Response<ResponseBody> response = authApi.requestRegisterForm().execute();

        String csrfToken = "";
        if (response.isSuccessful() && response.body() != null) {
            String html = response.body().string();
            csrfToken = extractCsrf(html);
        }

        Response<ResponseBody> registerResponse = authApi.registerUser(
                csrfToken,
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        ).execute();
        assert registerResponse.isSuccessful();

        Response<ResponseBody> loginResponse = authApi.loginUser(
                csrfToken,
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        ).execute();
        assert loginResponse.code() == 302;
    }

    private String extractCsrf(String html) {
        Document doc = Jsoup.parse(html);
        Element input = doc.selectFirst("input[name=_csrf]");
        return input != null ? input.attr("value") : null;
    }
}
