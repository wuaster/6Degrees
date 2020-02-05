package com.example.accessingdataneo4j;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    Movie findByName(String name);
    Movie findByMovieId(String movieId);
    Long deleteByMovieId(String movieId);
}