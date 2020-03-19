insert into roles (name) values ('ROLE_ADMIN')
insert into roles (name) values ('ROLE_USER')


insert into users (username, password, email, is_account_non_locked, is_account_non_expired, is_enabled, is_credentials_non_expired, token_id) values ('admin', '$2a$10$spu0oUURXlNX6veWtuHP5.bC6FjWrDsBAimM50emGblkLZuGOYDCC', 'admin@gmail.com', true, true, true, true, null)
insert into users (username, password, email, is_account_non_locked, is_account_non_expired, is_enabled, is_credentials_non_expired, token_id) values ('user', '$2a$10$dT6fA7sMnpku4SG.bmU3..dGpXYuJ8Jhm1q5gu5w9LmRR1W3i/tHi', 'user@gmail.com', true, true, true, true, null)

		
insert into users_roles(users_id, roles_id) values (1, 1)
insert into users_roles(users_id, roles_id) values (1, 2)
insert into users_roles(users_id, roles_id) values (2, 2)
