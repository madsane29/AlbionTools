package com.albiontools.trading.model;



public class Offer {
	private Item tradingItem;
	private String city;
	private int sellPriceMin;
	private String sellPriceMinDate;
	private int buyPriceMax;
	private String buyPriceMaxDate;

	public Offer() {
		super();
	}

	public Offer(Item tradingItem, String city, int sellPriceMin, String sellPriceMinDate, int buyPriceMax,
			String buyPriceMaxDate) {
		super();
		this.tradingItem = tradingItem;
		this.city = city;
		this.sellPriceMin = sellPriceMin;
		this.sellPriceMinDate = sellPriceMinDate;
		this.buyPriceMax = buyPriceMax;
		this.buyPriceMaxDate = buyPriceMaxDate;
	}

	
	
	public Item getTradingItem() {
		return tradingItem;
	}

	public void setTradingItem(Item tradingItem) {
		this.tradingItem = tradingItem;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getSellPriceMin() {
		return sellPriceMin;
	}

	public void setSellPriceMin(int sellPriceMin) {
		this.sellPriceMin = sellPriceMin;
	}

	public String getSellPriceMinDate() {
		return sellPriceMinDate;
	}

	public void setSellPriceMinDate(String sellPriceMinDate) {
		this.sellPriceMinDate = sellPriceMinDate;
	}

	public int getBuyPriceMax() {
		return buyPriceMax;
	}

	public void setBuyPriceMax(int buyPriceMax) {
		this.buyPriceMax = buyPriceMax;
	}

	public String getBuyPriceMaxDate() {
		return buyPriceMaxDate;
	}

	public void setBuyPriceMaxDate(String buyPriceMaxDate) {
		this.buyPriceMaxDate = buyPriceMaxDate;
	}

	@Override
	public String toString() {
		return tradingItem + "city = " + city + " --> sellPriceMin = " + sellPriceMin;
	}

}
