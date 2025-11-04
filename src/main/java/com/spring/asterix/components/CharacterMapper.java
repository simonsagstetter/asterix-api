package com.spring.asterix.components;

import com.spring.asterix.dtos.CharacterIdDTO;
import com.spring.asterix.models.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    public CharacterIdDTO toCharacterIdDTO( Character character ) {
        return new CharacterIdDTO(
                character.name(),
                character.description(),
                character.attributes(),
                character.age(),
                character.nationality(),
                character.occupation(),
                character.firstAppearance(),
                character.village(),
                character.isMainCharacter()
        );
    }
}
