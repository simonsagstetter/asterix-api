package com.spring.asterix.services;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdService {

    public IdService() {
    }

    public String generateIdFor( String recordName ) {
        return recordName.substring( 0, 3 ).toUpperCase() + UUID.randomUUID().toString().replaceAll( "-", "" ).toUpperCase();
    }
}
