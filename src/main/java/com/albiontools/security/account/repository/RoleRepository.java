package com.albiontools.security.account.repository;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albiontools.security.account.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	HashSet<Role> findById(long id);
	Role findByName(String name);
}