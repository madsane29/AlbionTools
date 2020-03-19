package com.albiontools.trading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.albiontools.trading.model.TradingTableObject;
import com.albiontools.trading.service.TradingService;

@RestController
@RequestMapping("/trading")
public class TradingRestController {
	@Autowired
	private TradingService tradingService;
	
	@PostMapping("/data") 
	public /*@ResponseBody*/ List<TradingTableObject> getData(@RequestBody String json) {
		return tradingService.getTradesJSONObjStringParameter(json);
	}

}
