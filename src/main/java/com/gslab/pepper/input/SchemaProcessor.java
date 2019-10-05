package com.gslab.pepper.input;

import com.gslab.pepper.exception.PepperBoxException;
import com.gslab.pepper.model.FieldExpressionMapping;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * The SchemaProcessor class reads input schema/field expression mapping and returns iterator.
 *
 * @Author Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 01/03/2017
 */
public class SchemaProcessor {

    private SchemaParser schemaParser = new SchemaParser();

    private SchemaTranslator schemaTranslator = new SchemaTranslator();

    /**
     * Creates an iterator for an Avro message payload ({@link com.gslab.pepper.config.avro.AvroConfigElement}).
     *
     * @param avroSchema An Avro schema definition (in JSON format)
     * @param inputSchema A test message (in JSON format), which SHOULD conform to "avroSchema", optionally containing dynamic functions
     * @return An Iterator whose "next()" method always returns a fresh re-rendering of the test message, in Avro binary format (i.e. byte[]).
     * @throws PepperBoxException
     *
     */
    public Iterator getAvroMessageIterator(final String avroSchema, final String inputSchema) throws PepperBoxException {
        // Generate Java source code, that renders the test message with any dynamic functions applied.
        final String javaSource = schemaParser.getProcessedSchema(inputSchema);

        // Dynamically compile that Java source into a class.  Then build an Iterator, whose "next()" method invokes that
        // class, and converts its rendered output into Avro binary form.
        return schemaTranslator.getAvroMsgIterator(avroSchema, javaSource);
    }

    /**
     * Creates Iterator for plaintext config element with input schema
     * @param inputSchema
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Iterator getPlainTextMessageIterator(String inputSchema) throws PepperBoxException {

        String processedSchema = schemaParser.getProcessedSchema(inputSchema);
        return  schemaTranslator.getPlainTextMsgIterator(processedSchema);
    }

    /**
     * Creates Iterator for serialized config element
     * @param inputClass
     * @param fieldExpressions
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Iterator getSerializedMessageIterator(String inputClass, List<FieldExpressionMapping> fieldExpressions) throws PepperBoxException {

        String execStatements = schemaParser.getProcessedSchema(fieldExpressions);
        return  schemaTranslator.getSerializedMsgIterator(inputClass, execStatements);
    }
}