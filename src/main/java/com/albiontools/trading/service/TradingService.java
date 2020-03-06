package com.albiontools.trading.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albiontools.AlbionToolsApplication;
import com.albiontools.jsonhandler.JSONFromURL;
import com.albiontools.trading.model.Item;
import com.albiontools.trading.model.Offer;
import com.albiontools.trading.model.Trade;

@Service
public class TradingService {

	@Autowired
	private JSONFromURL JSONHandler;
	
	public List<Trade> getTrades(String fromCity, String toCity, int profitMinimum, int profitMaximum, int auctionTax) {
		List<Trade> trades = new ArrayList<>();
		List<Item> tradingItems = getItemNamesAndIDsFromTXT();
		
		ArrayList<Offer> offers = getOffers(fromCity, toCity, auctionTax, tradingItems);

		fromCity = splitCityName(fromCity);
		toCity = splitCityName(toCity);

		final String fromCityFinal = fromCity;
		final String toCityFinal = toCity;

		List<Offer> fromOffers = offers.stream().filter(e -> fromCityFinal.contains(e.getCity()))
				.collect(Collectors.toList());
		List<Offer> toOffers = offers.stream().filter(e -> toCityFinal.contains(e.getCity()))
				.collect(Collectors.toList());

		for (Offer fromOffer : fromOffers) {
			String fromOfferItemID = fromOffer.getTradingItem().getItemSpecificID();
			List<Offer> toOfferList = toOffers.stream().filter(e -> e.getTradingItem().getItemSpecificID().equals(fromOfferItemID))
					.collect(Collectors.toList());

			for (Offer toOffer : toOfferList) {
				if ((toOffer.getSellPriceMin() - fromOffer.getSellPriceMin() > profitMinimum)
						&& (toOffer.getSellPriceMin() - fromOffer.getSellPriceMin() <= profitMaximum)) {
					trades.add(new Trade(fromOffer, toOffer));
				}
			}
		}

		Collections.sort(trades);
		return trades;
	}

	public ArrayList<Offer> getOffers(String fromCity, String toCity, int auctionTax, List<Item> tradingItems) {
		ArrayList<Offer> offers = new ArrayList<>();

		String firstPartOfUrl = "https://www.albion-online-data.com/api/v2/stats/prices/";
		String secondPartOfUrl = "";
		String thirdPartOfUrl = "?locations=";
		String fourthPartOfUrl = fromCity + "," + toCity;
		String fifthPartOfUrl = "&qualities=0";

		int n = 1;
		for (Item item : tradingItems) {
			secondPartOfUrl += item.getItemSpecificID() + ",";
			n++;

			if (n % 19 == 0 || (item.equals(tradingItems.get(tradingItems.size() - 1)))) {
				secondPartOfUrl = secondPartOfUrl.substring(0, secondPartOfUrl.length() - 1);
				String url = firstPartOfUrl + secondPartOfUrl + thirdPartOfUrl + fourthPartOfUrl + fifthPartOfUrl;

				JSONArray jsonarray = JSONHandler.getJSONArrayOutOfURL(url);
				for (int j = 0; j < jsonarray.length(); j++) {
					JSONObject obj = jsonarray.getJSONObject(j);
					String itemID = obj.getString("item_id");

					Item tempItem = tradingItems.stream().filter(e -> itemID.equals(e.getItemSpecificID())).findAny()
							.orElse(null);
					if (tempItem != null && obj.getInt("sell_price_min") > 0) {
						String city = obj.getString("city");
						int sellPriceMin = obj.getInt("sell_price_min");
						if (toCity.contains(city))
							sellPriceMin = (int) (sellPriceMin * (100 - auctionTax) / 100);
						String sellPriceMinDate = obj.getString("sell_price_min_date");
						int buyPriceMax = obj.getInt("buy_price_max");
						String buyPriceMaxDate = obj.getString("buy_price_max_date");

						offers.add(new Offer(tempItem, city, sellPriceMin, sellPriceMinDate, buyPriceMax,
								buyPriceMaxDate));
					}
				}
				secondPartOfUrl = "";
			}
		}
		return offers;
	}
	
	private String splitCityName(String city) {
		if (city.contains("FortSterling"))
			city = city.substring(0, city.indexOf("Sterling")) + " " + city.substring(city.indexOf("Sterling"));
		return city;
	}
	
	public List<Item> getItemNamesAndIDsFromTXT() {
		InputStream is = AlbionToolsApplication.class.getClassLoader().getResourceAsStream("txt/resourcesID");
		//InputStream is = TransportFromCityToCityApplication.class.getClassLoader().getResourceAsStream("txt/allitemsID");
		
		//@SuppressWarnings("resource")
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String content = scanner.next();

		List<Item> tradingItems = new ArrayList<>();
		List<String> lines = Arrays.asList(content.split("\\n"));
		for (String line : lines) {

			if (isItValidString(line, ':')) {
				String uniqueName = line.substring(line.indexOf(':') + 1, line.lastIndexOf(':')).trim();
				String tier = uniqueName.substring(0, 2);
				
				String itemName = line.substring(line.lastIndexOf(':') + 2).trim();
				

				int enchantLevel = 0;
				if (uniqueName.contains("@")) {
					itemName = itemName + uniqueName.substring(uniqueName.indexOf('@'));
					enchantLevel = Integer.parseInt(itemName.substring(itemName.indexOf('@')+1));
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
			if (count == 2) return true;
		}

		return false;
	}
}
