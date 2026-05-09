package io.student.rangiffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String registerUrl();

    String registerJdbcUrl();

    String dbUsername();

    String dbPassword();
}
