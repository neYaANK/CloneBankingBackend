package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.exception.CardNotFoundException;
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
        return ResponseEntity.ok(new CardsResponse(cardService.getCardDTOsByUserId(id)));
    }

    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardInfoResponse> cardByUserAndCardId(@PathVariable(name = "id") Long userId, @PathVariable Long cardId) {
        var cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty() || !cardOptional.get().getUser().getId().equals(userId))
            throw new CardNotFoundException(cardId + " not found");
        return ResponseEntity.ok(new CardInfoResponse(cardOptional.get()));
    }
    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createNewCard(@PathVariable Long id, @Valid @RequestBody CreateCardRequest request) {
        var card = cardService.createCard(request.getCurrency(), request.getType(), request.getPaymentSystem(), id);
        return ResponseEntity.ok("Card is created successfully");
    }


}
