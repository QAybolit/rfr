package io.student.rangiffler.data.extractor;

import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.entity.user.PhotoEntity;
import io.student.rangiffler.data.entity.user.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoEntityExtractor implements ResultSetExtractor<PhotoEntity> {

    public static final PhotoEntityExtractor INSTANCE = new PhotoEntityExtractor();

    private PhotoEntityExtractor() {
    }
    
    @Override
    public PhotoEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, PhotoEntity> photoMap = new ConcurrentHashMap<>();
        UUID photoId = null;
        while (rs.next()) {
            photoId = rs.getObject("id", UUID.class);
            PhotoEntity photo = photoMap.computeIfAbsent(photoId, id -> {
                try {
                    PhotoEntity newPhoto = new PhotoEntity();
                    newPhoto.setId(rs.getObject("p.id", UUID.class));
                    newPhoto.setDescription(rs.getString("p.description"));
                    newPhoto.setPhoto(rs.getObject("p.photo", byte[].class));
                    newPhoto.setCreatedDate(rs.getObject("p.created_date", java.sql.Date.class));

                    return newPhoto;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            UserEntity user = new UserEntity();
            user.setId(rs.getObject("p.user_id", UUID.class));
            photo.setUser(user);

            CountryEntity country = new CountryEntity();
            country.setId(rs.getObject("p.country_id", UUID.class));
            photo.setCountry(country);
        }
        return photoMap.get(photoId);
    }
}
