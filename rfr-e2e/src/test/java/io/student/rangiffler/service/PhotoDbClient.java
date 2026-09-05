package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.PhotoJson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PhotoDbClient implements PhotoClient {

    private static final Config CFG = Config.getInstance();

    private final PhotoRepository photoRepository = PhotoRepository.getInstance();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.apiJdbcUrl()
    );

    @Override
    public PhotoJson createPhoto(PhotoJson photo) {
        return xaTxTemplate.execute(() -> {
            PhotoEntity photoEntity = getPhotoEntity(photo);
            photoRepository.create(photoEntity);
            return PhotoJson.fromEntity(photoEntity);
        });
    }

    @Nonnull
    private PhotoEntity getPhotoEntity(PhotoJson json) {
        PhotoEntity photo = new PhotoEntity();
        photo.setDescription(json.description());
        photo.setPhoto(json.photo() != null ? json.photo().getBytes(StandardCharsets.UTF_8) : loadPhoto());

        UserEntity user = new UserEntity();
        user.setId(json.userId());
        photo.setUser(user);

        CountryEntity country = new CountryEntity();
        country.setId(json.countryId());
        photo.setCountry(country);

        return photo;
    }

    private byte[] loadPhoto() {
        try {
            Resource resource = new ClassPathResource("images/default.jpg");
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            // NOP
            return null;
        }
    }
}
