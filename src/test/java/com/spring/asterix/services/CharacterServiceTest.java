package com.spring.asterix.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.dtos.CharacterMapper;
import com.spring.asterix.models.Character;
import com.spring.asterix.repositories.CharacterRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    private static final CharacterRepository characterMockRepository = Mockito.mock( CharacterRepository.class );
    private static final CharacterMapper characterMapper = new CharacterMapper();
    private static final IdService idServiceMock = Mockito.mock( IdService.class );
    private static final AuditService auditServiceMock = Mockito.mock( AuditService.class );
    private static final CharacterService characterService = new CharacterService( characterMockRepository, characterMapper, idServiceMock, auditServiceMock );
    private static final List<CharacterDTO> characterDTOList = new ArrayList<>();
    private static final String recordTestId = "CHA-TEST-ID-STRING";
    private static final Instant createdAt = Instant.now();

    @BeforeAll
    static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource( "characters.json" );
        try ( InputStream inputStream = resource.getInputStream() ) {
            characterDTOList.addAll( objectMapper.readValue( inputStream, new TypeReference<List<CharacterDTO>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            } ) );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    @Test
    void createCharacter() {
        //GIVEN
        CharacterDTO characterDTO = characterDTOList.getFirst();

        //MOCKING
        Character expectedCharacter = characterMapper.toCharacter( recordTestId, createdAt, createdAt, characterDTO );
        Character expectedResponse = expectedCharacter;

        when( idServiceMock.generateIdFor( Character.class.getSimpleName() ) )
                .thenReturn( recordTestId );

        when( auditServiceMock.getCurrentTimestamp() )
                .thenReturn( createdAt );

        when( characterMockRepository.insert( expectedCharacter ) )
                .thenReturn( expectedResponse );

        //WHEN
        Character actual = characterService.createCharacter( characterDTO );

        //THEN
        assertThat( actual ).isEqualTo( expectedResponse );
        assertThat( actual.id() ).isEqualTo( recordTestId );

        //VERIFY
        verify( idServiceMock, atLeastOnce() ).generateIdFor( Character.class.getSimpleName() );
        verify( characterMockRepository, atLeastOnce() ).insert( expectedCharacter );
    }

    @Test
    void createManyCharacters() {
        //GIVEN
        List<Character> expectedArgument = characterDTOList.stream()
                .map( character -> characterMapper.toCharacter( recordTestId, createdAt, createdAt, character ) )
                .toList();

        List<Character> expectedResponse = expectedArgument;

        //MOCKING
        when( idServiceMock.generateIdFor( Character.class.getSimpleName() ) )
                .thenReturn( recordTestId );
        
        when( auditServiceMock.getCurrentTimestamp() )
                .thenReturn( createdAt );

        when( characterMockRepository.insert( expectedArgument ) )
                .thenReturn( expectedResponse );

        //WHEN
        List<Character> actual = characterService.createManyCharacters( characterDTOList );
        //THEN
        assertThat( actual )
                .isNotNull()
                .isNotEmpty()
                .isEqualTo( expectedResponse );

        assertThat( actual.size() ).isEqualTo( expectedArgument.size() );
        assertThat( actual ).extracting( "id" )
                .contains( recordTestId );

        assertThat( actual ).containsAll( expectedResponse );

        verify( idServiceMock, times( expectedArgument.size() ) ).generateIdFor( Character.class.getSimpleName() );
        verify( characterMockRepository, atLeastOnce() ).insert( expectedArgument );
    }

}