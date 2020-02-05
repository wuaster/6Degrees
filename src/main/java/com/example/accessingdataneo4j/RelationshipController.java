package com.example.accessingdataneo4j;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelationshipController {

  @Autowired
  private ActorRepository actorRepository;
  @Autowired
  private MovieRepository movieRepository;
  
  @PutMapping("/api/v1/addRelationship")
  public ResponseEntity<String> addRelationship(@RequestBody String json) throws IOException, JSONException {
      String actorId = null;
      String movieId = null;
      ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      JSONObject deserialized = new JSONObject(json);
      
      if (deserialized.has("actorId") && deserialized.has("movieId")) {
        // Get the values for the name and actorId field
        actorId = deserialized.getString("actorId");
        movieId = deserialized.getString("movieId");
        
        Actor actor = actorRepository.findByActorId(actorId);
        Movie movie = movieRepository.findByMovieId(movieId);
        if (actor != null && movie != null) {
        actor.actsIn(movie);
        movie.addActor(actor);
        actorRepository.save(actor);
        response = new ResponseEntity<String>(HttpStatus.OK);
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
      }
      else {
        response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }

      return response;
  }
  @GetMapping(value="/api/v1/hasRelationship", produces="application/json")
  public ResponseEntity<String> hasRelationship(@RequestBody String json) throws IOException, JSONException {
      String actorId = null;
      String movieId = null;
      ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      JSONObject deserialized = new JSONObject(json);
      boolean hasRelationship = false;
      if (deserialized.has("actorId") && deserialized.has("movieId")) {
        // Get the values for the name and actorId field
        actorId = deserialized.getString("actorId");
        movieId = deserialized.getString("movieId");
        Actor actor = actorRepository.findByActorId(actorId);
        Movie movie = movieRepository.findByMovieId(movieId);
        if (actor != null && movie != null) {
          if (actor.getMovies().contains(movie.getMovieId())) {
            hasRelationship = true;
          }
          String responseBody = "{\n\t\"actorId\": "+ "\"" + actorId + "\",\n\t\"movieId\": "
              + "\"" + movieId + "\",\n\t\"hasRelationship\": " + hasRelationship + "\n}";
          response = new ResponseEntity<String>(responseBody, HttpStatus.OK);
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
      }
      else {
        response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }

      return response;
  }
}
