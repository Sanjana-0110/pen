<%@ page import="model.Product"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart Store</title>
    <style>
    .add-to-cart{
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
    .add-to-cart:hover{
    /* Scale up the button on hover */
    background:#FFFACD;
    border-width: 1px;
    font-weight: normal;
    font-size: 16px;
    transform: scale(1.07);
    color: black;
    }
        /* Your CSS styles */
        .product-container {

            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            background-color:azure;
        }
        .product {
            width: 200px;
            margin: 10px;
            padding: 10px;
            border: 2px solid rgba(255, 255, 255, 0.5); /* Transparent white border */
            border-radius: 5px;
            text-align: center;
            background-color:white;
        }
        .product img {
            width: 150px;
            height: 150px;
            border-radius: 5px;
        }
        .registerandcart {
            position: absolute;
            top: 10px;
            right: 10px;
            text-align: right;
        }
        body {
            background-image: url('bgimg.jpg'); /* Replace 'bgimg.jpg' with the path to your image */
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0; /* Reset default body margin */
            padding: 0; /* Reset default body padding */
            min-height: 100vh; /* Ensure the background covers the entire viewport */
            position: relative; /* Ensure correct stacking context for child elements */
        }
        #login{
        	border-radius:10px;
    border-width: 2px;
    padding: 10px 20px;
    font-size: 16px;
    background: white;
    color: black; /* Change to your desired text color */
    border-color: rgb(78, 78, 78);
    transition: transform 0.1s ease; 
    transform: translateZ(0); 
        }
        #login:hover{
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
<div style="background-color:darkblue;">
<div><
<center><h1 style="color:white">Cart Store</h1></center>
</div>
<div class="registerandcart">
    <button id="login">Sign in/Register</button><br><br>
    <a id="cartButton" href="cart.jsp">
        <img style="height:50px;width:50px" src="cart.png"/>
    </a>
</div>

<label style="color:white">Category:</label>
<select id="category" style="width:150px;" required>
    <option value="">Select your category</option>
</select>
<label style="color:white">Sort:</label>
<select id="sort" style="width:150px;" required>
    <option value="">Select your Price</option>
    <option value="asc">Price: Low to High</option>
    <option value="desc">Price: High to Low</option>
</select><br><br>
</div>
<div class="product-container">
    <!-- Products will be displayed here -->
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    document.getElementById("login").addEventListener("click", function() {
        window.location.href = "login.jsp";
    });

    $(document).ready(function () {
        var displayedProducts;

        function fetchAndDisplayAllProducts() {
            $.ajax({
                type: 'GET',
                url: 'AllProductServlet',
                dataType: 'json',
                success: function (response) {
                    displayedProducts = response;
                    displayProducts(response);
                },
                error: function () {
                    console.error('Error occurred while fetching products');
                }
            });
        }

        function fetchAndDisplayProductsByCategory(categoryId) {
            $.ajax({
                type: 'GET',
                url: 'products',
                data: {categoryId: categoryId},
                dataType: 'json',
                success: function (response) {
                    displayedProducts = response;
                    displayProducts(response);
                },
                error: function () {
                    console.error('Error occurred while fetching products');
                }
            });
        }

        function displayProducts(products) {
            $('.product-container').empty();
            $.each(products, function(index, product) {
                var productDiv = '<div class="product">' +
                    '<img src="' + product.image + '" alt="Product Image">' +
                    '<p>Product ID: ' + product.pid + '</p>' +
                    '<p>Product Name: ' + product.pname + '</p>' +
                    '<p>Price: ' + product.price + '</p>' +
                    '<p>HSN Code: ' + product.hsncode + '</p>' +
                    '<button class="add-to-cart" data-product=\'' + JSON.stringify(product) + '\'>Add to Cart</button>' +
                    '</div>';
                $('.product-container').append(productDiv);
            });
        }

        fetchAndDisplayAllProducts();

        $.ajax({
            type: 'GET',
            url: 'cat',
            dataType: 'html',
            success: function (response) {
                $('#category').append(response);
            },
            error: function () {
                console.error('Error occurred while invoking ProductCategoryServlet');
            }
        });

        $('#category').change(function () {
            var categoryId = $(this).val();
            if (categoryId) {
                fetchAndDisplayProductsByCategory(categoryId);
            } else {
                fetchAndDisplayAllProducts();
            }
        });

        $('#sort').change(function () {
            var sortValue = $(this).val();
            if (sortValue && displayedProducts) {
                var sortedProducts = displayedProducts.slice();
                if (sortValue === 'asc') {
                    sortedProducts.sort(function(a, b) {
                        return a.price - b.price;
                    });
                } else if (sortValue === 'desc') {
                    sortedProducts.sort(function(a, b) {
                        return b.price - a.price;
                    });
                }
                
                
                displayProducts(sortedProducts);
            }
        });
        $(document).on('click', '.add-to-cart', function() {
            var productData = $(this).data('product');
            var cartItems = JSON.parse(localStorage.getItem('cart') || '[]');
            cartItems.push(productData);
            localStorage.setItem('cart', JSON.stringify(cartItems));
            // Send cart data to the server
            $.ajax({
                type: 'POST',
                url: 'CartManagerServlet',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({ cart: cartItems }),
                success: function(response) {
                    alert('Product added to cart and cart data saved to session!');
                },
                error: function(xhr, status, error) {
                    console.error('Error saving cart to session:', error);
                }
            });
        });
    });

</script>


</body>
</html>
