package me.neyaank.clonebankingbackend.security.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService {

    @Value("${neyaank.clonebanking.twilioAccountSID}")
    String ACCOUNT_SID;
    @Value("${neyaank.clonebanking.authToken}")
    String AUTH_TOKEN;
    @Value("${neyaank.clonebanking.senderNumber}")
    String SENDER_NUMBER;

    @PostConstruct
    public void setup() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public String sendCode(String receiver, String code) {
        String msg = "Your Clonebanking code: " + code;
        Message message = Message.creator(
                new PhoneNumber(receiver),
                new PhoneNumber(SENDER_NUMBER),
                code
        ).create();
        return message.getStatus().toString();
    }
}
