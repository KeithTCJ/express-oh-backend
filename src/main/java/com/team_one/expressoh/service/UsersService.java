package com.team_one.expressoh.service;

import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// For Admin Use
@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users save(Users users) {
        return usersRepository.save(users);
    }

    public Optional<Users> findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username).orElseThrow();
    }
}