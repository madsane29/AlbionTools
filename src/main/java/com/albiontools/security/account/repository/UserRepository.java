package com.albiontools.security.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albiontools.security.account.model.ConfirmationToken;
import com.albiontools.security.account.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
	Optional<User> findById(Long id);
	
}
