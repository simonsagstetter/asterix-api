package com.spring.asterix.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.models.Character;
import com.spring.asterix.services.CharacterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.temporal.ChronoUnit;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // prevents race conditions of different test methods
class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CharacterService characterService;

    @Test
    void getAllCharacters_ShouldReturnAllCharactersFromCharacterSeed_WhenCalled() throws Exception {
        //GIVEN
        CharacterDTO character = CharacterDTO.builder()
                .name( "Asterix" )
                .description( "A clever and brave Gaulish warrior, small but fearless." )
                .attributes(
                        List.of(
                                "brave",
                                "strategic",
                                "strong"
                        )
                )
                .age( 35 )
                .nationality( "Gaul" )
                .occupation( "Warrior" )
                .firstAppearance( "Asterix the Gaul" )
                .village( "Armorican Village" )
                .isMainCharacter( true )
                .build();

        Character storedCharacter = characterService.createCharacter( character );

        String jsonContent = new ObjectMapper().writeValueAsString( List.of( character ) );
        String createdAt = storedCharacter.createdAt().truncatedTo( ChronoUnit.MILLIS ).toString();

        //WHEN
        mockMvc.perform( MockMvcRequestBuilders
                        .get( "/api/v1/characters" ) )
                //THEN
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.content().json( jsonContent ) )
                .andExpect( MockMvcResultMatchers.jsonPath( "$[0].id" ).value( storedCharacter.id() ) )
                .andExpect( MockMvcResultMatchers.jsonPath( "$[0].createdAt" ).value( createdAt ) )
                .andExpect( MockMvcResultMatchers.jsonPath( "$[0].updatedAt" ).value( createdAt ) );
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/characters.csv", delimiter = ',', numLinesToSkip = 1)
    void createCharacter( String name, String description, String attributes, String age, String nationality, String occupation, String firstAppearance, String village, String isMainCharacter ) throws Exception {
        //GIVEN
        CharacterDTO characterDTO = CharacterDTO.builder()
                .name( name )
                .description( description )
                .attributes( List.of( attributes.split( ", " ) ) )
                .age( Integer.parseInt( age ) )
                .nationality( nationality )
                .occupation( occupation )
                .firstAppearance( firstAppearance )
                .village( village )
                .isMainCharacter( Boolean.parseBoolean( isMainCharacter ) )
                .build();

        String jsonContent = new ObjectMapper().writeValueAsString( characterDTO );
        //WHEN
        mockMvc.perform( MockMvcRequestBuilders
                        .post( "/api/v1/characters" )
                        .accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                )
                //THEN
                .andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.content().json( jsonContent ) )
                .andExpect( MockMvcResultMatchers.jsonPath( "$.id" ).isNotEmpty() )
                .andExpect( MockMvcResultMatchers.jsonPath( "$.createdAt" ).isNotEmpty() )
                .andExpect( MockMvcResultMatchers.jsonPath( "$.updatedAt" ).isNotEmpty() );
    }
}