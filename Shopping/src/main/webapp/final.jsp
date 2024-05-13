<%@ page import="java.sql.SQLException,java.lang.Math, java. util.*,DAL.AllProductsDAL,DAO.AllDAO,model.ProductInfo,model.ProductPriceDetails" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Order Details</title>
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
    #payment{
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
    #payment:hover{
    background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
    }
    </style>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
</head>
<body>

<c:set var="orderedProducts" value="${sessionScope.productinfo}" />
<c:set var="productlistdetails" value="${sessionScope.productlistdetails}" />
<c:set var="totalprice" value="${sessionScope.totalprice}" />
<%
	AllDAO a;
	double cashback=0.0;
	a = new AllProductsDAL();
	double cprice=(Double)session.getAttribute("cprice");
    double totalprice = (Double) session.getAttribute("totalprice");
%>
    <h1 style="background: darkblue; color: white;">Order ID: ${sessionScope.oid != null ? sessionScope.oid : "No Order ID found"} is Successful!</h1>

    <div style="display:flex;" class="container">
        <div class="table-container">
            <table >
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>GST</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productinfo}">
                        <tr>
                            <td>${product.pname}</td>
                            <td>${product.qty}</td>
                            <td>${product.price}</td>
                            <td>${product.gst}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="table-container">
            <table >
                <thead>
                    <tr>
                        <th>Discount</th>
                        <th>Product Actual Price</th>
                        <th>Product Actual GST</th>
                        <th>Price</th>
                        <th>Shipping Charge on Product</th>
                        <th>Shipping Charge with GST</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="ppd" items="${productlistdetails}">
                        <tr>
                            <td>${ppd.discount}</td>
                            <td>${ppd.productactualprice}</td>
                            <td>${ppd.productactualgst}</td>
                            <td>${ppd.price}</td>
                            <td>${ppd.shippingchargeshare}</td>
                            <td>${ppd.shippingchargewithgst}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <h3>Total Price (including Shipping) : ${String.format("%.2f", totalprice)}</h3>
<h3 style="margin-left:190px;">Coupon :  -<%=cprice %></h3>

<%totalprice=totalprice-cprice; %>
<%cashback=a.getCashBack(totalprice); %>
<h3 style="margin-left:180px;">Cashback : -<%=cashback %></h3>
<%totalprice=totalprice-cashback; %>
<p style="margin-left:140px;">---------------------------------------</p>
<h3 style="margin-left:150px;">Grand Total : <%=totalprice%></h3>
<p style="margin-left:140px;">---------------------------------------</p>
<script>
    var tp = <%= Math.round(totalprice * 100) %>;
</script>
<button id="payment" style="margin-left:150px;" onclick="pay()">Pay</button>
<script>
 function pay() {
	  var options = {
		        "key": "rzp_test_RLOEqDIodTh0PZ", // Enter the Key ID generated from the Dashboard
		        "amount": tp,
		        "currency": "INR",
		        "description": "Acme Corp",
		        "image": "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg",
		        "prefill":
		        {
		          "email": "sanjananvsv@gmail.com",
		          "contact": +918464844505,
		        },
		        config: {
		          display: {
		            blocks: {
		              utib: { //name for Axis block
		                name: "Pay using Axis Bank",
		                instruments: [
		                  {
		                    method: "card",
		                    issuers: ["UTIB"]
		                  },
		                  {
		                    method: "netbanking",
		                    banks: ["UTIB"]
		                  },
		                ]
		              },
		              other: { //  name for other block
		                name: "Other Payment modes",
		                instruments: [
		                  {
		                    method: "card",
		                    issuers: ["ICIC"]
		                  },
		                  {
		                    method: 'netbanking',
		                  }
		                ]
		              }
		            },
		            hide: [
		              {
		              method: "upi"
		              }
		            ],
		            sequence: ["block.utib", "block.other"],
		            preferences: {
		              show_default_blocks: false // Should Checkout show its default blocks?
		            }
		          }
		        },
		        "handler": function (response) {
		          alert(response.razorpay_payment_id);
		        },
		        "modal": {
		          "ondismiss": function () {
		            if (confirm("Are you sure, you want to close the form?")) {
		              txt = "You pressed OK!";
		              console.log("Checkout form closed by the user");
		            } else {
		              txt = "You pressed Cancel!";	
		              console.log("Complete the Payment")
		            }
		          }
		        }
		      };
	  
	   var rzp1 = new Razorpay(options);
	console.log(options);
	
  rzp1.open();
  e.preventDefault();
}



</script>

</body>
</html>
