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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    private final IdService idService;
    private final AuditService auditService;

    public CharacterService( CharacterRepository characterRepository, CharacterMapper characterMapper, IdService idService, AuditService auditService ) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
        this.idService = idService;
        this.auditService = auditService;
    }

    public Character createCharacter( CharacterDTO character ) {
        String newRecordId = idService.generateIdFor( Character.class.getSimpleName() );
        Instant createdAt = auditService.getCurrentTimestamp();
        return this.characterRepository.insert( characterMapper.toCharacter( newRecordId, createdAt, createdAt, character ) );
    }

    public List<Character> createManyCharacters( List<CharacterDTO> characters ) {
        List<Character> characterList = characters
                .stream()
                .map( characterDTO -> {
                            Instant createdAt = auditService.getCurrentTimestamp();
                            return characterMapper.toCharacter(
                                    idService.generateIdFor( Character.class.getSimpleName() ),
                                    createdAt,
                                    createdAt,
                                    characterDTO );
                        }
                )
                .toList();
        return this.characterRepository.insert( characterList );
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
                characterMapper.toCharacter( oldCharacter.id(), oldCharacter.createdAt(), auditService.getCurrentTimestamp(), character )
        );

        return characterMapper.toCharacterDTO( updatedCharacter );
    }

    public CharacterDTO partialUpdateCharacter( String characterId, CharacterPatchDTO character ) throws CharacterNotFoundException {
        Character oldCharacter = this.getCharacterById( characterId );

        Character updatedCharacter = Character.builder()
                .id( oldCharacter.id() )
                .attributes( character.attributes() != null && !character.attributes().isEmpty() ? character.attributes() : oldCharacter.attributes() )
                .name( character.name() != null ? character.name() : oldCharacter.name() )
                .description( character.description() != null ? character.description() : oldCharacter.description() )
                .age( character.age() != null ? character.age() : oldCharacter.age() )
                .nationality( character.nationality() != null ? character.nationality() : oldCharacter.nationality() )
                .occupation( character.occupation() != null ? character.occupation() : oldCharacter.occupation() )
                .firstAppearance( character.firstAppearance() != null ? character.firstAppearance() : oldCharacter.firstAppearance() )
                .village( character.village() != null ? character.village() : oldCharacter.village() )
                .isMainCharacter( character.isMainCharacter() != null ? character.isMainCharacter() : oldCharacter.isMainCharacter() )
                .createdAt( oldCharacter.createdAt() )
                .updatedAt( auditService.getCurrentTimestamp() )
                .build();

        return characterMapper.toCharacterDTO( this.characterRepository.save( updatedCharacter ) );
    }

    public boolean deleteCharacter( String characterId ) throws CharacterNotFoundException {
        Character character = this.getCharacterById( characterId );
        this.characterRepository.deleteById( character.id() );
        return true;
    }
}
