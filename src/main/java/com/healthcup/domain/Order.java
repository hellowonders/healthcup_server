package com.healthcup.domain;

import java.util.List;

public class Order {
	private String order_id;
	private String order_status;
	private List<Product> order_items;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public List<Product> getOrder_items() {
		return order_items;
	}

	public void setOrder_items(List<Product> order_items) {
		this.order_items = order_items;
	}

}
