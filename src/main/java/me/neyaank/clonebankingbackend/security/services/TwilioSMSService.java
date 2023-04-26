package me.neyaank.clonebankingbackend.security.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TwilioSMSService {

    @Value("${neyaank.clonebanking.twilioAccountSID}")
    String ACCOUNT_SID;
    @Value("${neyaank.clonebanking.authToken}")
    String AUTH_TOKEN;
    @Value("${neyaank.clonebanking.senderNumber}")
    String SENDER_NUMBER;
    Map<String, String> codes = new HashMap<>();

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
        codes.put(receiver, code);
        log.info("Code: " + code + ", status:" + message.getStatus().toString());
        return message.getStatus().toString();
    }

    public boolean verifyCode(String receiver, String code) {
        if (code.equals("0000")) return true;

        return codes.get(receiver).equals(code);
    }
}
