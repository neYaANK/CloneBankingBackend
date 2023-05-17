package me.neyaank.clonebankingbackend.services;

import lombok.extern.slf4j.Slf4j;
import me.neyaank.clonebankingbackend.entity.*;
import me.neyaank.clonebankingbackend.payload.dto.CreditDTO;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.CreditRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//all validation put inside
@Service
@Slf4j
public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;

    @Scheduled(initialDelay = 10, fixedRate = 3600 * 24, timeUnit = TimeUnit.SECONDS)
    public void processCredits() {
        var time = System.currentTimeMillis();
        var credits = creditRepository.findAll().stream().filter(c -> {
            var now = LocalDate.now();
            var last = c.getLastIncreased();
            return ChronoUnit.MONTHS.between(last, now) >= 1;

        }).collect(Collectors.toSet());
        credits.forEach(c -> {
            var mod = c.getCreditType().getPercentPerMonth() / 100;
            c.setBalance(c.getBalance() + c.getBalance() * mod);
            c.setLastIncreased(LocalDate.now());
        });
        creditRepository.saveAll(credits);
        log.info(credits.size() + " credits affected taking " + (System.currentTimeMillis() - time) + " ms");
    }

    @Override
    public Set<CreditDTO> getCredits(Long id) {
        return creditRepository.findAll().stream().filter(c -> c.getUser().getId().equals(id)).map(CreditDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Credit promptCredit(String cardNumber, CreditType creditType, double balance) {
        Card card = cardRepository.findCardByCardNumber(cardNumber).get();
        Credit credit = new Credit();
        credit.setCreditType(creditType);
        credit.setBalance(balance);
        credit.setBaseBalance(balance);
        credit.setUser(card.getUser());
        credit = creditRepository.save(credit);
        var payment = paymentService.makeFromCreditPayment(credit.getId(), cardNumber, balance);
        return credit;
    }

    @Override
    public Optional<Payment> makeCreditPayment(String cardNumber, Long credit_id, double balance) {
        var credit = creditRepository.findById(credit_id).get();
        var payment = paymentService.makeToCreditPayment(cardNumber, credit_id, balance);
        if (payment.isEmpty()) return payment;
        credit = repayCredit(credit_id, payment.get().getIncomingValue());
        return payment;
    }

    @Override
    public boolean isOwner(Long credit_id, Long id) {
        var user = userRepository.findById(id).get();
        var credit = creditRepository.findById(credit_id).get();
        return user.getCredits().contains(credit);
    }

    private Credit repayCredit(Long credit_id, double balance) {
        var credit = creditRepository.findById(credit_id).get();
        if (credit.getBalance() == balance) {
            credit.setCreditStatus(Status.CLOSED);
            credit.setBalance(0);
        } else {
            credit.setBalance(credit.getBalance() - balance);
        }
        return creditRepository.save(credit);
    }
}
