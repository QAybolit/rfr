package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.jupiter.annotation.Photo;
import io.student.rangiffler.model.CountryJson;
import io.student.rangiffler.model.PhotoJson;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.service.CountryClient;
import io.student.rangiffler.service.CountryDbClient;
import io.student.rangiffler.service.PhotoClient;
import io.student.rangiffler.service.PhotoDbClient;
import io.student.rangiffler.service.UsersClient;
import io.student.rangiffler.service.UsersDbClient;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;

public class PhotoExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PhotoExtension.class);
    private final PhotoClient photoClient = new PhotoDbClient();
    private final UsersClient usersClient = new UsersDbClient();
    private final CountryClient countryClient = new CountryDbClient();
    private static final String DEFAULT_PASSWORD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Photo.class
        ).ifPresent(photoAnno -> {
                    final String username = randomUsername();
                    UserJson createdUser = usersClient.createUser(username, DEFAULT_PASSWORD);
                    Optional<CountryJson> countryJson;
                    if (photoAnno.countryCode() == null || photoAnno.countryCode().isEmpty()) {
                        countryJson = countryClient.findCountryByCode("es");
                    } else {
                        countryJson = countryClient.findCountryByCode(photoAnno.countryCode());
                    }

                    PhotoJson photo = new PhotoJson(
                            null,
                            createdUser.id(),
                            countryJson.get().id(),
                            photoAnno.description(),
                            photoAnno.src() != null ? photoAnno.src() : "",
                            null
                    );
                    PhotoJson createdPhoto = photoClient.createPhoto(photo);

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdPhoto);
                }
        );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return PhotoJson.class.isAssignableFrom(parameterContext.getParameter().getType())
                && AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), Photo.class);
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), PhotoJson.class);
    }
}
