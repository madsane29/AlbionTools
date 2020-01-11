package com.albiontools.security.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.albiontools.security.account.model.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long>{
	ConfirmationToken findByConfirmationToken(String confirmationToken);
}
