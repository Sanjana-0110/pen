
<%@ page import="java.sql.SQLException,java.lang.Math, java. util.*,model.CartList,model.ProductInfo,model.ProductPriceDetails" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment</title>
<style>
th{
    	background:darkblue;
    	color:white;
    	padding:10px;
    }
    td{
    	background:azure;
    	padding:10px;
    }
#o1,#applyCouponBtn{
    	border-radius:10px;
    border-width: 2px;
    padding: 10px 20px;
    font-size: 16px;
    background: darkblue;
    color: white; /* Change to your desired text color */
    border-color: rgb(78, 78, 78);
    transition: transform 0.1s ease; 
    transform: translateZ(0); 
    }
    #o1:hover,#applyCouponBtn:hover{
    /* Scale up the button on hover */
    background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
    }
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

</head>
<body style="background:azure;">
<div>
<div style="background:darkblue;color:white;height:100px;width:100%;"><center><h1 style="justify-content:center;">Checkout</h1></center></div>
<% // Get the oid from the session
    String oid = (String) session.getAttribute("oid");
    List<ProductInfo> op = (List<ProductInfo>) session.getAttribute("orderedProducts");
    CartList cartList = new CartList();
    List<ProductInfo> pi=(List<ProductInfo>) session.getAttribute("productinfo");
    //cartList=(CartList)request.getAttribute("CartList", cartList);
    List<ProductPriceDetails> lpd =(List<ProductPriceDetails>) session.getAttribute("productlistdetails");
	System.out.println("Entered Checkout JSP");
    double t = 0.0;
    double shippingAmount =0.0;
	double tot=0.0;
    %>
    <div style="display:flex;">
    <div>

    <table style="border-collapse:collapse;margin-top:50px;margin-left:150px;" border="1">
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>GST </th>
            </tr>
        </thead>
        <tbody>
    <% 
    for (ProductInfo product : pi) {
    	System.out.println(product.getGst());
    %>
    <tr>
        <td><div style="text-align:center;"><%= product.getPname() %></div></td>
        <td><div style="text-align:center;"><%= product.getQty() %></div></td>
        <td><div style="text-align:center;"><%= product.getPrice() %></div></td>
        <td><div style="text-align:center;"><%= product.getGst() %></div></td>
    </tr>
    <% 
    } 
    %>
</tbody>

    </table><br><br>
    </div>
    <div>
    <table style="border-collapse:collapse;margin-top:50px;" border="1">
        <thead>
            <tr>
				<th>discount</th>
				<th>product actual price</th>
				<th>product actual gst</th>
				<th>price</th>
				<th>Shipping charge on product</th>
                <th>Shipping charge with GST</th>
            </tr>
        </thead>
        <tbody>
    <%
        for (ProductPriceDetails ppd : lpd) {
        	shippingAmount=ppd.getShippingamount();
        	t=ppd.getTotalprice();
			session.setAttribute("totalcp",t);
       %>
    <tr>
    	<td><div style="text-align:center;"><%= ppd.getDiscount()%></div></td>
    	<td><div style="text-align:center;"><%= ppd.getProductactualprice()%></div></td>
    	<td><div style="text-align:center;"><%= ppd.getProductactualgst()%></div></td>
    	<td><div style="text-align:center;"><%= ppd.getPrice()%></div></td>
        <td><div style="text-align:center;"><%= ppd.getShippingchargeshare() %></div></td>
        <td><div style="text-align:center;"><%= ppd.getShippingchargewithgst() %></div></td>
    </tr>
    <% 
    } 
    %>
</tbody>

    </table>
</div>
</div>
<center>
<h3>Total Price (including Shipping): <%= String.format("%.2f", t ) %></h3>
</center>
<div style="background:azure;margin:0px; height:590px;width:100%;">
<form style="padding:50px;" action="OrderServlet" method="get">
	<center>
    <label style="font-size:22px;font-weight:bold;">Apply Coupon:</label>
    <input type="text" id="c1" name="coupon">
	<input type="button" id="applyCouponBtn" value="Apply"><br><br>
    <input type="submit" id="o1" value="Order">
    </center>
</form>
</div>
</div>
<script>
$(document).ready(function(){
    $("#applyCouponBtn").click(function(){
        var couponCode = $("#c1").val();
        $.ajax({
            type: "GET",
            url: "CouponServlet",
            data: { coupon: couponCode },
            success: function(response){
                alert(response); 
            },
            error: function(){
                alert("Error applying coupon. Please try again.");
            }
        });
    });
});
</script>
</body>
</html>