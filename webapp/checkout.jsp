

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment</title>
<style>
#o1{
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
    #o1:hover{
    /* Scale up the button on hover */
    background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
    }
</style>
</head>
<body>
<div>
<div style="background:darkblue;color:white;height:125px;width:100%;margin:0px;"><center><h1 style="justify-content:center;">Checkout</h1></center></div>
<div style="background:azure;margin:0px; height:590px;width:100%;">
<form style="padding:50px;" action="CheckoutServlet" method="get">
	
	<center>
    <label style="margin:50px;">Choose payment type:</label>
    <select name="paymentType">
        <option value="prepaid">Prepaid cards</option>
        <option value="credit">Credit card</option>
        <option value="debit">Debit card</option>
        <option value="cash">Cash on delivery</option>
    </select><br><br>
    <input type="submit" id="o1" value="Order">
    </center>
</form>
</div>
</div>
</body>
</html>