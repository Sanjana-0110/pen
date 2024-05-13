package BLL;

import java.text.DecimalFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAL.AllProductsDAL;
import dbcon.DBConnectionManager;
import model.ProductPriceDetails;

public class BussinessLogic {
	ProductPriceDetails pd=new ProductPriceDetails();
	AllProductsDAL a=new AllProductsDAL();
	List<ProductPriceDetails> lpd=new ArrayList<>();
	// Method to find the GST based on the HSN code
	public double findgst(String hsncode) throws SQLException {
		double gst=0.0;
		
		try {
			
			Connection conn = DBConnectionManager.getConnection();

			PreparedStatement stmt = conn.prepareStatement("SELECT gst_percentage FROM hsncode2003 WHERE hsncode=?");
			stmt.setString(1, hsncode);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				gst = rs.getDouble("gst_percentage");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gst;
	}
	public double calculateActualPrice(double priceWithGST, double gstPercentage) {
		return Double.parseDouble(String.format("%.2f", priceWithGST / (1 + (gstPercentage / 100))));
    }

    // Method to calculate total price for an order
	public List<ProductPriceDetails> calculateTotalPrice(double[] pricesWithGST, double[] gstPercentages, int[] quantities,int[]  discount) {
        DecimalFormat df = new DecimalFormat("#.##");
        double[] productActualPriceAfterDiscount=new double[discount.length];
        for(int i=0;i<pricesWithGST.length;i++) {
        	pricesWithGST[i]=(pricesWithGST[i]/quantities[i]);
        	System.out.println("pricesWithGST "+i+":"+pricesWithGST[i]);
        }
        double[] productActualPrice= new double[pricesWithGST.length];
        double[] productActualGST= new double[pricesWithGST.length];
        double[] productDiscount= new double[pricesWithGST.length];
        for(int i=0;i<productActualPrice.length;i++) {
        productActualPrice[i]=pricesWithGST[i] / (1 + (gstPercentages[i] / 100));
    	System.out.println("productActualPrice "+i+":"+productActualPrice[i]);

        }
        for(int i=0;i<productActualGST.length;i++) {
        productActualGST[i] = pricesWithGST[i] - productActualPrice[i];
    	System.out.println("productActualGST "+i+":"+productActualGST[i]);
        }
        for(int i=0;i<productDiscount.length;i++) {
        	System.out.println(discount[i]);
        	productDiscount[i] = (productActualPrice[i] * (discount[i] / 100.0));
    	System.out.println("productDiscount "+i+":"+productDiscount[i]);

        }
        
        for(int i=0;i<productActualPriceAfterDiscount.length;i++) {
        	productActualPriceAfterDiscount[i]=productActualPrice[i] - productDiscount[i];
        }
        
        double[] productActualGSTAfterDiscount=new double[discount.length];
        for(int i=0;i<productActualGSTAfterDiscount.length;i++) {
        	productActualGSTAfterDiscount[i]= (productActualPriceAfterDiscount[i] * productActualGST[i])/ productActualPrice[i];
        	System.out.println("productActualGSTAfterDiscount "+i+":"+productActualGSTAfterDiscount[i]);

        }
        double[] productPriceWithGST =new double[discount.length];
        for(int i=0;i<productPriceWithGST.length;i++) {
        productPriceWithGST[i]=(productActualPriceAfterDiscount[i] + productActualGSTAfterDiscount[i])* quantities[i];
    	System.out.println("productPriceWithGST "+i+":"+productPriceWithGST[i]);

        }
        
        double totalprice=0.0;
        for(int i=0;i<productPriceWithGST.length;i++) {
        	totalprice+=productPriceWithGST[i];
        	System.out.println("totalprice:"+totalprice);
        }
        double shippingCharges = a.getShipping(totalprice);
        System.out.println("shippingCharges:"+shippingCharges);
        
        double[] productShippingChargesShare = new double[pricesWithGST.length];
        for (int i = 0; i < pricesWithGST.length; i++) {
            productShippingChargesShare[i] = (productPriceWithGST[i] / totalprice) * shippingCharges;
            System.out.println("productShippingChargesShare" + i + ":" + productShippingChargesShare[i]);
        }
        
        double[] productShippingGST = new double[pricesWithGST.length];
        for (int i = 0; i < pricesWithGST.length; i++) {
            productShippingGST[i] = ((gstPercentages[i] / 100) * productShippingChargesShare[i])*quantities[i];
            System.out.println("productShippingGST" + i + ":" + productShippingGST[i]);
        }
        
        for (int i = 0; i < pricesWithGST.length; i++) {
            productShippingGST[i] = ((gstPercentages[i] / 100) * productShippingChargesShare[i])*quantities[i];
            System.out.println("productShippingGST" + i + ":" + productShippingGST[i]);
        }
        
        totalprice+=shippingCharges;

        for (int i = 0; i < pricesWithGST.length; i++) {
        	totalprice += productShippingGST[i];
        }
        System.out.println("totalprice:" + totalprice);

        // Create and populate ProductPriceDetails objects
        List<ProductPriceDetails> result = new ArrayList<>();
        for (int i = 0; i < pricesWithGST.length; i++) {
            ProductPriceDetails pd = new ProductPriceDetails();
            pd.setQty(quantities[i]);
            pd.setDiscount(Double.parseDouble(df.format(discount[i])));
            pd.setProductactualprice(Double.parseDouble(df.format(productActualPriceAfterDiscount[i])));
            pd.setPrice(Double.parseDouble(df.format(productPriceWithGST[i])));
            pd.setProductactualgst(Double.parseDouble(df.format(productActualGSTAfterDiscount[i])));
            pd.setShippingamount(Double.parseDouble(df.format(shippingCharges)));
            pd.setShippingchargeshare(Double.parseDouble(df.format(productShippingChargesShare[i])));
            pd.setShippingchargewithgst(Double.parseDouble(df.format(productShippingGST[i])));
            pd.setTotalprice(Double.parseDouble(df.format(totalprice)));
            
            System.out.println("quantity:"+pd.getQty());
            System.out.println("discount:"+pd.getDiscount());
            System.out.println("Productactualprice"+pd.getProductactualprice());
            System.out.println("Price"+pd.getPrice());
            System.out.println("Productactualgst"+pd.getProductactualgst());
            System.out.println("Shippingamount"+pd.getShippingamount());
            System.out.println("Shippingchargeshare"+pd.getShippingchargeshare());
            System.out.println("Shippingchargewithgst"+pd.getShippingchargewithgst());
            System.out.println("total price:"+pd.getTotalprice());
            result.add(pd);
        }

        return result;
    }
   
}


