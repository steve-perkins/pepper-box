package com.gslab.pepper.config.avro;

import com.gslab.pepper.loadgen.BaseLoadGenerator;
import com.gslab.pepper.loadgen.impl.AvroLoadGenerator;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class AvroConfigElement extends ConfigTestElement implements TestBean, LoopIterationListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private String avroSchema;
    private String jsonTemplate;
    private BaseLoadGenerator generator;
    private String placeHolder;

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        if (generator == null) {
            try {
                generator = new AvroLoadGenerator(getAvroSchema(), getJsonTemplate());
            } catch (Exception e) {
                log.error("Failed to create AvroLoadGenerator instance", e);
            }
        }
        JMeterVariables variables = JMeterContextService.getContext().getVariables();
        variables.putObject(placeHolder, generator.nextMessage());
    }

    public String getAvroSchema() {
        return avroSchema;
    }

    public void setAvroSchema(String avroSchema) {
        this.avroSchema = avroSchema;
    }

    public String getJsonTemplate() {
        return jsonTemplate;
    }

    public void setJsonTemplate(String jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }

    public BaseLoadGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(BaseLoadGenerator generator) {
        this.generator = generator;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

}
