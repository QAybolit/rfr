package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.user.CountryEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface CountryRepository {

    Optional<CountryEntity> findByCode(String countryCode);
}
