package com.spring.asterix.repositories;

import com.spring.asterix.models.Character;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CharacterQueryRepositoryImpl implements CharacterQueryRepository {
    private final MongoTemplate mongoTemplate;

    public CharacterQueryRepositoryImpl( MongoTemplate mongoTemplate ) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<List<Character>> query( Query query ) {
        List<Character> characters = mongoTemplate.find( query, Character.class );
        return characters.isEmpty() ? Optional.empty() : Optional.of( characters );
    }
}
