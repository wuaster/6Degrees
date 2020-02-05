package com.example.accessingdataneo4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;

@RestController
public class ActorController {
    @Autowired
    private ActorRepository actorRepository;
    // List to hold all bacon paths
    List<String> baconPaths = new ArrayList<String>();
    @PutMapping("/api/v1/addActor")
    public ResponseEntity<String> addActor(@RequestBody String json) throws IOException, JSONException {
        String name = null;
        String actorId = null;
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        
        if (deserialized.has("name") && deserialized.has("actorId")) {
          // Get the values for the name and actorId field
          name = deserialized.getString("name");
          actorId = deserialized.getString("actorId");
          Actor actor = new Actor(name, actorId);
          addNewNode(actor);
          response = new ResponseEntity<String>(HttpStatus.OK);
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
    
    @GetMapping(value="/api/v1/getActor", produces="application/json")
    public ResponseEntity<Actor> getActor(@RequestBody String json) throws IOException, JSONException {
        String actorId = null;
        ResponseEntity<Actor> response = new ResponseEntity<Actor>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        
        if (deserialized.has("actorId")) {
          // Get the values for the name and actorId field
          actorId = deserialized.getString("actorId");
          Actor actor = actorRepository.findByActorId(actorId);
          if (actor != null ) {
            // If actor can be found
            response = new ResponseEntity<Actor>(actor, HttpStatus.OK);
          }
          else {
            response = new ResponseEntity<Actor>(HttpStatus.NOT_FOUND);
          }
        }
        else {
          response = new ResponseEntity<Actor>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
 
   
    
    @GetMapping(value="/api/v1/computeBaconNumber", produces="application/json")
    public ResponseEntity<String> computeBacon(@RequestBody String json) throws IOException, JSONException {
        String actorId, responseString = null;
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        int baconNumber = 0;
        String baconPath = "";
        baconPaths.clear();
        if (deserialized.has("actorId")) {
          // Get the values for the name and actorId field
          actorId = deserialized.getString("actorId");
          Actor actor = actorRepository.findByActorId(actorId);
          if (actor != null ) {
            // calculate bacon number and path if the given actor isn't Kevin Bacon
            if (!actor.getName().equals("Kevin Bacon")) {
              // Initialize sets for visited nodes
              Set<Movie> visitedM = new HashSet<Movie>();
              Set<Actor> visitedA = new HashSet<Actor>();
              // Add this actor to the set of visited actors
              visitedA.add(actor);
              getBaconPaths(baconPath, actor, visitedM, visitedA);
              String[] shortestPath = computeShortestPath();
              if (shortestPath != null) {
                baconNumber = (shortestPath.length - 1)/2;
                responseString = "{\n\t\"baconNumber\": \"" + String.valueOf(baconNumber) + "\"\n}";
                // give response accordingly
                response = new ResponseEntity<String>(responseString, HttpStatus.OK);
              }
              else {
                // No path to Kevin Bacon was found
                response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
              }
            }
            else {
              // given actor is kevin bacon so bacon number is 0 and bacon path dne
              responseString = "{\n\t\"baconNumber\": \"0\"\n}";
              // give response accordingly
              response = new ResponseEntity<String>(responseString, HttpStatus.OK);
            }
          }
          // given actor was not in the database
          else {
            response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
          }
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
    
    @GetMapping(value="/api/v1/computeBaconPath", produces="application/json")
    public ResponseEntity<String> computeBaconPath(@RequestBody String json) throws IOException, JSONException {
        String actorId, responseString = null;
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        JSONObject deserialized = new JSONObject(json);
        int baconNumber = 0;
        String baconPath = "";
        baconPaths.clear();
        if (deserialized.has("actorId")) {
          // Get the values for the name and actorId field
          actorId = deserialized.getString("actorId");
          Actor actor = actorRepository.findByActorId(actorId);
          if (actor != null ) {
            // calculate bacon number and path if the given actor isn't Kevin Bacon
            if (!actor.getName().equals("Kevin Bacon")) {
              // Initialize sets for visited nodes
              Set<Movie> visitedM = new HashSet<Movie>();
              Set<Actor> visitedA = new HashSet<Actor>();
              // Add this actor to the set of visited actors
              visitedA.add(actor);
              getBaconPaths(baconPath, actor, visitedM, visitedA);
              String[] shortestPath = computeShortestPath();
              if (shortestPath != null) {
                baconNumber = (shortestPath.length - 1)/2;
                baconPath = "";
                // Add in all actor and movie IDs
                for (int i = 0; i<shortestPath.length-1; i+=2) {
                  baconPath += "\t\t\t{\n\t\t\t\t\"actorId\": \"" + shortestPath[i] + "\",\n";
                  baconPath += "\t\t\t\t\"movieId\": \"" + shortestPath[i+1] + "\"\n\t\t\t},\n";
                }
                // Add the Kevin Bacon info
                baconPath += "\t\t\t{\n\t\t\t\t\"actorId\": \"" + shortestPath[shortestPath.length-1] + "\",\n";
                baconPath += "\t\t\t\t\"movieId\": \"" + shortestPath[shortestPath.length-2] + "\"\n\t\t\t}\n";
                responseString = "{\n\t\"baconNumber\": \"" + String.valueOf(baconNumber) + "\"\n\t\t\"baconPath\": [" + baconPath + "\t\t]\n}";
                // give response accordingly
                response = new ResponseEntity<String>(responseString, HttpStatus.OK);
              }
              else {
                // No path to Kevin Bacon was found
                response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
              }
            }
            else {
              // given actor is kevin bacon so bacon number is 0 and bacon path is empty
              responseString = "{\n\t\"baconNumber\": \"0\"\n\t\"baconPath\": []\n}";
              // give response accordingly
              response = new ResponseEntity<String>(responseString, HttpStatus.OK);
            }
          }
          // given actor was not in the database
          else {
            response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
          }
        }
        else {
          response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    public void getBaconPaths(String baconPath, Actor actor, Set<Movie> visitedM, Set<Actor> visitedA){
      // If this actor is Kevin Bacon, add it to the path, save it to the list of bacon paths and stop
      if (actor.getName().equals("Kevin Bacon")) {
        baconPath += "," + actor.getActorId();
        baconPaths.add(baconPath);
      }
      // Else visit all the other movies that haven't been visited yet
      else if (actor.returnMovies() != null && !visitedM.containsAll(actor.returnMovies())) {
        if (baconPath.equals("")) {
          // We don't want a , before the first actor id
          baconPath += actor.getActorId();
        }
        // if not first actor, add the comma before id
        else {
          baconPath += "," + actor.getActorId();
        }
        // visit all movies and calculate bacon path from there
        for(Movie movie : actor.returnMovies()){
          if (!visitedM.contains(movie)) {
            visitedM.add(movie);
            calculateBaconMovie(baconPath, movie, visitedM, visitedA);
          }
        }
      }
      
    }
    
    
    private String[] computeShortestPath() {
      String[] shortestPath = null;
      if (baconPaths.size()>0) {
        String smallest = baconPaths.get(0);
      
        for (String paths : baconPaths) {
          if (smallest.length()>paths.length()) {
            smallest = paths;
          }
        }
        // sort the ids into an array
        shortestPath = smallest.split(",");
      }
      return shortestPath;
      
    }
    
    public void calculateBaconMovie(String baconPath, Movie movie, Set<Movie> visitedM, Set<Actor> visitedA) {
      // add , before movie id to the current path
      baconPath += "," + movie.getMovieId();
      // visit all unvisited actors in the movie
      for (String actorId : movie.getActors()){
        Actor actor = actorRepository.findByActorId(actorId);
        if (!visitedA.contains(actor)) {
          visitedA.add(actor);
          // get bacon paths from here
          getBaconPaths(baconPath, actor, visitedM, visitedA);
        } 
      }
    }
    
    // updates by deleting the node and creating a new one
    public void addNewNode(Actor actor) {
      // if it already exists, update the name
      if (actorRepository.findByActorId(actor.getActorId()) != null) {
        Actor repoActor = actorRepository.findByActorId(actor.getActorId());
        repoActor.setName(actor.getName());
        actor = repoActor;
      }
        actorRepository.save(actor);
    }
   
}