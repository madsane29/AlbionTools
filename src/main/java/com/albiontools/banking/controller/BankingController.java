package com.albiontools.banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/banking")
public class BankingController {

	@GetMapping("")
	public String getBankingPage() {
		return "banking";
	}
}
