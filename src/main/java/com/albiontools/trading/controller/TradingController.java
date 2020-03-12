package com.albiontools.trading.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.albiontools.trading.service.TradingService;

@Controller
@RequestMapping("/trading")
public class TradingController {
	private static final String tradingHTMLFilesFolder = "trading/";
	
	private static final String ROOT_OF_CLASS = "trading";
	
	
	@Autowired
	private TradingService tradingService;
	
	@GetMapping("")
	public String getTradingPage(Model model) {
		model.addAttribute("profitMinimum", 0);
		model.addAttribute("profitMaximum", 999999);
		model.addAttribute("auctionTax", 2);
		model.addAttribute("toCity", "");
		model.addAttribute("fromCity", "");
		return tradingHTMLFilesFolder + ROOT_OF_CLASS;
	}
	
	@GetMapping("/**")
	public String redirectToTradingPage() {
		return "redirect:/" + ROOT_OF_CLASS;
	}
	
	/*@PostMapping("/get-data")
	public String returnDataBackToTable(//@RequestParam Map<String,String> allRequestParams, 
			Model model, HttpServletRequest request, 
			@RequestParam (value="toCity") String toCity, 
			@RequestParam (value="fromCity") String fromCity,
			@RequestParam (value="auctionTax") Integer auctionTax,
			@RequestParam (value="profitMinimum") Integer profitMinimum,
			@RequestParam (value="profitMaximum") Integer profitMaximum) {
		//for (Map.Entry<String,String> entry : allRequestParams.entrySet()) System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		model.addAttribute("profitMinimum", profitMinimum);
		model.addAttribute("profitMaximum", profitMaximum);
		model.addAttribute("auctionTax", auctionTax);
		model.addAttribute("toCity", toCity);
		model.addAttribute("fromCity", fromCity);
		model.addAttribute("trades", tradingService.getTrades(fromCity, toCity, profitMinimum, profitMaximum, auctionTax));
		
		
		return tradingHTMLFilesFolder + ROOT_OF_CLASS;
	}*/
}
