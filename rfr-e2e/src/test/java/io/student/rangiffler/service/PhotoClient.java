package io.student.rangiffler.service;

import io.student.rangiffler.model.types.Photo;
import io.student.rangiffler.model.types.PhotoInput;

import java.util.UUID;

public interface PhotoClient {

    Photo createPhoto(PhotoInput photoInput, UUID userId);
}
