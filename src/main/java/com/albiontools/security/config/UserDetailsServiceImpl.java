package com.albiontools.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.albiontools.security.account.model.User;
import com.albiontools.security.account.repository.UserRepository;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
  
    @Autowired
    private UserRepository userRepository;

    
    @Override
    public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
  
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException(
              "No user found with username: "+ username);
        }
        
        return new UserPrincipal(user);
    }
}