package com.cts.tripease.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.util.stream.Stream;

public class FlightIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        String prefix = "flt_";
        // This query finds the current max ID to determine the next number
        String query = String.format("select %s from %s", 
            session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
            obj.getClass().getSimpleName());
        
        Stream<String> ids = session.createQuery(query, String.class).stream();
        Long max = ids.map(id -> id.replace(prefix, ""))
                     .mapToLong(Long::parseLong)
                     .max()
                     .orElse(0L);

        return prefix + (max + 1);
    }
}