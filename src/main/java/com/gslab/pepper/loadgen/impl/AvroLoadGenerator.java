package com.gslab.pepper.loadgen.impl;

import com.gslab.pepper.exception.PepperBoxException;
import com.gslab.pepper.input.SchemaProcessor;
import com.gslab.pepper.loadgen.BaseLoadGenerator;
import org.apache.avro.generic.GenericRecord;

import java.util.Iterator;

public class AvroLoadGenerator implements BaseLoadGenerator {

    private transient Iterator<GenericRecord> messageIterator = null;

    private transient SchemaProcessor schemaProcessor = new SchemaProcessor();

    public AvroLoadGenerator(final String avroTemplate, final String jmeterTemplate) throws PepperBoxException {
        // A JSON expression of the config element template, AFTER applying all dynamic functions
        final String json = schemaProcessor.getPlainTextMessageIterator(jmeterTemplate).next().toString();

        // An iterator whose "next()" method ALWAYS returns an Avro binary payload, for the JSON template as
        // applied to the Avro schema.
        this.messageIterator = schemaProcessor.getAvroMessageIterator(avroTemplate, json);
    }

    @Override
    public Object nextMessage() {
        return this.messageIterator.next();
    }

}
