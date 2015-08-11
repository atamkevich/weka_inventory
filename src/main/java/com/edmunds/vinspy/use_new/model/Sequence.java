package com.edmunds.vinspy.use_new.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Vitaly_Krasovsky
 */
@Document
@CompoundIndexes(value = {
        @CompoundIndex(name = "sequence_name_index", def = "{'name':1}")})
public class Sequence {

    @Id
    private String name;
    private Long value;

    public String getName() {
        return name;
    }

    public Long getValue() {
        return value;
    }
}
