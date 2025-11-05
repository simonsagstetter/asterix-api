package com.spring.asterix.dtos;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
public record CharacterDTO( @NonNull String name,
                            @NonNull String description,
                            @NonNull List<String> attributes,
                            @NonNull Integer age,
                            @NonNull String nationality,
                            @NonNull String occupation,
                            @NonNull String firstAppearance,
                            @NonNull String village,
                            @NonNull Boolean isMainCharacter ) {

    public CharacterDTO copy() {
        return new CharacterDTO(
                this.name,
                this.description,
                this.attributes,
                this.age, this.nationality,
                this.occupation,
                this.firstAppearance,
                this.village,
                this.isMainCharacter
        );
    }
}
