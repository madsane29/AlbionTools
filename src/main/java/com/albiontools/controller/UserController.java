package com.albiontools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@GetMapping("/")
	public String getHome() {
		return "index";
	}

	@GetMapping("/trading")
	public String getTradingPage() {
		return "trading";
	}
	
	@GetMapping("/change-password")
	public String getChangePasswordPage() {
		return "relatedToUserAccounts/change-password";
	}
	
	@GetMapping("/verification")
	public String a() {
		return "relatedToUserAccounts/verification";
	}
	
	@GetMapping("/registration")
	public String b() {
		return "relatedToUserAccounts/registration";
	}
	
	@GetMapping("/login")
	public String c() {
		return "relatedToUserAccounts/login";
	}
	
	
	
	
}
