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
}
