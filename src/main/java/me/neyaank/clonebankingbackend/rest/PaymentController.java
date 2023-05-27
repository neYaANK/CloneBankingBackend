package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.exception.CardNotFoundException;
import me.neyaank.clonebankingbackend.exception.InvalidOwnerException;
import me.neyaank.clonebankingbackend.payload.requests.payment.PaymentRequest;
import me.neyaank.clonebankingbackend.payload.responses.PaymentsResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.PaymentRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.CardService;
import me.neyaank.clonebankingbackend.services.CurrencyService;
import me.neyaank.clonebankingbackend.services.PaymentService;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    CardService cardService;
    @Autowired
    PaymentService paymentService;

    @PostMapping("/sendPayment")
    public ResponseEntity makePayment(@Valid @RequestBody PaymentRequest request) {
        var id = Long.valueOf(((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getToken().getSubject());
        if (!cardService.isOwner(request.getSenderCardNumber(), id))
            throw new InvalidOwnerException("User is not owner of " + id);
        var payment = paymentService.makeCardPayment(request.getSenderCardNumber(), request.getReceiverCardNumber(), request.getBalance());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping("/{id}/{card_id}")
    public ResponseEntity getCardPayments(@PathVariable Long id, @PathVariable Long card_id) {
        if (!cardRepository.existsById(card_id)) throw new CardNotFoundException(card_id + " not found");
        if (!cardService.isOwner(card_id, id)) throw new InvalidOwnerException("User is not owner of card");

        var card = cardRepository.findCardById(card_id).get();
        var response = new PaymentsResponse(card.getCardNumber(), paymentService.getIncomingPayments(card_id), paymentService.getOutgoingPayments(card_id));
        return ResponseEntity.ok(response);
    }
}
