document.addEventListener("DOMContentLoaded", function () {

    loadFoods();

});


// ================= LOAD FOOD =================

function loadFoods() {

    let params =
        new URLSearchParams(
            window.location.search
        );


    let category =
        params.get("category");


    let restaurantId =
        params.get("restaurantId");


    // ================= RESTAURANT =================

    if (restaurantId) {

        loadRestaurantFoods(restaurantId);

        return;

    }


    // ================= ALL FOOD =================

    fetch(API_URL + "/fgetall", {

        method: "GET",

        headers: getAuthHeaders()

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load food");
            }

            return response.json();

        })

        .then(foods => {

            if (category) {

                let filteredFoods =
                    foods.filter(food => {

                        let foodCategory =
                            (food.category || "")
                                .toLowerCase();

                        let selectedCategory =
                            category.toLowerCase();


                        return foodCategory.includes(
                            selectedCategory
                        );

                    });


                showFoods(
                    filteredFoods,
                    category + " Food"
                );

            } else {

                showFoods(
                    foods,
                    "All Food"
                );

            }

        })

        .catch(error => {

            console.log(error);

            document.getElementById(
                "foodContainer"
            ).innerHTML =
                "<p>Unable to load food items.</p>";

        });

}


// ================= RESTAURANT FOOD =================

function loadRestaurantFoods(restaurantId) {

    fetch(
        API_URL +
        "/rgetbyid/" +
        restaurantId,
        {
            method: "GET",
            headers: getAuthHeaders()
        }
    )

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Restaurant not found"
                );
            }

            return response.json();

        })

        .then(restaurant => {

            let foods =
                restaurant.foods || [];


            showFoods(
                foods,
                restaurant.name
            );

        })

        .catch(error => {

            console.log(error);

            document.getElementById(
                "foodContainer"
            ).innerHTML =
                "<p>No food found for this restaurant.</p>";

        });

}


// ================= SHOW FOODS =================

function showFoods(foods, title) {

    let container =
        document.getElementById(
            "foodContainer"
        );


    container.innerHTML = "";


    let heading =
        document.getElementById(
            "foodPageTitle"
        );


    if (heading) {

        heading.innerText = title;

    }


    if (foods.length === 0) {

        container.innerHTML =
            "<p>No food available.</p>";

        return;

    }


    foods.forEach(food => {

        let card =
            document.createElement("div");


        card.className =
            "restaurant-card";


        card.innerHTML = `

            <div class="restaurant-image food-image">

                <img
                    src="${getFoodImage(food)}"
                    alt="${food.name}">

            </div>


            <div class="restaurant-info">

                <h3>
                    ${food.name}
                </h3>

                <p>
                    ${food.category || "Food"}
                </p>

                <p class="food-price">
                    ₹${food.price}
                </p>


                <button
                    class="primary-btn"
                    onclick="
                        addToCart(
                            ${food.id},
                            ${food.price}
                        )
                    ">

                    🛒 Add to Cart

                </button>

            </div>

        `;


        container.appendChild(card);

    });

}


// ================= FOOD IMAGE =================

function getFoodImage(food) {

    let name =
        (food.name || "").toLowerCase();


    let category =
        (food.category || "").toLowerCase();


    if (
        name.includes("pizza") ||
        category.includes("pizza")
    ) {

        return "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=700&q=80";

    }


    if (
        name.includes("burger") ||
        category.includes("burger")
    ) {

        return "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=700&q=80";

    }


    if (
        name.includes("noodle") ||
        category.includes("noodle")
    ) {

        return "https://images.unsplash.com/photo-1557872943-16a5ac26437e?auto=format&fit=crop&w=700&q=80";

    }


    if (
        name.includes("dessert") ||
        category.includes("dessert") ||
        name.includes("cake") ||
        name.includes("ice cream")
    ) {

        return "https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&fit=crop&w=700&q=80";

    }


    return "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=700&q=80";

}


// ================= ADD TO CART =================

function addToCart(foodId, foodPrice) {

    let customerId =
        localStorage.getItem(
            "customerId"
        );


    if (!customerId) {

        alert(
            "Please create your profile first."
        );

        window.location.href =
            "profile.html";

        return;

    }


    fetch(
        API_URL +
        "/cartbycustomer/" +
        customerId,
        {
            method: "GET",
            headers: getAuthHeaders()
        }
    )

        .then(response => {

            if (response.ok) {

                return response.json();

            }

            return null;

        })

        .then(cart => {

            if (cart) {

                let foodIds = [];


                if (cart.foods) {

                    foodIds =
                        cart.foods.map(
                            food => food.id
                        );

                }


                if (!foodIds.includes(foodId)) {

                    foodIds.push(foodId);

                }


                let cartData = {

                    quantity:
                        cart.quantity + 1,

                    totalPrice:
                        cart.totalPrice + foodPrice,

                    customerId:
                        parseInt(customerId),

                    foodIds:
                    foodIds

                };


                return fetch(

                    API_URL +
                    "/cartupdate/" +
                    cart.id,

                    {

                        method: "PUT",

                        headers:
                            getAuthHeaders(),

                        body:
                            JSON.stringify(
                                cartData
                            )

                    }

                );

            }


            let cartData = {

                quantity: 1,

                totalPrice: foodPrice,

                customerId:
                    parseInt(customerId),

                foodIds: [foodId]

            };


            return fetch(

                API_URL + "/cart",

                {

                    method: "POST",

                    headers:
                        getAuthHeaders(),

                    body:
                        JSON.stringify(
                            cartData
                        )

                }

            );

        })

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to add food to cart"
                );

            }

            return response.json();

        })

        .then(data => {

            alert(
                "Food added to cart!"
            );

        })

        .catch(error => {

            console.log(error);

            alert(
                "Unable to add food to cart."
            );

        });

}