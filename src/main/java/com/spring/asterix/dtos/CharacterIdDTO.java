package com.spring.asterix.dtos;

import lombok.With;

import java.util.List;

@With
public record CharacterIdDTO( String name,
                              String description,
                              List<String> attributes,
                              int age,
                              String nationality,
                              String occupation,
                              String firstAppearance,
                              String village,
                              boolean isMainCharacter ) {
}
