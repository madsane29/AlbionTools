package com.albiontools.trading.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trading")
public class TradingController {

	private static final String tradingHTMLFilesFolder = "trading/";
	
	@GetMapping("")
	public String getTradingPage() {
		return tradingHTMLFilesFolder + "trading";
	}
	
	@GetMapping("/get-data")
	public String returnDataBackToTable(@RequestParam Map<String,String> allRequestParams) {
		 for (Map.Entry<String,String> entry : allRequestParams.entrySet())  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue());
		return "redirect:/";
	}
}
