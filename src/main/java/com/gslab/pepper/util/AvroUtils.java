package com.gslab.pepper.util;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AvroUtils {

    /**
     * Generates a binary Avro message payload, for given JSON string (which should fit the shape of a given Avro
     * schema string).  Allows test plan writers to enter message data test into an {@link com.gslab.pepper.config.avro.AvroConfigElement},
     * without the need for compiled classes backing the Avro schema.
     *
     * @param json Static message data, with any dynamic functions already previously rendered
     * @param avroSchema An Avro schema, to which the "json" data should conform
     * @return A binary Avro payload, for the given schema and data
     * @throws IOException
     */
    public static List<GenericRecord> serialize(final String json, final String avroSchema) throws IOException {
        try (final InputStream input = new ByteArrayInputStream(json.getBytes())) {
            final Schema schema = new Schema.Parser().parse(avroSchema);

            final DataInputStream din = new DataInputStream(input);
            final Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            final DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
            final List<GenericRecord> records = new ArrayList<>();
            while (true) {
                try {
                    final GenericRecord record = reader.read(null, decoder);
                    records.add(record);
                } catch (final EOFException eofe) {
                    break;
                }
            }
            return records;
        }
    }

    /**
     * Parses a binary Avro message payload.  For use in unit testing, or from Kafka consumer code in a JSR223 Sampler,
     * or anywhere else one needs to inspect an Avro message.
     *
     * NOTE:  "GenericRecord" contains both the relevant Avro schema, and the actual message value.  You can obtain a
     *        JSON representation of the value or its schema like this:
     *
     *        <code>deserialize(msg).get(0).toString()</code>
     *        <code>deserialize(msg).get(0).getSchema().toString()</code>
     *
     * @param message A binary Avro payload
     * @return Avro API data structure, containing the payload's schema and data contents
     * @throws IOException
     */
    public static List<GenericRecord> deserialize(final byte[] message) throws IOException {
        final List<GenericRecord> listOfRecords = new ArrayList<>();
        final DatumReader<GenericRecord> reader = new SpecificDatumReader<>();
        final DataFileReader<GenericRecord> fileReader = new DataFileReader<>(new SeekableByteArrayInput(message), reader);
        while (fileReader.hasNext()) {
            listOfRecords.add(fileReader.next());
        }
        return listOfRecords;
    }

}
