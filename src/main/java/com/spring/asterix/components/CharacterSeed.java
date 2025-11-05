package com.spring.asterix.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.repositories.CharacterRepository;
import com.spring.asterix.services.CharacterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

@Component
@ConditionalOnProperty(
        prefix = "app",
        name = "seeder.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class CharacterSeed implements CommandLineRunner {
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;
    private final CharacterService characterService;

    public CharacterSeed( CharacterRepository characterRepository, ObjectMapper objectMapper, CharacterService characterService ) {
        this.characterRepository = characterRepository;
        this.objectMapper = objectMapper;
        this.characterService = characterService;
    }

    @Override
    public void run( String... args ) throws Exception {
        long count = this.characterRepository.count();
        if ( count > 0 ) {
            System.out.println( "✅ MongoDB already contains " + count + " characters. Skipping seeding." );
            return;
        }

        System.out.println( "⚠️ Character collection is empty. Seeding initial data from characters.json..." );

        ClassPathResource resource = new ClassPathResource( "characters.json" );
        try ( InputStream inputStream = resource.getInputStream() ) {
            List<CharacterDTO> characters = objectMapper.readValue( inputStream, new TypeReference<List<CharacterDTO>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            } );
            this.characterService.createManyCharacters( characters );
            System.out.println( "🌱 Seeded " + characters.size() + " characters into MongoDB." );
        } catch ( Exception e ) {
            System.err.println( "❌ Failed to seed characters: " + e.getMessage() );
        }
    }
}
