package com.spring.asterix.services;

import com.spring.asterix.components.CharacterMapper;
import com.spring.asterix.dtos.CharacterIdDTO;
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
    private final CharacterMapper characterMapper;

    public CharacterService( CharacterRepository characterRepository, CharacterMapper characterMapper ) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    public CharacterIdDTO createCharacter( CharacterIdDTO character ) {
        return this.characterRepository.insert( character );
    }

    public List<CharacterIdDTO> createManyCharacters( List<CharacterIdDTO> characters ) {
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

        return this.characterRepository
                .query( query )
                .orElseThrow( CharacterNotFoundException::new );
    }

    public CharacterIdDTO updateCharacter( String characterId, CharacterIdDTO character ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );

        Character updatedCharacter = this.characterRepository.save( oldCharacter
                .withName( character.name() )
                .withAttributes( character.attributes() )
                .withAge( character.age() )
                .withNationality( character.nationality() )
                .withOccupation( character.occupation() )
                .withVillage( character.village() ) );


        return characterMapper.toCharacterIdDTO( updatedCharacter );
    }

    public boolean deleteCharacter( String characterId ) throws CharacterNotFoundException {
        Character character = this.getCharacterById( characterId );
        this.characterRepository.deleteById( character.id() );
        return true;
    }
}
