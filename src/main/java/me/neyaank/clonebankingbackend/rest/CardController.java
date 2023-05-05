package me.neyaank.clonebankingbackend.rest;

import jakarta.validation.Valid;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.payload.dto.CardDTO;
import me.neyaank.clonebankingbackend.payload.requests.auth.CodeRequest;
import me.neyaank.clonebankingbackend.payload.requests.card.CreateCardRequest;
import me.neyaank.clonebankingbackend.payload.requests.card.PincodeRequest;
import me.neyaank.clonebankingbackend.payload.responses.card.CardInfoResponse;
import me.neyaank.clonebankingbackend.payload.responses.card.CardsResponse;
import me.neyaank.clonebankingbackend.payload.responses.card.PincodeResponse;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static me.neyaank.clonebankingbackend.security.utils.Util.generateSecureCode;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Value("${neyaank.clonebanking.cardDurationInMonths}")
    private int cardExpiresInMonths;
    @Value("${neyaank.clonebanking.BIN}")
    private String bankIdentificationNumber;

    @Autowired
    UserService userService;

    private Long getMaxCardNumber() {
        var cards = cardRepository.findAll();
        var card = cards.stream().skip(cards.size() - 1).findFirst().get().getId();
        return card;
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardsResponse> cardsByUserId(@PathVariable Long id) {
        var user = userRepository.findById(id).get();
        var cards = user.getCards();
        Set<CardDTO> cardDTOSet = cards.stream().map(c -> new CardDTO(c)).collect(Collectors.toSet());
        return ResponseEntity.ok(new CardsResponse(cardDTOSet));
    }

    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardInfoResponse> cardByUserAndCardId(@PathVariable(name = "id") Long userId, @PathVariable Long cardId) {
        var user = userRepository.findById(userId).get();
        var card = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (card.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new CardInfoResponse(card.get()));
    }

    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/{cardId}/changePinOld", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PincodeResponse> changeCardPinWithOldPin(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody PincodeRequest request) {
        var user = userRepository.findById(userId).get();
        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();
        if (!request.getPincode().equals(cardOptional.get().getPinCode())) return ResponseEntity.badRequest().build();
        var card = cardOptional.get();
        card.setPinCode(generateSecureCode(9999, 4));
        cardRepository.save(card);
        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
    }

    @PreAuthorize("(#userId+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/{cardId}/changePin2FA", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PincodeResponse> changeCardPinWith2FA(@PathVariable(name = "id") Long userId, @PathVariable Long cardId, @Valid @RequestBody CodeRequest request) {
        var user = userRepository.findById(userId).get();
        var cardOptional = user.getCards().stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOptional.isEmpty()) return ResponseEntity.notFound().build();

//        String secretKey = user.getSecret();
//        String realCode = getTOTPCode(secretKey);
//        if (!realCode.equals(request.getCode())) {
//            return ResponseEntity.badRequest().build();
//        }
        var card = cardOptional.get();
        card.setPinCode(generateSecureCode(9999, 4));
        cardRepository.save(card);
        return ResponseEntity.ok(new PincodeResponse(card.getPinCode()));
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createNewCard(@PathVariable Long id, @Valid @RequestBody CreateCardRequest request) {
        var user = userRepository.findById(id).get();
        var card = new Card();
        card.setCurrency(request.getCurrency());
        card.setType(request.getType());
        card.setPaymentSystem(request.getPaymentSystem());
        card.setCardNumber(bankIdentificationNumber + getNextCardNumber());
        card.setCv2(generateSecureCode(999, 3));
        card.setExpireDate(LocalDate.now().withDayOfMonth(1).plusMonths(cardExpiresInMonths));
        card.setPinCode(generateSecureCode(9999, 4));
        card.setUser(user);
        cardRepository.save(card);

        return ResponseEntity.ok("Card is created successfully");
    }
    private String getNextCardNumber() {
        Long maxCardNumber = getMaxCardNumber();
        int cardlength = 10;
        var size = String.valueOf(maxCardNumber).length();
        String cardNumber = "";
        for (int i = 0; i < cardlength - size; i++) cardNumber += "0";
        cardNumber += String.valueOf(maxCardNumber);
        return cardNumber;
    }

}
