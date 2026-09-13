package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.model.TestData;
import io.student.rangiffler.model.TestUser;
import io.student.rangiffler.model.types.User;
import io.student.rangiffler.service.UsersClient;
import io.student.rangiffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.student.rangiffler.jupiter.extension.TestMethodContextExtension.context;
import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UsersClient userClient = new UsersDbClient();
    private static final String DEFAULT_PASSWORD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                io.student.rangiffler.jupiter.annotation.User.class
        ).ifPresent(userAnno -> {
                    final String username = "".equals(userAnno.username()) ? randomUsername() : userAnno.username();
                    User createdUser = userClient.createUser(username, DEFAULT_PASSWORD);
                    List<User> incomeInvitations = userClient.createIncomeInvitation(createdUser, userAnno.incomeInvitations());
                    List<User> outcomeInvitations = userClient.createOutcomeInvitation(createdUser, userAnno.outcomeInvitations());
                    List<User> friends = userClient.createFriends(createdUser, userAnno.friends());

                    createdUser.setIncomeInvitations(
                            userAnno.incomeInvitations() > 0
                                    ? pageUserFromList(incomeInvitations, userAnno.incomeInvitations())
                                    : null
                    );
                    createdUser.setOutcomeInvitations(
                            userAnno.outcomeInvitations() > 0
                                    ? pageUserFromList(outcomeInvitations, userAnno.outcomeInvitations())
                                    : null
                    );
                    createdUser.setFriends(
                            userAnno.friends() > 0
                                    ? pageUserFromList(friends, userAnno.friends())
                                    : null
                    );

                    List<User> otherPeople = new ArrayList<>();
                    for (int i = 0; i < userAnno.otherPeoples(); i++) {
                        otherPeople.add(userClient.createUser(randomUsername(), DEFAULT_PASSWORD));
                    }
                    TestData testData = new TestData(DEFAULT_PASSWORD, otherPeople);

                    TestUser testUser = new TestUser(
                            createdUser,
                            testData
                    );

                    context.getStore(NAMESPACE).put(context.getUniqueId(), testUser);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return TestUser.class.isAssignableFrom(parameterContext.getParameter().getType())
                && AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), io.student.rangiffler.jupiter.annotation.User.class);
    }

    @Override
    public TestUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), TestUser.class);
    }

    public static Optional<TestUser> createdUser() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), TestUser.class));
    }

    private Page<User> pageUserFromList(List<User> users, int count) {
        Pageable pageable = PageRequest.of(1, count);
        return new PageImpl<>(users, pageable, count);
    }
}
