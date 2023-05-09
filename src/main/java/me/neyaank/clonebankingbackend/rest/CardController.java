package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.payload.requests.card.CreateCardRequest;
import me.neyaank.clonebankingbackend.payload.responses.card.CardInfoResponse;
import me.neyaank.clonebankingbackend.payload.responses.card.CardsResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.CardService;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserService userService;
    @Autowired
    CardService cardService;

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardsResponse> cardsByUserId(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(new CardsResponse(cardService.getCardDTOsByUserId(id)));
    }

    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardInfoResponse> cardByUserAndCardId(@PathVariable(name = "id") Long userId, @PathVariable Long cardId) {
        if (!userRepository.existsById(userId)) return ResponseEntity.status(401).build();
        var cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty() || !cardOptional.get().getUser().getId().equals(userId))
            return ResponseEntity.status(401).build();
        return ResponseEntity.ok(new CardInfoResponse(cardOptional.get()));
    }

//    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
//    @PostMapping(value = "/{id}/{cardId}/changePinOld", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PincodeResponse> changeCardPinWithOldPin(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody PincodeRequest request) {
//        var user = userRepository.findById(userId).get();
//        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
//        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();
//        if (!request.getPincode().equals(cardOptional.get().getPinCode())) return ResponseEntity.badRequest().build();
//        var card = cardOptional.get();
//        card.setPinCode(generateSecureCode(9999, 4));
//        cardRepository.save(card);
//        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
//    }

//    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
//    @PostMapping(value = "/{id}/{cardId}/changePin2FA", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PincodeResponse> changeCardPinWith2FA(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody CodeRequest request) {
//        var user = userRepository.findById(userId).get();
//        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
//        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();
//
////        String secretKey = user.getSecret();
////        String realCode = getTOTPCode(secretKey);
////        if (!realCode.equals(request.getCode())) {
////            return ResponseEntity.badRequest().build();
////        }
//        var card = cardOptional.get();
//        card.setPinCode(generateSecureCode(9999, 4));
//        cardRepository.save(card);
//        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
//    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createNewCard(@PathVariable Long id, @Valid @RequestBody CreateCardRequest request) {
        if (!userRepository.existsById(id)) return ResponseEntity.status(401).build();
        var card = cardService.createCard(request.getCurrency(), request.getType(), request.getPaymentSystem(), id);
        return ResponseEntity.ok("Card is created successfully");
    }


}
