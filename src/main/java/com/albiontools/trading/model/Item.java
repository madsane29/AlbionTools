package com.albiontools.trading.model;


public class Item {
	private Long id;
	private String itemSpecificID;
	private String name;
	private String tier;
	private Integer enchant;
	
	public Item(String itemSpecificID, String name, String tier, Integer enchantLevel) {
		super();
		this.id = null;
		this.itemSpecificID = itemSpecificID;
		this.name = name;
		this.tier = tier;
		this.enchant = enchantLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemSpecificID() {
		return itemSpecificID;
	}

	public void setItemSpecificID(String itemSpecificID) {
		this.itemSpecificID = itemSpecificID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Integer getEnchant() {
		return enchant;
	}

	public void setEnchant(Integer enchant) {
		this.enchant = enchant;
	}

	
	
	
}
