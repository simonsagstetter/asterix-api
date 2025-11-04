package com.spring.asterix.services;

import com.spring.asterix.exceptions.CharacterNotFoundException;
import com.spring.asterix.models.Character;
import com.spring.asterix.repositories.CharacterRepository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;

    public CharacterService( CharacterRepository characterRepository ) {
        this.characterRepository = characterRepository;
    }

    public Character createCharacter( Character character ) {
        return this.characterRepository.insert( character );
    }

    public List<Character> createManyCharacters( List<Character> characters ) {
        return this.characterRepository.insert( characters );
    }

    public List<Character> findAllCharacters() {
        return this.characterRepository.findAll();
    }

    public Character getCharacterById( String characterId ) throws CharacterNotFoundException {
        return this.characterRepository
                .getCharacterById( characterId )
                .orElseThrow( CharacterNotFoundException::new );

    }

    public List<Character> queryCharacters(
            Optional<String> name,
            Optional<String> attribute,
            Optional<Integer> age,
            Optional<String> nationality,
            Optional<String> occupation,
            Optional<String> village,
            Optional<Boolean> main
    ) throws CharacterNotFoundException {

        Query query = new Query();

        name.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "name" )
                        .regex( s, "i" )
        ) );

        attribute.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "attributes" )
                        .in( s )
        ) );

        age.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "age" )
                        .is( s )
        ) );

        nationality.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "nationality" )
                        .regex( s, "i" )
        ) );

        occupation.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "occupation" )
                        .regex( s, "i" )

        ) );

        village.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "village" )
                        .regex( s, "i" )
        ) );

        main.ifPresent( s -> query.addCriteria(
                Criteria
                        .where( "isMainCharacter" )
                        .is( s )
        ) );

        System.out.println();

        return this.characterRepository
                .query( query )
                .orElseThrow( CharacterNotFoundException::new );
    }

    public Character updateCharacter( String characterId, Character character ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );

        return this.characterRepository
                .save( character.withId( oldCharacter.id() ) );
    }

    public void deleteCharacter( String characterId ) {
        this.characterRepository.deleteById( characterId );
    }
}
