package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Payment;
import me.neyaank.clonebankingbackend.payload.dto.PaymentDTO;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.PaymentRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CurrencyService currencyService;

    @Override
    public Payment makePayment(String sender, String receiver, double balance) {
        var send = cardRepository.findCardByCardNumber(sender).get();
        var receive = cardRepository.findCardByCardNumber(receiver).get();
        double rate = currencyService.getExchangeRate(send.getCurrency(), receive.getCurrency());

        Payment payment = new Payment(send, receive, balance, balance * rate, send.getCurrency(), receive.getCurrency(), rate);
        payment.setReceiver(receive);
        payment.setSender(send);
        send.setBalance(send.getBalance() - payment.getOutgoingValue());
        receive.setBalance(receive.getBalance() + payment.getIncomingValue());
        payment = paymentRepository.save(payment);
        cardRepository.save(send);
        return payment;
    }

    @Override
    public Set<PaymentDTO> getIncomingPayments(Long card_id) {
        var card = cardRepository.findById(card_id).get();
        return card.getIncomingPayments().stream().map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
    }

    @Override
    public Set<PaymentDTO> getOutgoingPayments(Long card_id) {
        var card = cardRepository.findById(card_id).get();
        return card.getOutgoingPayments().stream().map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
    }
}
