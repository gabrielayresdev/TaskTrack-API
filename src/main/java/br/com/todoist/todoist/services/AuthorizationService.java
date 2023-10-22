package br.com.todoist.todoist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.todoist.todoist.user.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.repository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("Username not found");
        return user;
    }
}