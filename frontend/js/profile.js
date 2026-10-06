document.addEventListener("DOMContentLoaded", function () {

    loadProfile();

});


// ================= LOAD PROFILE =================

function loadProfile() {

    let customerId = localStorage.getItem("customerId");


    if (!customerId) {
        return;
    }


    fetch(API_URL + "/ccustomerbyid/" + customerId, {

        method: "GET",

        headers: getAuthHeaders()

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Profile not found");
            }

            return response.json();

        })

        .then(customer => {

            document.getElementById("customerName").value =
                customer.name || "";

            document.getElementById("customerPhone").value =
                customer.phone || "";

            document.getElementById("customerEmail").value =
                customer.email || "";

            document.getElementById("customerAddress").value =
                customer.address || "";


            document.getElementById("profileButton").innerText =
                "Update Profile";


            document.getElementById("profileSubtitle").innerText =
                "Manage your customer profile";

        })

        .catch(error => {

            console.log(error);

        });

}


// ================= SHOW PROFILE DETAILS =================

function showProfileDetails(customer) {

    document.getElementById("showName").innerText =
        customer.name;

    document.getElementById("showPhone").innerText =
        customer.phone;

    document.getElementById("showEmail").innerText =
        customer.email;

    document.getElementById("showAddress").innerText =
        customer.address;


    document.getElementById("profileForm").style.display =
        "none";

    document.getElementById("profileDetails").style.display =
        "block";

    document.getElementById("profileSubtitle").innerText =
        "Your customer profile";

}


// ================= CREATE / UPDATE PROFILE =================

document.getElementById("profileForm")?.addEventListener("submit", function (event) {

    event.preventDefault();


    let name =
        document.getElementById("customerName").value;

    let phone =
        document.getElementById("customerPhone").value;

    let email =
        document.getElementById("customerEmail").value;

    let address =
        document.getElementById("customerAddress").value;


    let customerData = {

        name: name,

        phone: phone,

        email: email,

        address: address

    };


    let customerId =
        localStorage.getItem("customerId");


    let url;

    let method;


    // ================= UPDATE =================

    if (customerId) {

        url = API_URL + "/cupdate/" + customerId;

        method = "PUT";

    }

    // ================= CREATE =================

    else {

        url = API_URL + "/ccustomer";

        method = "POST";

    }


    fetch(url, {

        method: method,

        headers: getAuthHeaders(),

        body: JSON.stringify(customerData)

    })

        .then(response => {

            if (!response.ok) {
                throw new Error("Profile operation failed");
            }

            return response.json();

        })

        .then(customer => {

            localStorage.setItem(
                "customerId",
                customer.id
            );


            if (!customerId) {

                document.getElementById("profileMessage").innerText =
                    "Profile created successfully!";

                showProfileDetails(customer);

            }

            else {

                document.getElementById("profileMessage").innerText =
                    "Profile updated successfully!";

                showProfileDetails(customer);

            }

        })

        .catch(error => {

            console.log(error);

            document.getElementById("profileMessage").innerText =
                "Unable to save profile.";

        });

});