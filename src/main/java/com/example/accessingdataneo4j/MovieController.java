package com.example.accessingdataneo4j;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;

@RestController
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;
    
    
    @PutMapping("/api/v1/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody String json) throws IOException, JSONException {
        String name = null;
        String movieId = null;
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        
        if (deserialized.has("name") && deserialized.has("movieId")) {
          // Get the values for the name and movieId field
          name = deserialized.getString("name");
          movieId = deserialized.getString("movieId");
          Movie movie = new Movie(name, movieId);
          addNewNode(movie);
          response = new ResponseEntity<String>(HttpStatus.OK);
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
    
    @GetMapping("/api/v1/getMovie")
    public ResponseEntity<Movie> getMovie(@RequestBody String json) throws IOException, JSONException {
        String movieId = null;
        ResponseEntity<Movie> response = new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        
        if (deserialized.has("movieId")) {
          // Get the values for the name and movieId field
          movieId = deserialized.getString("movieId");
  
          Movie movie = movieRepository.findByMovieId(movieId);
          if (movie != null) {
            response = new ResponseEntity<Movie>(movie, HttpStatus.OK);
          }
          else {
            response = new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
          }
          
        }
        else {
          response = new ResponseEntity<Movie>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
   
    public void addNewNode(Movie movie) {
      
      if (movieRepository.findByMovieId(movie.getMovieId()) != null) {
  
        Movie repoMovie = movieRepository.findByMovieId(movie.getMovieId());
        repoMovie.setName(movie.getName());
        movie = repoMovie;
      }
      movieRepository.save(movie);

    }
   
}