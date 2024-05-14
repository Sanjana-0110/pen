<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    
    <style>
        body {
            background-image: url('bgimg.jpg');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0;
            padding: 0;
            height: 100vh; 
        }
        .cart-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }
        .cart-item {
            border: 2px solid black;
            border-radius: 5px;
            margin: 10px;
            padding: 10px;
            width: calc(20% - 20px); /* 20% width with margin calculation */
            box-sizing: border-box; /* Include padding and border in the width calculation */
            background-color: white;
        }
        .cart-item img {
            width: 100%;
            border-radius: 5px;
            height:250px;
        }
        .cart-item-details {
            margin-top: 10px;
        }
        .remove-btn {
            border: none;
            padding: 5px 10px;
            border-radius: 5px;
            cursor: pointer;
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
        .remove-btn:hover{
        	 background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
        }
        #saveCart,#order{
        border-radius:10px;
    border-width: 2px;
    padding: 10px 20px;
    font-size: 16px;
    background: #4FFFB0;
    color: black; /* Change to your desired text color */
    border-color: rgb(78, 78, 78);
    transition: transform 0.1s ease; 
    transform: translateZ(0); 
        }
        #saveCart:hover,#order:hover{
           	 background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
        }
    </style>
</head>
<body style="background:azure;">
<% String pincode = request.getParameter("pincode"); %>
    <center>
        <h1>Cart</h1>
        <div class="cart-container" id="cartItems">
            <!-- Cart items will be displayed here -->
        </div>
        <br><br>
        <button id="saveCart">Save Cart</button>
        <br><br>
        <form action="CheckoutServlet" method="get">
            <div class="abc">
                <center>
                    <input id="order" type="submit" value="Checkout">
                </center>
            </div>
        </form>
    </center>
<script>

        // Retrieve cart items from local storage
        var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
        var cartContainer = document.getElementById('cartItems');

        // Function to remove all cart items from page and local storage
        function clearCart() {
            cartContainer.innerHTML = ''; // Clear cart items from the page
            cartItems = []; // Clear cartItems array
            localStorage.removeItem('cart'); // Clear cartItems from local storage
        }

        // Display cart items using divs
        function displayCartItems() {
            cartContainer.innerHTML = ''; // Clear existing cart items
            cartItems.forEach(function(item, index) {
                var cartItemDiv = document.createElement('div');
                cartItemDiv.classList.add('cart-item');

                var image = document.createElement('img');
                image.src = item.image;

                var itemDetailsDiv = document.createElement('div');
                itemDetailsDiv.classList.add('cart-item-details');
                itemDetailsDiv.innerHTML = '<p><strong>Product ID:</strong> ' + item.pid + '</p>' +
                                           '<p><strong>Product Name:</strong> ' + item.pname + '</p>' +
                                           '<p><strong>Price:</strong> ' + item.price + '</p>' +
                                           '<p><strong>HSN Code:</strong> ' + item.hsncode + '</p>';

                var removeBtn = document.createElement('button');
                removeBtn.classList.add('remove-btn');
                removeBtn.textContent = 'Remove From Cart';
                removeBtn.addEventListener('click', function() {
                    cartItems.splice(index, 1); // Remove item from cartItems array
                    localStorage.setItem('cart', JSON.stringify(cartItems)); // Update local storage
                    checkDeliverability(pincode);
                    displayCartItems(); 
                   // Refresh cart display
                    if (cartItems.length === 0) clearCart(); // If cart is empty, clear all items
                });

                cartItemDiv.appendChild(image);
                cartItemDiv.appendChild(itemDetailsDiv);
                cartItemDiv.appendChild(removeBtn);
                cartContainer.appendChild(cartItemDiv);
            });
        }

        // Save cart button event listener
        // Inside the script tag in cart.jsp

// Save cart button event listener

document.getElementById('saveCart').addEventListener('click', function() {
    // Check if cart is empty before sending
    if (cartItems.length === 0) {
        alert('Your cart is empty!');
        return;
    }

    // Check stock availability for each item in the cart
    var outOfStockItems = [];
    var promises = [];
    cartItems.forEach(function(item) {
        var promise = $.ajax({
            url: 'StockCheckServlet',
            type: 'GET',
            data: { pid: item.pid }, // Send product ID
            dataType: 'json'
        }).then(function(response) {
            if (response.error) {
                outOfStockItems.push(item.pname);
            }
        });
        promises.push(promise);
    });

    // Once all requests are completed, display the result
    $.when.apply($, promises).then(function() {
        if (outOfStockItems.length > 0) {
            var errorMessage = 'The following items are not available: ' + outOfStockItems.join(', ') +
                '. We will let you know once they are available.';
            alert(errorMessage);
        } else {
            // If all items are available, proceed to save the cart
            saveCartToServer();
        }
    });
});


        // Function to save cart to server
        function saveCartToServer() {
            $.ajax({
                url: 'CartManagerServlet',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({items: cartItems}),
                success: function(response) {
                    console.log('Server response:', response);
                    alert('Cart saved successfully!');
                },
                error: function(xhr, status, error) {
                    console.error('Error while saving cart:', error);
                    alert('Failed to save cart.');
                }
            });
        }

        // Display cart items when the page loads
        displayCartItems();
        // Function to check deliverability based on pincode
        function checkDeliverability(pincode) {
            var cart = JSON.parse(localStorage.getItem('cart')) || [];
            var itemIds = cart.map(function(item) {
                return item.pid;
            });
            console.log(itemIds);
            $.ajax({
                url: 'UnSeriveceableRegionServlet',
                type: 'POST',
                dataType: 'json',
                data: {
                    pincode: pincode,
                    itemIds: itemIds
                },
                success: function(response) {
                    console.log("serviceable regions:", response);

                    var notDeliverableProducts = [];

                    Object.entries(response).forEach(([productId, isDeliverable]) => {
                        if (isDeliverable) {
                            $("#" + productId).prop('disabled', true);
                            notDeliverableProducts.push(productId);
                        }
                    });

                    if (notDeliverableProducts.length > 0) {
                        alert("Products with IDs " + notDeliverableProducts.join(", ") + " are not deliverable to this location.");

                        $(".CartItem").each(function() {
                            var itemId = $(this).attr('.CartItemId');
                            if (notDeliverableProducts.includes(itemId)) {
                                $(this).css('opacity', '0.5');
                            }
                        });
                        $("#saveCart").prop('disabled', true);
                        $("#order").prop('disabled', true);
                    } else {
                        $("#saveCart").prop('disabled', false);
                        $("#order").prop('disabled', false);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error occurred while verifying pin code:", error);
                }
            });
        }
		var pincode;
        $(document).ready(function() {
             pincode = prompt("Please enter your pincode:");
             pincode = pincode.trim();
             checkDeliverability(pincode);
        });
        	

       
    </script>
</body>
</html>