package me.neyaank.clonebankingbackend;

import me.neyaank.clonebankingbackend.entity.*;
import me.neyaank.clonebankingbackend.repository.*;
import me.neyaank.clonebankingbackend.services.CardService;
import me.neyaank.clonebankingbackend.services.CreditService;
import me.neyaank.clonebankingbackend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CreditTypeRepository creditTypeRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CardService cardService;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CreditService creditService;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CardTypeRepository cardTypeRepository;
    @Autowired
    PaymentSystemRepository paymentSystemRepository;
    @Autowired
    StatusRepository statusRepository;

    @Override
    public void run(String... args) {
        currencyRepository.save(new Currency(ECurrency.UAH));
        currencyRepository.save(new Currency(ECurrency.USD));
        currencyRepository.save(new Currency(ECurrency.EUR));
        cardTypeRepository.save(new CardType(ECardType.CREDIT));
        cardTypeRepository.save(new CardType(ECardType.DEBIT));
        paymentSystemRepository.save(new PaymentSystem(EPaymentSystem.VISA));
        paymentSystemRepository.save(new PaymentSystem(EPaymentSystem.MASTERCARD));
        statusRepository.save(new Status(EStatus.CLOSED));
        statusRepository.save(new Status(EStatus.OPEN));

        roleRepository.save(new Role(ERole.NO_2FA));
        roleRepository.save(new Role(ERole.WITH_2FA));
        CreditType ct1 = new CreditType(5, currencyRepository.findByName(ECurrency.UAH));
        ct1 = creditTypeRepository.save(ct1);

        User user = new User();
        user.setName("Name1");
        user.setSurname("Surname1");
        user.setThirdName("Thirdname1");
        user.setBirthday(LocalDate.of(2000, 7, 6));
        user.setPhoneNumber("+380961234567");
        user.setEmail("admin@gmail.com");
        user.setRoles(Set.of(roleRepository.findByName(ERole.WITH_2FA).get()));
        user.setPassword(encoder.encode("testPassword12"));

        User user2 = new User();
        user2.setName("Name2");
        user2.setSurname("Surname2");
        user2.setThirdName("Thirdname2");
        user2.setBirthday(LocalDate.of(1980, 9, 1));
        user2.setPhoneNumber("+380960000000");
        user2.setEmail("notadmin@gmail.com");
        user2.setRoles(Set.of(roleRepository.findByName(ERole.WITH_2FA).get()));
        user2.setPassword(encoder.encode("testPassword12"));
        user = userRepository.save(user);
        user2 = userRepository.save(user2);

        var card1 = cardService.createCard(ECurrency.UAH, ECardType.DEBIT, EPaymentSystem.MASTERCARD, user.getId());
        card1.setBalance(500);
        var card2 = cardService.createCard(ECurrency.UAH, ECardType.DEBIT, EPaymentSystem.MASTERCARD, user2.getId());
        card2.setBalance(150);

        card1 = cardRepository.save(card1);
        card2 = cardRepository.save(card2);

        var payment1 = paymentService.makeCardPayment(card1.getCardNumber(), card2.getCardNumber(), 100);


        var credit1 = creditService.promptCredit(card1.getCardNumber(), ct1, 10000);
        credit1.setLastIncreased(LocalDate.now().minusMonths(2));
        credit1.setIssuedAt(LocalDate.now().minusMonths(3));
        creditRepository.save(credit1);
        creditService.makeCreditPayment(card1.getCardNumber(), credit1.getId(), 500);
    }
}
