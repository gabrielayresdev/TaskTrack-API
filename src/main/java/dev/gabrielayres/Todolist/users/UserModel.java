package dev.gabrielayres.Todolist.users;


import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tb_users")
public class UserModel implements UserDetails {

    public UserModel(String username, String name, String password, String telephone, List<String> groups , UserRoles role) {
        this.username = username;
        this.name = name;
        this.password = password;
        String stringTel = telephone.replaceAll("\\D", "");
        try {
            long longTel = Long.parseLong((stringTel));
            this.telephone = longTel;
        } catch (NumberFormatException e) {
            System.err.println("Invalid input string");
        }
        this.groups = groups;
        this.role = role;
    }


    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String username;
    private String name;
    private String password;
    private long telephone;
    private UserRoles role;
    private List<String> groups;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRoles.ADMIN) return List.of(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
}
