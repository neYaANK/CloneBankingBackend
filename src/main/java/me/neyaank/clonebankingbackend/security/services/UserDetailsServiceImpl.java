package me.neyaank.clonebankingbackend.security.services;

import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + phone));

        return UserDetailsImpl.build(user);
    }

}
