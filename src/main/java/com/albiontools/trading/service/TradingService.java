package com.albiontools.trading.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albiontools.AlbionToolsApplication;
import com.albiontools.jsonhandler.JSONFromURL;
import com.albiontools.trading.model.TradingTableObject;
import com.albiontools.trading.model.Item;
import com.albiontools.trading.model.Offer;
import com.albiontools.trading.model.Trade;

@Service
public class TradingService {

	@Autowired
	private JSONFromURL JSONHandler;

	public List<TradingTableObject> getTradesJSONObjStringParameter(String jsonObj) {

		JSONObject jsonObject = new JSONObject(jsonObj);
		String fromCity = makeCityArrayIntoAString(jsonObject.getJSONArray("fromCity"));
		String toCity = makeCityArrayIntoAString(jsonObject.getJSONArray("toCity"));
		int profitMinimum = jsonObject.getInt("profitMinimum");
		int profitMaximum = jsonObject.getInt("profitMaximum");
		int auctionTax = jsonObject.getInt("auctionTax");

		List<Trade> tradeList = getTrades(fromCity, toCity, profitMinimum, profitMaximum, auctionTax);

		List<TradingTableObject> tradingTableList = tradeList.stream()
				.filter(trade -> !(trade.getFromOffer().getCity().equals(trade.getToOffer().getCity())))
				.map(trade -> 
					new TradingTableObject(
						trade.getFromOffer().getTradingItem().getName(),
						trade.getFromOffer().getCity(), 
						trade.getFromOffer().getSellPriceMin(),
						trade.getToOffer().getCity(), 
						trade.getToOffer().getSellPriceMin()))
				.collect(Collectors.toList());

		return tradingTableList;
	}

	public List<Trade> getTrades(String fromCity, String toCity, int profitMinimum, int profitMaximum, int auctionTax) {
		List<Trade> trades = new ArrayList<>();

		List<Item> tradingItems = getItemNamesAndIDsFromTXT();

		ArrayList<Offer> offers = getOffers(fromCity, toCity, auctionTax, tradingItems);

		final String fromCityFinal = fromCity;
		final String toCityFinal = toCity;

		List<Offer> fromOffers = offers.stream().filter(e -> fromCityFinal.contains(e.getCity()))
				.collect(Collectors.toList());
		List<Offer> toOffers = offers.stream().filter(e -> toCityFinal.contains(e.getCity()))
				.collect(Collectors.toList());

		for (Offer fromOffer : fromOffers) {
			String fromOfferItemID = fromOffer.getTradingItem().getItemSpecificID();
			List<Offer> toOfferList = toOffers.stream()
					.filter(e -> e.getTradingItem().getItemSpecificID().equals(fromOfferItemID))
					.peek(e -> e.setSellPriceMin(e.getSellPriceMin() * (100 - auctionTax) / 100))
					.collect(Collectors.toList());

			for (Offer toOffer : toOfferList) {
				if (isItInRange(toOffer.getSellPriceMin(), fromOffer.getSellPriceMin(), profitMinimum,
						profitMaximum)) {
					trades.add(new Trade(fromOffer, toOffer));
				}
			}
		}

		Collections.sort(trades);

		return trades;
	}

	private ArrayList<Offer> getOffers(String fromCity, String toCity, int auctionTax, List<Item> tradingItems) {
		ArrayList<Offer> offers = new ArrayList<>();

		String firstPartOfUrl = "https://www.albion-online-data.com/api/v2/stats/prices/";
		String secondPartOfUrl = "";
		String thirdPartOfUrl = "?locations=" + buildLocationURLParameter(fromCity, toCity);
		String fourthPartOfUrl = "&qualities=0";

		for (Item item : tradingItems) {
			secondPartOfUrl += item.getItemSpecificID() + ",";
			int sizeOfFinalUrl = firstPartOfUrl.length() + thirdPartOfUrl.length() + fourthPartOfUrl.length()
					+ secondPartOfUrl.length();

			if (sizeOfFinalUrl > 1900 || (item.equals(tradingItems.get(tradingItems.size() - 1)))) {

				secondPartOfUrl = removeLastCharacter(secondPartOfUrl);
				String url = firstPartOfUrl + secondPartOfUrl + thirdPartOfUrl + fourthPartOfUrl;

				JSONArray jsonarray = JSONHandler.getJSONArrayOutOfURL(url);
				for (int i = 0; i < jsonarray.length(); i++) {

					JSONObject obj = jsonarray.getJSONObject(i);
					String itemID = obj.getString("item_id");

					Item tempItem = tradingItems.stream().filter(e -> itemID.equals(e.getItemSpecificID())).findAny()
							.orElse(null);
					if (tempItem != null && obj.getInt("sell_price_min") > 0) {
						String city = obj.getString("city");
						String buyPriceMaxDate = obj.getString("buy_price_max_date");
						String sellPriceMinDate = obj.getString("sell_price_min_date");

						int sellPriceMin = obj.getInt("sell_price_min");
						int buyPriceMax = obj.getInt("buy_price_max");

						offers.add(new Offer(tempItem, city, sellPriceMin, sellPriceMinDate, buyPriceMax,
								buyPriceMaxDate));

					}
				}
				secondPartOfUrl = "";
			}
		}

		return offers;
	}

	private List<Item> getItemNamesAndIDsFromTXT() {
		List<Item> tradingItems = new ArrayList<>();
		// InputStream inputStream =
		// AlbionToolsApplication.class.getClassLoader().getResourceAsStream("txt/resourcesID");
		InputStream inputStream = AlbionToolsApplication.class.getClassLoader()
				.getResourceAsStream("txt/resourcesID_all");

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
		String content = scanner.next();

		List<String> lines = Arrays.asList(content.split("\\n"));
		for (String line : lines) {

			if (isItValidString(line, ':')) {
				String uniqueName = line.substring(line.indexOf(':') + 1, line.lastIndexOf(':')).trim();
				String tier = uniqueName.substring(0, 2);

				String itemName = line.substring(line.lastIndexOf(':') + 2).trim();

				int enchantLevel = 0;
				if (uniqueName.contains("@")) {
					itemName = itemName + uniqueName.substring(uniqueName.indexOf('@'));
					enchantLevel = Integer.parseInt(itemName.substring(itemName.indexOf('@') + 1));
				} else {
					itemName = itemName + "@" + enchantLevel;
				}
				tradingItems.add(new Item(uniqueName, itemName, tier, enchantLevel));
			}
		}

		return tradingItems;
	}

	private boolean isItValidString(String line, char ch) {
		int count = 0;

		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ch)
				count++;

			if (count == 2)
				return true;

		}
		return false;
	}

	private boolean isItInRange(int to, int from, int min, int max) {
		int price = to - from;
		
		return ((price >= min) && (price <= max));
	}

	private String removeWhiteSpaces(String str) {
		return str.replaceAll(" ", "");
	}

	private String removeLastCharacter(String str) {
		return str.substring(0, str.length() - 1);
	}

	private String makeCityArrayIntoAString(JSONArray arr) {
		String str = "";
		for (int i = 0; i < arr.length(); i++) {
			str += arr.getString(i) + ",";
		}
		str = removeLastCharacter(str);

		return str;
	}

	private String buildLocationURLParameter(String fromCity, String toCity) {
		String str = "";
		fromCity = removeWhiteSpaces(fromCity);
		toCity = removeWhiteSpaces(toCity);

		Set<String> cities = new HashSet<>();

		for (String s : fromCity.split(",")) {
			cities.add(s);
		}
		for (String s : toCity.split(",")) {
			cities.add(s);
		}

		for (String s : cities) {
			str += s + ",";
		}

		str = removeLastCharacter(str);
		return str;
	}

}
