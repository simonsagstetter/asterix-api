package com.spring.asterix.repositories;

import com.spring.asterix.models.Character;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String>, CharacterQueryRepository {
    Optional<Character> getCharacterById( String id );
}
