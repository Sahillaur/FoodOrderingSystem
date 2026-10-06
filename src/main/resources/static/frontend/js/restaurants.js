document.addEventListener("DOMContentLoaded", function () {

    loadRestaurants();

});


function loadRestaurants() {

    fetch(API_URL + "/rgetall", {

        method: "GET",

        headers: getAuthHeaders()

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load restaurants");
            }

            return response.json();

        })

        .then(restaurants => {

            let container =
                document.getElementById(
                    "restaurantContainer"
                );

            container.innerHTML = "";


            if (restaurants.length === 0) {

                container.innerHTML =
                    "<p>No restaurants available.</p>";

                return;

            }


            restaurants.forEach(restaurant => {

                let card =
                    document.createElement("div");

                card.className =
                    "restaurant-card clickable-card";


                card.onclick = function () {

                    window.location.href =
                        "foods.html?restaurantId=" +
                        restaurant.id;

                };


                card.innerHTML = `

                <div class="restaurant-image">

                    <img
                        src="https://images.unsplash.com/photo-1514933651103-005eec06c04b?auto=format&fit=crop&w=700&q=80"
                        alt="Restaurant">

                </div>


                <div class="restaurant-info">

                    <h3>
                        ${restaurant.name}
                    </h3>

                    <p>
                        📍 ${restaurant.address}
                    </p>

                    <p>
                        📞 ${restaurant.phone}
                    </p>


                    <button
                        class="primary-btn restaurant-btn">

                        View Food →

                    </button>

                </div>

            `;


                container.appendChild(card);

            });

        })

        .catch(error => {

            console.log(error);

            document.getElementById(
                "restaurantContainer"
            ).innerHTML =
                "<p>Unable to load restaurants.</p>";

        });

}