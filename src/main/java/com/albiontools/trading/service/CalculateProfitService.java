package com.albiontools.trading.service;


import org.springframework.stereotype.Service;

@Service("calculateProfitService")
public class CalculateProfitService {

	public int calculateProfit(int toPrice, int fromPrice) {
		return toPrice - fromPrice;
	}
}
