package io.student.rangiffler.model;

import java.util.ArrayList;
import java.util.List;

public record TestData(String password,
                       List<UserJson> friendshipRequests,
                       List<UserJson> friendshipAddressees,
                       List<UserJson> friends) {

    public TestData(String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TestData(String password, List<UserJson> friendshipRequests, List<UserJson> friendshipAddressees, List<UserJson> friends) {
        this.password = password;
        this.friendshipRequests = friendshipRequests;
        this.friendshipAddressees = friendshipAddressees;
        this.friends = friends;
    }

    public String[] friendsUsernames() {
        return extractUsernames(friends);
    }

    public String[] friendshipRequestsUsernames() {
        return extractUsernames(friendshipRequests);
    }

    public String[] friendshipAddresseesUsernames() {
        return extractUsernames(friendshipAddressees);
    }

    private String[] extractUsernames(List<UserJson> users) {
        return users.stream().map(UserJson::username).toArray(String[]::new);
    }
}
