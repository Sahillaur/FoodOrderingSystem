document.addEventListener("DOMContentLoaded", function () {

    loadCart();

});


// ================= LOAD CART =================

function loadCart() {

    let customerId = localStorage.getItem("customerId");


    if (!customerId) {

        document.getElementById("cartContainer").innerHTML =
            "<p>Please create your profile first.</p>";

        return;
    }


    fetch(API_URL + "/cartbycustomer/" + customerId, {

        method: "GET",

        headers: getAuthHeaders()

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Cart not found");
            }

            return response.json();

        })

        .then(cart => {

            let container =
                document.getElementById("cartContainer");


            container.innerHTML = "";


            let item = document.createElement("div");

            item.className = "restaurant-card";


            let foodList = "";


            if (cart.foods && cart.foods.length > 0) {

                foodList = cart.foods.map(food => {

                    return `<p>🍴 ${food.name} - ₹${food.price}</p>`;

                }).join("");

            }


            item.innerHTML = `

            <div class="restaurant-info">

                <h3>My Cart</h3>

                ${foodList}

                <p>
                    Quantity: ${cart.quantity}
                </p>

                <p>
                    Total: ₹${cart.totalPrice}
                </p>

                <button
                    class="primary-btn"
                    onclick="placeOrder(${cart.id})">

                    Place Order

                </button>

            </div>

        `;


            container.appendChild(item);


            document.getElementById("cartTotal").innerText =
                "₹" + cart.totalPrice;

        })

        .catch(error => {

            console.log(error);


            document.getElementById("cartContainer").innerHTML =
                "<p>Your cart is empty.</p>";


            document.getElementById("cartTotal").innerText =
                "₹0";

        });

}


// ================= PLACE ORDER =================

function placeOrder(cartId) {

    fetch(API_URL + "/orderfromcart/" + cartId, {

        method: "POST",

        headers: getAuthHeaders()

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to place order");
            }

            return response.json();

        })

        .then(order => {

            alert("Order placed successfully!");

            window.location.href = "orders.html";

        })

        .catch(error => {

            console.log(error);

            alert("Unable to place order.");

        });

}