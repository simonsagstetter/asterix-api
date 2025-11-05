package com.spring.asterix.repositories;

import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.models.Character;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface CharacterRepository extends MongoRepository<Character, String>, CharacterQueryRepository {
    Optional<Character> getCharacterById( String id );

    CharacterDTO insert( CharacterDTO character );

    List<CharacterDTO> insert( List<CharacterDTO> character );

}
