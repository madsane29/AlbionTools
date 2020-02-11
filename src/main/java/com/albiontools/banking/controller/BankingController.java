package com.albiontools.banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/banking")
public class BankingController {
	
	private static final String bankingHTMLFilesFolder = "banking/";

	@GetMapping("")
	public String getBankingPage() {
		return bankingHTMLFilesFolder + "banking";
	}
}
