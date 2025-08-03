package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics="patient", groupId="analytics-service") // connecting kafka consumer to kafka topic, any event sent to topic will be read by consumer; groupId tells broker who the consumer is
    public void consumeEvent(byte[] event){

        try{
            PatientEvent patientEvent = PatientEvent.parseFrom(event); // parsing byte array event into PatientEvent object
            // ...perform any business logic related to analytics-service


            log.info("Received patient event: [PatientID : {}, PatientName : {}, PatientEmail : {}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
        } catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event {}", e.getMessage());
        }
        // using try-catch because the sent event may not be compatible with the java class- an extra property or property maybe left in the message or user error where the proto file wasn't updated
    }
}
