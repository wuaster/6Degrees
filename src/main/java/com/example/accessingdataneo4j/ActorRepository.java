package com.example.accessingdataneo4j;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {

    Actor findByName(String name);
    Actor findByActorId(String actorId);
    Long deleteByActorId(String actorId);
    


}