package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.payload.requests.auth.CodeRequest;
import me.neyaank.clonebankingbackend.payload.requests.card.PincodeRequest;
import me.neyaank.clonebankingbackend.payload.responses.card.CardDTO;
import me.neyaank.clonebankingbackend.payload.responses.card.CardInfoResponse;
import me.neyaank.clonebankingbackend.payload.responses.card.CardsResponse;
import me.neyaank.clonebankingbackend.payload.responses.card.PincodeResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;

import static me.neyaank.clonebankingbackend.security.utils.TOTPUtility.getTOTPCode;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;


    @PreAuthorize("#id == authentication.principal.id&&hasRole(T(me.neyaank.clonebankingbackend.entity.ERole).ROLE_2FA.name())")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardsResponse> cardsByUserId(@PathVariable Long id) {
        var user = userRepository.findById(id).get();
        var cards = user.getCards();
        Set<CardDTO> cardDTOSet = cards.stream().map(c -> new CardDTO(c)).collect(Collectors.toSet());
        return ResponseEntity.ok(new CardsResponse(cardDTOSet));
    }

    @PreAuthorize("#id == authentication.principal.id&&hasRole(T(me.neyaank.clonebankingbackend.entity.ERole).ROLE_2FA.name())")
    @GetMapping(value = "/{id}/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardInfoResponse> cardByUserAndCardId(@PathVariable(name = "id") Long userId, @PathVariable Long cardId) {
        var user = userRepository.findById(userId).get();
        var card = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (card.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new CardInfoResponse(card.get()));
    }

    @PreAuthorize("#id == authentication.principal.id&&hasRole(T(me.neyaank.clonebankingbackend.entity.ERole).ROLE_2FA.name())")
    @PostMapping(value = "/{id}/{cardId}/changePinOld", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PincodeResponse> changeCardPinWithOldPin(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody PincodeRequest request) {
        var user = userRepository.findById(userId).get();
        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();
        if (!request.getPincode().equals(cardOptional.get().getPinCode())) return ResponseEntity.badRequest().build();
        var card = cardOptional.get();
        card.setPinCode(generatePinCode());
        cardRepository.save(card);
        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
    }

    @PreAuthorize("#id == authentication.principal.id&&hasRole(T(me.neyaank.clonebankingbackend.entity.ERole).ROLE_2FA.name())")
    @PostMapping(value = "/{id}/{cardId}/changePin2FA", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PincodeResponse> changeCardPinWith2FA(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody CodeRequest request) {
        var user = userRepository.findById(userId).get();
        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();

        String secretKey = user.getSecret();
        String realCode = getTOTPCode(secretKey);
        if (!realCode.equals(request.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        var card = cardOptional.get();
        card.setPinCode(generatePinCode());
        cardRepository.save(card);
        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
    }

    private String generatePinCode() {
        SecureRandom rand = new SecureRandom();
        String pin = String.valueOf(rand.nextInt(4));
        return pin;
    }
}
