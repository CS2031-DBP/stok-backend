package com.example.stokapp.twilio;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Value("${twilio.whatsapp.number}")
    private String fromWhatsAppNumber;

    public void sendWhatsAppMessage(String from, String to, String message) {
        String finalMessage = "Message from " + from + ": " + message;
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(fromWhatsAppNumber),
                finalMessage
        ).create();
    }
}
