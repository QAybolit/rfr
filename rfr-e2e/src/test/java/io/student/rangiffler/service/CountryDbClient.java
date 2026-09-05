package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.impl.CountryRepositoryHibernate;
import io.student.rangiffler.data.tpl.XaTransactionTemplate;
import io.student.rangiffler.model.CountryJson;

import java.util.Optional;

public class CountryDbClient implements CountryClient{

    private static final Config CFG = Config.getInstance();
    private final CountryRepository countryRepository = new CountryRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.apiJdbcUrl()
    );

    @Override
    public Optional<CountryJson> findCountryByCode(String countryCode) {
        return xaTxTemplate.execute(() -> {
                    Optional<CountryEntity> countryEntity = countryRepository.findByCode(countryCode);
                    return countryEntity.map(CountryJson::fromEntity);
                }
        );
    }
}
