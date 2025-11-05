package com.spring.asterix.controllers;

import com.spring.asterix.dtos.CharacterDTO;
import com.spring.asterix.dtos.CharacterPatchDTO;
import com.spring.asterix.exceptions.CharacterNotFoundException;
import com.spring.asterix.models.Character;
import com.spring.asterix.services.CharacterService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(
        path = "/api/v1/characters",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        headers = {
                "Content-Type=" + MediaType.APPLICATION_JSON_VALUE,
                "Accept=" + MediaType.APPLICATION_JSON_VALUE
        }
)
public class CharacterController {
    private final CharacterService characterService;

    public CharacterController( CharacterService characterService ) {
        this.characterService = characterService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Character create( @RequestBody CharacterDTO character ) {
        return this.characterService.createCharacter( character );
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Character> createMany( @RequestBody List<CharacterDTO> characters ) {
        return this.characterService.createManyCharacters( characters );
    }

    @GetMapping(path = "", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Character> getAll() {
        return this.characterService.findAllCharacters();
    }

    @GetMapping(path = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Character getById( @PathVariable String id ) {
        try {
            return this.characterService.getCharacterById( id );
        } catch ( CharacterNotFoundException e ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @GetMapping(path = "/search", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Character> search( @RequestParam Optional<String> name,
                                   @RequestParam Optional<String> attribute,
                                   @RequestParam Optional<Integer> age,
                                   @RequestParam Optional<String> nationality,
                                   @RequestParam Optional<String> occupation,
                                   @RequestParam Optional<String> village,
                                   @RequestParam Optional<Boolean> main ) {
        try {
            return this.characterService.queryCharacters( name, attribute, age, nationality, occupation, village, main );
        } catch ( CharacterNotFoundException e ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDTO update( @PathVariable String id, @RequestBody CharacterDTO character ) {
        try {
            return this.characterService.updateCharacter( id, character );
        } catch ( CharacterNotFoundException e ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e );
        }
    }

    @CrossOrigin("http://localhost")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDTO patch( @PathVariable String id, @RequestBody CharacterPatchDTO character ) {
        try {
            return this.characterService.partialUpdateCharacter( id, character );
        } catch ( CharacterNotFoundException e ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e );
        }
    }

    @CrossOrigin("http://localhost")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteById( @PathVariable String id ) {
        try {
            return this.characterService.deleteCharacter( id );
        } catch ( CharacterNotFoundException e ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e );
        }
    }
}
