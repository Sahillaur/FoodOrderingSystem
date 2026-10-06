document.addEventListener("DOMContentLoaded", function () {

    loadOrders();

});


// ================= LOAD ORDERS =================

function loadOrders() {

    let customerId = localStorage.getItem("customerId");


    if (!customerId) {

        document.getElementById("ordersContainer").innerHTML =
            "<p>Please create your profile first.</p>";

        return;
    }


    fetch(
        API_URL + "/getorderbycustomer/" + customerId,
        {
            method: "GET",
            headers: getAuthHeaders()
        }
    )

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load orders");
            }

            return response.json();

        })

        .then(orders => {

            let container =
                document.getElementById("ordersContainer");


            container.innerHTML = "";


            if (orders.length === 0) {

                container.innerHTML =
                    "<p>No orders found.</p>";

                return;
            }


            orders.forEach(order => {

                let card =
                    document.createElement("div");


                card.className =
                    "restaurant-card";


                let cancelButton = "";


                if (order.status === "PLACED") {

                    cancelButton = `
                    <button
                        class="primary-btn"
                        onclick="cancelOrder(${order.id})">

                        Cancel Order

                    </button>
                `;
                }


                card.innerHTML = `

                <div class="restaurant-info">

                    <h3>
                        Order #${order.id}
                    </h3>

                    <p>
                        Status: ${order.status}
                    </p>

                    <p>
                        Total: ₹${order.totalPrice}
                    </p>

                    <p>
                        Date: ${order.orderDate || "N/A"}
                    </p>

                    ${cancelButton}

                </div>

            `;


                container.appendChild(card);

            });

        })

        .catch(error => {

            console.log(error);

            document.getElementById("ordersContainer").innerHTML =
                "<p>Unable to load orders.</p>";

        });

}


// ================= CANCEL ORDER =================

function cancelOrder(orderId) {

    let confirmCancel =
        confirm("Are you sure you want to cancel this order?");


    if (!confirmCancel) {
        return;
    }


    fetch(
        API_URL + "/cancelorder/" + orderId,
        {
            method: "PUT",
            headers: getAuthHeaders()
        }
    )

        .then(response => {

            if (!response.ok) {
                throw new Error("Order cannot be cancelled");
            }

            return response.json();

        })

        .then(order => {

            alert("Order cancelled successfully!");

            loadOrders();

        })

        .catch(error => {

            console.log(error);

            alert("Order cannot be cancelled.");

        });

}