package com.example.accessingdataneo4j;


import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity
public class Actor{
 
    @Id @GeneratedValue private Long id;

    private String actorId, name;

    private Actor() {
        // Empty constructor required as of Neo4j API 2.0.5
    };

    public Actor(String name, String actorId) {
        this.name = name;
        this.actorId = actorId;
        
    }
    /**
     * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
     * to ignore the direction of the relationship.
     * https://dzone.com/articles/modelling-data-neo4j
     */
    @Relationship(type = "ACTED_IN", direction = Relationship.UNDIRECTED)
    public Set<Movie> movies;
    Set<String> movieIds = new HashSet<>();
    public void actsIn(Movie movie) {
        if (movies == null) {
            movies = new HashSet<>();
        }
        movieIds.add(movie.getMovieId());
        movies.add(movie);
    }
    

    public String toString() {
       
      
       return this.name + " " + this.actorId + " " + this.movies;
    }
    public String getActorId() {
      return actorId;
    }

    public String getName() {
        return name;
    }
    

    public Set<Movie> returnMovies(){
      return movies;
    }
    public Set<String> getMovies() {
      return movieIds;
    }
    public void setName(String name) {
        this.name = name;
    }
}