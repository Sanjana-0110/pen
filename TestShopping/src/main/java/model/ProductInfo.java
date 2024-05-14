package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductInfo {
	private int pid;
	private String pname;
	private double price;
	private String hsncode;
	private int pcid;
	private int qty;
	private double gst;

	public ProductInfo() {}
	public ProductInfo(int pid, String pname, double price, String hsncode, int pcid, int qty, double gst) {
		this.pid = pid;
		this.pname = pname;
		this.price = price;
		this.hsncode = hsncode;
		this.pcid = pcid;
		this.qty = qty;
		this.gst = gst;
	}

	// Getters and setters for all fields
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHsncode() {
		return hsncode;
	}

	public void setHsncode(String hsncode) {
		this.hsncode = hsncode;
	}

	public int getPcid() {
		return pcid;
	}

	public void setPcid(int pcid) {
		this.pcid = pcid;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	// Method to increment the quantity
	public void incrementQuantity() {
		this.qty += 1;
	}

	// Method to calculate the total price
	

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

}
