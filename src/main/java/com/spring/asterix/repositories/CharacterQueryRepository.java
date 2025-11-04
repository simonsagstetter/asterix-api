package com.spring.asterix.repositories;

import com.spring.asterix.models.Character;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public interface CharacterQueryRepository {
    Optional<List<Character>> query( Query query );
}
