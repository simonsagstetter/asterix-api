package com.spring.asterix.dtos;

import com.spring.asterix.models.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    public CharacterDTO toCharacterDTO( Character character ) {
        return CharacterDTO.builder()
                .name( character.name() )
                .description( character.description() )
                .attributes( character.attributes() )
                .age( character.age() )
                .nationality( character.nationality() )
                .occupation( character.occupation() )
                .firstAppearance( character.firstAppearance() )
                .village( character.village() )
                .isMainCharacter( character.isMainCharacter() )
                .build();
    }

    public Character toCharacter( String id, CharacterDTO characterDTO ) {
        return Character.builder()
                .id( id )
                .name( characterDTO.name() )
                .description( characterDTO.description() )
                .attributes( characterDTO.attributes() )
                .age( characterDTO.age() )
                .nationality( characterDTO.nationality() )
                .occupation( characterDTO.occupation() )
                .firstAppearance( characterDTO.firstAppearance() )
                .village( characterDTO.village() )
                .isMainCharacter( characterDTO.isMainCharacter() )
                .build();
    }
}
