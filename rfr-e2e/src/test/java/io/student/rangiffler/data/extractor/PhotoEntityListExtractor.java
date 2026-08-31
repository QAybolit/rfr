package io.student.rangiffler.data.extractor;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoEntityListExtractor implements ResultSetExtractor<List<PhotoEntity>> {

    public static final PhotoEntityListExtractor INSTANCE = new PhotoEntityListExtractor();

    private PhotoEntityListExtractor() {
    }

    @Override
    public List<PhotoEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<PhotoEntity> photos = new ArrayList<>();
        while (rs.next()) {
            try {
                PhotoEntity newPhoto = new PhotoEntity();
                newPhoto.setId(rs.getObject("p.id", UUID.class));
                newPhoto.setDescription(rs.getString("p.description"));
                newPhoto.setPhoto(rs.getObject("p.photo", byte[].class));
                newPhoto.setCreatedDate(rs.getObject("p.created_date", java.sql.Date.class));

                UserEntity user = new UserEntity();
                user.setId(rs.getObject("p.user_id", UUID.class));
                newPhoto.setUser(user);

                CountryEntity country = new CountryEntity();
                country.setId(rs.getObject("p.country_id", UUID.class));
                newPhoto.setCountry(country);

                photos.add(newPhoto);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return photos;
    }
}
