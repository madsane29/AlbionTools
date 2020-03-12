package com.albiontools.trading.model;

public class AjaxTestModel {
	private String itemName;
	private String fromCity;
	private String toCity;
	private int fromPrice;
	private int toPrice;
	private int profit;
	public AjaxTestModel(String itemName, String fromCity,int fromPrice, String toCity, int toPrice) {
		super();
		this.itemName = itemName;
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.profit = toPrice - fromPrice;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFromCity() {
		return fromCity;
	}
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public int getFromPrice() {
		return fromPrice;
	}
	public void setFromPrice(int fromPrice) {
		this.fromPrice = fromPrice;
	}
	public int getToPrice() {
		return toPrice;
	}
	public void setToPrice(int toPrice) {
		this.toPrice = toPrice;
	}
	public int getProfit() {
		return profit;
	}
	public void setProfit(int profit) {
		this.profit = profit;
	}

	@Override
	public String toString() {
		return "AjaxTestModel [itemName=" + itemName + ", fromCity=" + fromCity + ", toCity=" + toCity + ", fromPrice="
				+ fromPrice + ", toPrice=" + toPrice + ", profit=" + profit + "]";
	}
	
	
	

}
