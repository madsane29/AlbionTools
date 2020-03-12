package com.albiontools.trading.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.albiontools.trading.model.AjaxTestModel;
import com.albiontools.trading.model.Trade;
import com.albiontools.trading.service.TradingService;

@RestController
public class TradingRestController {
	@Autowired
	private TradingService tradingService;
	
	@PostMapping("/trading/data") 
	public @ResponseBody List<AjaxTestModel> getData(@RequestBody String json) {
		return tradingService.getTradesAsAjaxTestModelList(json);
	}

}
