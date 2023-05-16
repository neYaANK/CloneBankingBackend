package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.CardType;
import me.neyaank.clonebankingbackend.entity.Currency;
import me.neyaank.clonebankingbackend.entity.PaymentSystem;
import me.neyaank.clonebankingbackend.payload.dto.CardDTO;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static me.neyaank.clonebankingbackend.security.utils.Util.generateSecureCode;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;
    @Value("${neyaank.clonebanking.cardDurationInMonths}")
    private int cardExpiresInMonths;
    @Value("${neyaank.clonebanking.BIN}")
    private String bankIdentificationNumber;

    private Long getMaxCardNumber() {
        var cards = cardRepository.findAll();
        Long num;
        if(cards.size()>0) {
            num = cards.stream().skip(cards.size()-1).findFirst().get().getId();
        }else num = 0L;

        return num;
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

    @Override
    public boolean isOwner(String card, Long id) {
        var card_local = cardRepository.findCardByCardNumber(card).get();
        return Objects.equals(card_local.getUser().getId(), id);
    }

    @Override
    public boolean isOwner(Long card_id, Long id) {
        var card_local = cardRepository.findCardById(id).get();
        return Objects.equals(card_local.getUser().getId(), id);
    }

    @Override
    public Set<CardDTO> getCardDTOsByUserId(Long id) {
        return userRepository.findById(id).get().getCards().stream().map(c -> new CardDTO(c)).collect(Collectors.toSet());
    }

    @Override
    public Card createCard(Currency currency, CardType cardType, PaymentSystem paymentSystem, Long userId) {
        var user = userRepository.findById(userId).get();
        var card = new Card();
        card.setCurrency(currency);
        card.setType(cardType);
        card.setPaymentSystem(paymentSystem);
        card.setCardNumber(bankIdentificationNumber + getNextCardNumber());
        card.setCv2(generateSecureCode(999, 3));
        card.setExpireDate(LocalDate.now().withDayOfMonth(1).plusMonths(cardExpiresInMonths));
        //card.setPinCode(generateSecureCode(9999, 4));
        card.setUser(user);
        card.setBalance(0);
        card = cardRepository.save(card);
        return card;
    }
}
