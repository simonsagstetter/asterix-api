package com.spring.asterix.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.asterix.models.Character;
import com.spring.asterix.repositories.CharacterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class CharacterSeed implements CommandLineRunner {
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    public CharacterSeed( CharacterRepository characterRepository, ObjectMapper objectMapper ) {
        this.characterRepository = characterRepository;
        this.objectMapper = objectMapper;
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
            List<Character> characters = objectMapper.readValue( inputStream, new TypeReference<List<Character>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            } );
            this.characterRepository.insert( characters );
            System.out.println( "🌱 Seeded " + characters.size() + " characters into MongoDB." );
        } catch ( Exception e ) {
            System.err.println( "❌ Failed to seed characters: " + e.getMessage() );
        }

    }
}
