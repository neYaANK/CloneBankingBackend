package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.CardPayment;
import me.neyaank.clonebankingbackend.entity.FromCreditPayment;
import me.neyaank.clonebankingbackend.entity.Payment;
import me.neyaank.clonebankingbackend.entity.ToCreditPayment;
import me.neyaank.clonebankingbackend.exception.CardNotFoundException;
import me.neyaank.clonebankingbackend.exception.NotEnoughBalanceException;
import me.neyaank.clonebankingbackend.payload.dto.PaymentDTO;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.CreditRepository;
import me.neyaank.clonebankingbackend.repository.PaymentRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

//TODO: use polymorphism to get sender, receiver sources and credentials of payment
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
    @Autowired
    CreditRepository creditRepository;

    @Override
    public Payment makeCardPayment(String sender, String receiver, double balance) {
        if (!cardRepository.existsByCardNumber(sender) || !cardRepository.existsByCardNumber(receiver))
            throw new CardNotFoundException("Either receiver or sender card is invalid");
        var send = cardRepository.findCardByCardNumber(sender).get();
        var receive = cardRepository.findCardByCardNumber(receiver).get();
        double rate = currencyService.getExchangeRate(send.getCurrency().getName(), receive.getCurrency().getName());

        if (send.getBalance() < balance) throw new NotEnoughBalanceException(balance + " required");
        CardPayment payment = new CardPayment(send, receive, balance, balance * rate, send.getCurrency(), receive.getCurrency(), rate);
        send.setBalance(send.getBalance() - payment.getOutgoingValue());
        receive.setBalance(receive.getBalance() + payment.getIncomingValue());
        payment = paymentRepository.save(payment);
        cardRepository.save(send);
        cardRepository.save(receive);
        return payment;
    }

    @Override
    public Payment makeFromCreditPayment(Long sender_credit_id, String receiver, double balance) {
        var send = creditRepository.findById(sender_credit_id).get();
        var receive = cardRepository.findCardByCardNumber(receiver).get();
        double rate = currencyService.getExchangeRate(send.getCreditType().getCurrency().getName(), receive.getCurrency().getName());
        FromCreditPayment payment = new FromCreditPayment(send, receive, balance, balance * rate, send.getCreditType().getCurrency(), receive.getCurrency(), rate);
        receive.setBalance(receive.getBalance() + payment.getIncomingValue());
        payment = paymentRepository.save(payment);
        cardRepository.save(receive);
        return payment;
    }

    @Override
    public Payment makeToCreditPayment(String sender, Long receiver_credit_id, double balance) {
        var send = cardRepository.findCardByCardNumber(sender).get();
        var receive = creditRepository.findById(receiver_credit_id).get();
        double rate = currencyService.getExchangeRate(send.getCurrency().getName(), receive.getCreditType().getCurrency().getName());

        if (send.getBalance() < balance) throw new NotEnoughBalanceException(balance + " required");

        ToCreditPayment payment = new ToCreditPayment(send, receive, balance, balance * rate, send.getCurrency(), receive.getCreditType().getCurrency(), rate);
        send.setBalance(send.getBalance() - payment.getOutgoingValue());
        receive.setBalance(receive.getBalance() + payment.getIncomingValue());
        payment = paymentRepository.save(payment);
        cardRepository.save(send);
        return payment;
    }

    @Override
    public Set<PaymentDTO> getIncomingPayments(Long card_id) {
        return paymentRepository.findAll().stream().filter(c -> {
            if (c instanceof CardPayment) {
                var payment = (CardPayment) c;
                return payment.getReceiver().getId().equals(card_id);
            } else if (c instanceof ToCreditPayment) {
                return false;
            } else if (c instanceof FromCreditPayment) {
                var payment = (FromCreditPayment) c;
                return payment.getReceiver().getId().equals(card_id);
            }
            return false;
        }).map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
    }

    @Override
    public Set<PaymentDTO> getOutgoingPayments(Long card_id) {
        return paymentRepository.findAll().stream().filter(c -> {
            if (c instanceof CardPayment) {
                var payment = (CardPayment) c;
                return payment.getSender().getId().equals(card_id);
            } else if (c instanceof ToCreditPayment) {
                var payment = (ToCreditPayment) c;
                return payment.getSender().getId().equals(card_id);
            } else if (c instanceof FromCreditPayment) {
                return false;
            }
            return false;
        }).map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
    }
}
