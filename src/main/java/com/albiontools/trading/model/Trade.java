package com.albiontools.trading.model;

public class Trade implements Comparable<Trade> {

	private Offer fromOffer;
	private Offer toOffer;

	public Trade() {
		super();
	}

	public Trade(Offer fromOffer, Offer toOffer) {
		super();
		this.fromOffer = fromOffer;
		this.toOffer = toOffer;
	}

	@Override
	public int compareTo(Trade trade) {
		return (trade.toOffer.getSellPriceMin() - this.toOffer.getSellPriceMin()) - (trade.fromOffer.getSellPriceMin() - this.fromOffer.getSellPriceMin());
	}

	public Offer getFromOffer() {
		return fromOffer;
	}

	public void setFromOffer(Offer fromOffer) {
		this.fromOffer = fromOffer;
	}

	public Offer getToOffer() {
		return toOffer;
	}

	public void setToOffer(Offer toOffer) {
		this.toOffer = toOffer;
	}
}
	