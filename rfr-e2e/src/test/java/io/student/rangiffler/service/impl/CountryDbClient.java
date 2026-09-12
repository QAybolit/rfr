package io.student.rangiffler.service.impl;

import io.qameta.allure.Step;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.impl.CountryRepositoryHibernate;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.types.Country;
import io.student.rangiffler.model.types.CountryInput;
import io.student.rangiffler.service.CountryClient;
import io.student.rangiffler.service.mapper.CountryMapper;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CountryDbClient implements CountryClient {

    private static final Config CFG = Config.getInstance();
    private final CountryRepository countryRepository = new CountryRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.apiJdbcUrl()
    );

    @Step("Find country in DB by code")
    @Override
    public @Nonnull Optional<Country> findCountryByCode(@Nonnull CountryInput countryInput) {
        return xaTxTemplate.execute(() -> {
                    Optional<CountryEntity> countryEntity = countryRepository.findByCode(countryInput.getCode());
                    return countryEntity.map(CountryMapper::countryFromEntity);
                }
        );
    }
}
