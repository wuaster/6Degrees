package com.example.accessingdataneo4j;


import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;


@NodeEntity
public class Movie{
 
    @Id @GeneratedValue private Long id;

    private String movieId, name;

    private Movie() {
        // Empty constructor required as of Neo4j API 2.0.5
    };

    public Movie(String name, String movieId) {
        this.name = name;
        this.movieId = movieId;
        
    }

    Set<String> actors = new HashSet<>();
    public void addActor(Actor actor) {
      
        actors.add(actor.getActorId());

    }
    

    public String toString() {
       return this.name + " " + this.movieId + " " + this.actors;
    }
    public String getMovieId() {
      return movieId;
   }

    public String getName() {
        return name;
    }
    
    public Set<String> getActors() {
      return actors;
    }
    public void setName(String name) {
        this.name = name;
    }
}