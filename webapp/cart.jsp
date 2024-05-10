
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <style>
    
    table {
                   
                    width: 70%;
                }
                th{
                    background-color:darkblue;
                    padding: 15px;
                    text-align: center;
                    color:white;
                }
                td{
                	 background-color:azure;
                    padding: 15px;
                    text-align: center;
                    color:black;
                }
                .remove-from-cart{
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
                #saveCart,#c1{
                	border-radius:10px;
   					border-width: 2px;
    				padding: 10px 20px;
    				font-size: 16px;
    				background: lightgreen;
    				color: black; /* Change to your desired text color */
    				border-color: rgb(78, 78, 78);
    				transition: transform 0.1s ease; 
    				transform: translateZ(0);
                }
                .remove-from-cart:hover,#saveCart:hover,#c1:hover{
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
   
        <center>
         <h1>Cart</h1>
    <table>

        <thead>
            <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Price</th>
                <th>HSN Code</th>
                <th>Image</th>
                <th>RemoveFromCart</th>
            </tr>
        </thead>
        <tbody id="cartItems">
            <!-- Cart items will be displayed here -->
        </tbody>
    </table><br><br>
    <button id="saveCart">Save Cart</button><br><br>
    
    <a href="checkout.jsp"><button id="c1"> Click Here For Payments</button></a>
    
</center>
    <script>
        // Retrieve cart items from local storage
        var cartItems = JSON.parse(localStorage.getItem('cart')) || [];

        // Display cart items in the table
        var cartTable = document.getElementById('cartItems');
        cartItems.forEach(function(item, index) {
            var row = '<tr>' +
                      '<td name="pid">' + item.pid + '</td>' +
                      '<td name="pname">' + item.pname + '</td>' +
                      '<td name="price">' + item.price + '</td>' +
                      '<td name="hsncode">' + item.hsncode + '</td>' +
                      '<td><img style="height:150px;width:150px;border-top-left-radius:5px;border-top-right-radius:5px;border-bottom-left-radius:5px;border-bottom-right-radius:5px;" src="' + item.image + '" alt="Product Image" style="height:100px;width:100px;"></td>' +
                      '<td><button class="remove-from-cart" data-index="' + index + '">Remove From Cart</button></td>' +
                      '</tr>';
            cartTable.innerHTML += row;
        });
        
		

        document.querySelectorAll('.remove-from-cart').forEach(function(button) {
            button.addEventListener('click', function() {
                var index = parseInt(this.getAttribute('data-index'));
                cartItems.splice(index, 1); // Remove item from cartItems array
                localStorage.setItem('cart', JSON.stringify(cartItems)); // Update local storage
                this.closest('tr').remove(); // Remove the table row
            });
        });

    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(document).ready(function() {
    $('#saveCart').on('click', function() {
        // Fetch the cart data from localStorage
        var cartItems = JSON.parse(localStorage.getItem('cart')) || [];

        // Check if cart is empty before sending
        if (cartItems.length === 0) { // Corrected Line: Check length directly on cartItems
            alert('Your cart is empty!');
            return; // Stop the function if the cart is empty
        }

        $.ajax({
            url: 'CartManagerServlet',  // Make sure this path is correct
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({items: cartItems}), // Correctly sending the array wrapped in an object
            success: function(response) {
                console.log('Server response:', response);
                alert('Cart saved successfully!');
            },
            error: function(xhr, status, error) {
                console.error('Error while saving cart:', error);
                alert('Failed to save cart.');
            }
        });
    });
});


</script>
    
</body>
</html>
