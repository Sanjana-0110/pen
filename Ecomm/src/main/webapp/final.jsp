<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException,java.lang.Math" %>
 
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Order Details</title>
    <style>
    th{
    	background:darkblue;
    	color:white;
    }
    td{
    	background:azure
    }
    
    </style>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
</head>
<body>
    <h1 style="background:darkblue;color:white;">Order ID: ${sessionScope.oid != null ? sessionScope.oid : "No Order ID found"} is Successful!</h1>
    
    <% // Get the oid from the session
    String oid = (String) session.getAttribute("oid");

    double t = 0.0; // Initialize t to avoid compilation errors
    ResultSet rs = null; // Initialize rs to avoid compilation errors
	double tot=0.0;
    // Use oid in your SQL query
    String sqlQuery = "SELECT * FROM ordered_products2003 WHERE oid = ?";
    String s = "SELECT oname, totalprice FROM orders2003 WHERE oid = ?";
    try {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
                "plf_training_admin", "pff123");
        Connection conn2 = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
                "plf_training_admin", "pff123");
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        PreparedStatement p = conn.prepareStatement(s);
        preparedStatement.setString(1, oid);
        p.setString(1, oid);
        ResultSet r = p.executeQuery();
        rs = preparedStatement.executeQuery();
        
        while (r.next()) {
            System.out.println(r.getString("oname"));
            t = r.getDouble("totalprice");
        }

        // Process the result set as needed
    } catch (SQLException e) {
       System.out.println(e);
        // Handle any errors
    }
    %>
    
    <table style="border-collapse:collapse;width:50%">
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>GST</th>
            </tr>
        </thead>
        <tbody>
            <% while (rs != null && rs.next()) { %>
             <tr>
            <td><div style="text-align:center;"><%= rs.getString("pname") %></div></td>
            <td><div style="text-align:center;"><%= rs.getInt("qty") %></div></td>
            <td><div style="text-align:center;"><%= rs.getDouble("price") %></div></td>
            <td><div style="text-align:center;"><%= rs.getDouble("gst") %></div></td>
        </tr>
            <% } %>
        </tbody>
    </table>
 <% ResultSet rs3 = null;

   String s3 = "SELECT orvl_shippingamount FROM OrderValueWiseShippingCharges2003 WHERE ? BETWEEN orvl_from AND orvl_to";
   double shippingAmount = 0.0; // Initialize to avoid compilation errors
   try {
       Connection conn3 = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
               "plf_training_admin", "pff123");
       PreparedStatement ps3 = conn3.prepareStatement(s3);
       ps3.setDouble(1, t); // Set double value
       rs3 = ps3.executeQuery();
       
       while (rs3.next()) {
           System.out.println(rs3.getDouble(1));
           shippingAmount = rs3.getDouble(1); // Store shipping amount
       }

       // Process the result set as needed
   } catch (SQLException e) {
       e.printStackTrace();
       // Handle any errors
   }
%>

<h4>Shipping Amount: <%= String.format("%.2f", shippingAmount) %></h4>
<h3>Total Price (including Shipping): <%= String.format("%.2f", t + shippingAmount) %></h3>

<%= tot=t+shippingAmount%>
<script>
var tp=<%= Math.round(tot*100)%>;
</script>
<button id="payment" onclick="pay()">Pay</button>
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
		          "contact": +917893969459,
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
