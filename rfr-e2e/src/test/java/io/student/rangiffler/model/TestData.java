package io.student.rangiffler.model;

import io.student.rangiffler.model.types.User;

import java.util.ArrayList;
import java.util.List;

public record TestData(String password,
                       List<User> otherPeople) {

    public TestData(String password) {
        this(password, new ArrayList<>());
    }

    public TestData(String password, List<User> otherPeople) {
        this.password = password;
        this.otherPeople = otherPeople;
    }

    public String[] otherPeopleNames() {
        return otherPeople.stream().map(User::getUsername).toArray(String[]::new);
    }

}
