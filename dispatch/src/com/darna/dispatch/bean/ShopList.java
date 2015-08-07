package com.darna.dispatch.bean;

import java.util.List;

public class ShopList {
	String shop_name, shop_real_price, note, address;
	List<DishList> dishLists;
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getShop_real_price() {
		return shop_real_price;
	}
	public void setShop_real_price(String shop_real_price) {
		this.shop_real_price = shop_real_price;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<DishList> getDishLists() {
		return dishLists;
	}
	public void setDishLists(List<DishList> dishLists) {
		this.dishLists = dishLists;
	}
 }
