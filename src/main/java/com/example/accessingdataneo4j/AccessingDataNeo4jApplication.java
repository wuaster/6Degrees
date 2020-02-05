package com.example.accessingdataneo4j;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class AccessingDataNeo4jApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AccessingDataNeo4jApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(ActorRepository actorRepository, MovieRepository movieRepository) {
        return args -> {

            actorRepository.deleteAll();
            movieRepository.deleteAll();
 
        };
    }

}