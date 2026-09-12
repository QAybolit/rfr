package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.model.types.CountryInput;
import io.student.rangiffler.model.types.Photo;
import io.student.rangiffler.model.types.PhotoInput;
import io.student.rangiffler.model.types.User;
import io.student.rangiffler.service.PhotoClient;
import io.student.rangiffler.service.UsersClient;
import io.student.rangiffler.service.impl.PhotoDbClient;
import io.student.rangiffler.service.impl.UsersDbClient;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.UUID;

import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;

public class PhotoExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PhotoExtension.class);
    private final PhotoClient photoClient = new PhotoDbClient();
    private final UsersClient usersClient = new UsersDbClient();
    private static final String DEFAULT_PASSWORD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                io.student.rangiffler.jupiter.annotation.Photo.class
        ).ifPresent(photoAnno -> {
                    final String username = randomUsername();
                    User createdUser = usersClient.createUser(username, DEFAULT_PASSWORD);

                    PhotoInput photo = PhotoInput.newBuilder()
                            .src(photoAnno.src() != null && !photoAnno.src().isEmpty() ? photoAnno.src() : "")
                            .country(
                                    CountryInput.newBuilder()
                                            .code(photoAnno.countryCode() == null || photoAnno.countryCode().isEmpty() ? "ru" : photoAnno.countryCode())
                                            .build()
                            )
                            .description(photoAnno.description())
                            .like(null)
                            .build();
                    Photo createdPhoto = photoClient.createPhoto(photo, UUID.fromString(createdUser.getId()));

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdPhoto);
                }
        );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Photo.class.isAssignableFrom(parameterContext.getParameter().getType())
                && AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), io.student.rangiffler.jupiter.annotation.Photo.class);
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Photo.class);
    }
}
