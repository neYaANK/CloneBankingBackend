package me.neyaank.clonebankingbackend.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.neyaank.clonebankingbackend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String phoneNumber;
    @JsonIgnore
    private String password;


    public UserDetailsImpl(Long id, String phoneNumber, String password) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }


    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getPhoneNumber(),
                user.getPassword());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
