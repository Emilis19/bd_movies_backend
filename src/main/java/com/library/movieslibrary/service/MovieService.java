package com.library.movieslibrary.service;

import com.library.movieslibrary.model.SavedMovie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieService extends MongoRepository<SavedMovie, String> {
    List<SavedMovie> findAllByUserId(String userId);
    SavedMovie findByUserIdAndTitle(String userId, String title);
}
