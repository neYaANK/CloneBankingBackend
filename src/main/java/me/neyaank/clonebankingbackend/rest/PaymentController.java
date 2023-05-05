package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.entity.Payment;
import me.neyaank.clonebankingbackend.payload.dto.PaymentDTO;
import me.neyaank.clonebankingbackend.payload.requests.payment.PaymentRequest;
import me.neyaank.clonebankingbackend.payload.responses.PaymentsResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.PaymentRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.CurrencyService;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserService userService;
    @Autowired
    CurrencyService currencyService;

    @PostMapping("/sendPayment")
    public ResponseEntity makePayment(@Valid @RequestBody PaymentRequest request) {
        var user = userService.findUserByToken((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).get();
        var sender = cardRepository.findCardByCardNumber(request.getSenderCardNumber());
        var receiver = cardRepository.findCardByCardNumber(request.getReceiverCardNumber());

        if (sender.isEmpty() || receiver.isEmpty()) return ResponseEntity.notFound().build();
        if (sender.get().getUser().getId() != user.getId())
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Authorized user is not an owner of sending card");
        var cardSender = sender.get();
        var cardReceiver = receiver.get();
        double rate = currencyService.getExchangeRate(sender.get().getCurrency(), receiver.get().getCurrency());

        Payment payment = new Payment(sender.get(), receiver.get(), request.getBalance(), request.getBalance() * rate, sender.get().getCurrency(), receiver.get().getCurrency(), rate);
        payment.setReceiver(receiver.get());
        payment.setSender(sender.get());
        cardSender.setBalance(cardSender.getBalance() - payment.getOutgoingValue());
        cardReceiver.setBalance(cardReceiver.getBalance() + payment.getIncomingValue());
        paymentRepository.save(payment);
        cardRepository.save(cardSender);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping("/{id}/{card_id}")
    public ResponseEntity getCardPayments(@PathVariable Long id, @PathVariable Long card_id) {
        var card = cardRepository.findById(card_id);
        if (card.isEmpty()) return ResponseEntity.notFound().build();
        if (card.get().getUser().getId() != id)
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Wrong user");
        var receiver = paymentRepository.findAll().stream().filter(c -> c.getReceiver().getId() == card_id).map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
        var sender = paymentRepository.findAll().stream().filter(c -> c.getSender().getId() == card_id).map(c -> new PaymentDTO(c)).collect(Collectors.toSet());
        var response = new PaymentsResponse(card.get().getCardNumber(), receiver, sender);
        return ResponseEntity.ok(response);
    }
}
