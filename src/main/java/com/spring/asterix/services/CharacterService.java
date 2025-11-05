package com.spring.asterix.services;

import com.spring.asterix.dtos.CharacterMapper;
import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.dtos.CharacterPatchDTO;
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

    public CharacterDTO createCharacter( CharacterDTO character ) {
        return this.characterRepository.insert( character );
    }

    public List<CharacterDTO> createManyCharacters( List<CharacterDTO> characters ) {
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

    public CharacterDTO updateCharacter( String characterId, CharacterDTO character ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );

        Character updatedCharacter = this.characterRepository.save(
                characterMapper.toCharacter( oldCharacter.id(), character )
        );

        return characterMapper.toCharacterDTO( updatedCharacter );
    }

    public CharacterDTO partialUpdateCharacter( String characterId, CharacterPatchDTO character ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );

        System.out.println( character );

        Character updatedCharacter = Character.builder()
                .id( oldCharacter.id() )
                .attributes( oldCharacter.attributes() )
                .name( character.name() != null ? character.name() : oldCharacter.name() )
                .description( character.description() != null ? character.description() : oldCharacter.description() )
                .age( character.age() != null ? character.age() : oldCharacter.age() )
                .nationality( character.nationality() != null ? character.nationality() : oldCharacter.nationality() )
                .occupation( character.occupation() != null ? character.occupation() : oldCharacter.occupation() )
                .firstAppearance( character.firstAppearance() != null ? character.firstAppearance() : oldCharacter.firstAppearance() )
                .village( character.village() != null ? character.village() : oldCharacter.village() )
                .isMainCharacter( character.isMainCharacter() != null ? character.isMainCharacter() : oldCharacter.isMainCharacter() )

                .build();

        return characterMapper.toCharacterDTO( this.characterRepository.save( updatedCharacter ) );
    }

    public CharacterDTO partialUpdateCharacter( String characterId, List<String> attributes ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );


        Character updatedCharacter = oldCharacter.copy()
                .withAttributes( attributes );

        return characterMapper.toCharacterDTO( this.characterRepository.save( updatedCharacter ) );
    }

    public boolean deleteCharacter( String characterId ) throws CharacterNotFoundException {
        Character character = this.getCharacterById( characterId );
        this.characterRepository.deleteById( character.id() );
        return true;
    }
}
