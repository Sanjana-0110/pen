package model;

public class ProductPriceDetails {

	private double shippingchargeshare;
	private double shippingchargewithgst;
	private double shippingamount;
	private double totalprice;
	private double gstinprice;
	private double qty;
	private double discount;
	private double productactualprice;
	private double productactualgst;
	private double price;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getProductactualprice() {
		return productactualprice;
	}
	public void setProductactualprice(double productactualprice) {
		this.productactualprice = productactualprice;
	}
	public double getProductactualgst() {
		return productactualgst;
	}
	public void setProductactualgst(double productactualgst) {
		this.productactualgst = productactualgst;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getGstinprice() {
		return gstinprice;
	}
	public void setGstinprice(double gstinprice) {
		this.gstinprice = gstinprice;
	}
	public double getShippingchargewithgst() {
		return shippingchargewithgst;
	}
	public void setShippingchargewithgst(double shippingchargewithgst) {
		this.shippingchargewithgst = shippingchargewithgst;
	}
	

	public double getShippingchargeshare() {
		return shippingchargeshare;
	}
	public void setShippingchargeshare(double shippingchargeshare) {
		this.shippingchargeshare = shippingchargeshare;
	}
	public double getShippingamount() {
		return shippingamount;
	}
	public void setShippingamount(double shippingamount) {
		this.shippingamount = shippingamount;
	}

	public double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}
}
