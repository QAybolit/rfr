package io.student.rangiffler.service.mapper;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.model.types.Country;
import io.student.rangiffler.model.types.CountryInput;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;

@ParametersAreNonnullByDefault
public class CountryMapper {

    public static @Nonnull Country countryFromEntity(CountryEntity entity) {
        return Country.newBuilder()
                .flag(entity.getFlag() != null && entity.getFlag().length > 0 ? new String(entity.getFlag(), StandardCharsets.UTF_8) : null)
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }

    public static @Nonnull CountryEntity entityFromCountry(CountryInput country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setCode(country.getCode());
        return countryEntity;
    }
}
