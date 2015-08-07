package com.darna.dispatch.bean;

import java.util.List;

public class Order {
	String sn, start_time, status, consignee, recipient_phone, amount, extra, sum;
	List<Addr> addrs;
	Addr userAddr;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getRecipient_phone() {
		return recipient_phone;
	}
	public void setRecipient_phone(String recipient_phone) {
		this.recipient_phone = recipient_phone;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public List<Addr> getAddrs() {
		return addrs;
	}
	public void setAddrs(List<Addr> addrs) {
		this.addrs = addrs;
	}
	public Addr getUserAddr() {
		return userAddr;
	}
	public void setUserAddr(Addr userAddr) {
		this.userAddr = userAddr;
	}
	
}
