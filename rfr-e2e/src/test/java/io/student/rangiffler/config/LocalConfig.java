package io.student.rangiffler.config;

public enum LocalConfig implements Config {

    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://localhost:3001";
    }

    @Override
    public String spendUrl() {
        return "http://localhost:9001";
    }

    @Override
    public String spendJdbcUrl() {
        return "jdbc:mysql://localhost:3306/rangiffler-auth";
    }

    @Override
    public String dbUsername() {
        return "root";
    }

    @Override
    public String dbPassword() {
        return "secret";
    }
}
