package io.student.rangiffler.service.impl;

import io.qameta.allure.Step;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.data.repository.impl.CountryRepositoryHibernate;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.types.Photo;
import io.student.rangiffler.model.types.PhotoInput;
import io.student.rangiffler.service.PhotoClient;

import javax.annotation.Nonnull;
import java.util.UUID;

import static io.student.rangiffler.service.mapper.PhotoMapper.entityFromPhoto;
import static io.student.rangiffler.service.mapper.PhotoMapper.photoFromEntity;

public class PhotoDbClient implements PhotoClient {

    private static final Config CFG = Config.getInstance();

    private final PhotoRepository photoRepository = PhotoRepository.getInstance();
    private final CountryRepository countryRepository = new CountryRepositoryHibernate();
    private final UserRepository userRepository = UserRepository.getInstance();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.apiJdbcUrl()
    );

    @Step("Create new photo in DB")
    @Override
    public @Nonnull Photo createPhoto(@Nonnull PhotoInput photoInput, @Nonnull UUID userId) {
        return xaTxTemplate.execute(() -> {
            PhotoEntity photoEntity = entityFromPhoto(photoInput);

            CountryEntity country = countryRepository
                    .findByCode(photoInput.getCountry() != null ? photoInput.getCountry().getCode() : "ru")
                    .orElseThrow(() -> new IllegalArgumentException("Не найдена страна по коду " + (photoInput.getCountry() != null ? photoInput.getCountry().getCode() : "ru")));

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Не найден пользователь с ID " + userId));

            photoEntity.setCountry(country);
            photoEntity.setUser(user);

            photoRepository.create(photoEntity);
            return photoFromEntity(photoEntity);
        });
    }
}
