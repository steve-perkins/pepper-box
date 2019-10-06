package com.gslab.pepper.config.avro;

import com.gslab.pepper.util.PropsKeys;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TextAreaEditor;

import java.beans.PropertyDescriptor;

public class AvroConfigElementBeanInfo extends BeanInfoSupport {

    private static final String AVRO_SCHEMA = "avroSchema";
    private static final String JSON_TEMPLATE = "jsonTemplate";
    private static final String PLACE_HOLDER = "placeHolder";

    public AvroConfigElementBeanInfo() {
        super(AvroConfigElement.class);

        createPropertyGroup("avro_load_generator", new String[] {
                PLACE_HOLDER, AVRO_SCHEMA, JSON_TEMPLATE
        });

        final PropertyDescriptor placeHolderProps = property(PLACE_HOLDER);
        placeHolderProps.setValue(NOT_UNDEFINED, Boolean.TRUE);
        placeHolderProps.setValue(DEFAULT, PropsKeys.MSG_PLACEHOLDER);
        placeHolderProps.setValue(NOT_EXPRESSION, Boolean.TRUE);

        final PropertyDescriptor avroSchemaProps = property(AVRO_SCHEMA);
        avroSchemaProps.setPropertyEditorClass(TextAreaEditor .class);
        avroSchemaProps.setValue(NOT_UNDEFINED, Boolean.TRUE);

        final PropertyDescriptor jsonTemplateProps = property(JSON_TEMPLATE);
        jsonTemplateProps.setPropertyEditorClass(TextAreaEditor .class);
        jsonTemplateProps.setValue(NOT_UNDEFINED, Boolean.TRUE);
    }

}
