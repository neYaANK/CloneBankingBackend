package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.exception.CardNotFoundException;
import me.neyaank.clonebankingbackend.exception.InvalidOwnerException;
import me.neyaank.clonebankingbackend.payload.requests.credit.CreditRepayRequest;
import me.neyaank.clonebankingbackend.payload.requests.credit.CreditRequest;
import me.neyaank.clonebankingbackend.payload.responses.CreditsResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.CreditTypeRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.CardService;
import me.neyaank.clonebankingbackend.services.CreditService;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit")
public class CreditController {
    @Autowired
    UserService userService;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CreditTypeRepository creditTypeRepository;
    @Autowired
    CreditService creditService;
    @Autowired
    CardService cardService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    public ResponseEntity<CreditsResponse> getCredits(@PathVariable Long id) {
        return ResponseEntity.ok(new CreditsResponse(creditService.getCredits(id)));
    }

    @PostMapping("/request")
    public ResponseEntity promptCredit(@Valid @RequestBody CreditRequest request) {
        var user = userService.findUserByToken((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).get();
        if (!cardRepository.existsByCardNumber(request.getCardNumber()))
            throw new CardNotFoundException(request.getCardNumber() + " not found");
        var card = cardRepository.findCardByCardNumber(request.getCardNumber()).get();
        var creditType = creditTypeRepository.findById(request.getCreditTypeId());
        if (!creditType.isPresent()) throw new RuntimeException("Card type is invalid");
        if (!cardService.isOwner(card.getId(), user.getId())) throw new InvalidOwnerException("Not an owner");
        creditService.promptCredit(card.getCardNumber(), creditType.get(), request.getValue());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#user_id+'') == authentication.getToken().getSubject()")
    @PostMapping("/{user_id}/{credit_id}/repay")
    public ResponseEntity repayCredit(@PathVariable Long user_id, @PathVariable Long credit_id, @Valid @RequestBody CreditRepayRequest request) {
        if (!creditService.isOwner(credit_id, user_id)) throw new InvalidOwnerException("User is not owner of credit");
        if (!cardService.isOwner(request.getCardNumber(), user_id))
            throw new InvalidOwnerException("User is not owner of card");
        var payment = creditService.makeCreditPayment(request.getCardNumber(), credit_id, request.getValue());
        return ResponseEntity.ok().build();
    }
}
