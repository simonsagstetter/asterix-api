package com.spring.asterix.exceptions;

public class CharacterNotFoundException extends Exception {
    public CharacterNotFoundException() {
        super( "No characters found" );
    }
}
