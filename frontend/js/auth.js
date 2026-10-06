console.log("Foodie frontend loaded");

document.addEventListener("DOMContentLoaded", function () {

    injectAppNavigation();
    setupLogin();
    setupRegister();
    setupLogout();
    protectPrivatePage();

});


/* ================= AUTH HELPERS ================= */

function showMessage(element, text, type) {
    if (!element) return;

    element.textContent = text;
    element.className = "form-message " + (type || "");
}


function setButtonLoading(button, loadingText) {
    if (!button) return;

    if (!button.dataset.originalText) {
        button.dataset.originalText = button.textContent.trim();
    }

    button.disabled = true;
    button.classList.add("is-loading");
    button.innerHTML = `<span class="button-spinner"></span>${loadingText}`;
}


function resetButton(button) {
    if (!button) return;

    button.disabled = false;
    button.classList.remove("is-loading");
    button.textContent = button.dataset.originalText || "Submit";
}


/* ================= LOGIN ================= */

function setupLogin() {

    const form = document.getElementById("loginForm");
    if (!form) return;

    form.addEventListener("submit", async function (event) {

        event.preventDefault();

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value;

        const message = document.getElementById("message");
        const button = form.querySelector("button[type='submit']");

        if (!username || !password) {
            showMessage(message, "Please enter username and password.", "error");
            return;
        }

        setButtonLoading(button, "Signing in...");

        try {

            const response = await fetch(API_URL + "/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error("Invalid username or password.");
                }

                throw new Error("Login failed (HTTP " + response.status + "). Please try again.");
            }

            const data = await response.json();

            if (!data.token) {
                throw new Error("Login response did not contain a token.");
            }

            localStorage.setItem("token", data.token);
            localStorage.setItem("username", data.username || username);

            if (data.refreshToken) {
                localStorage.setItem("refreshToken", data.refreshToken);
            }

            if (data.id !== undefined && data.id !== null) {
                localStorage.setItem("userId", data.id);
            }

            showMessage(message, "Login successful! Redirecting...", "success");

            setTimeout(function () {
                window.location.href = "home.html";
            }, 500);

        } catch (error) {

            console.error("Login error:", error);

            let text = error.message || "Unable to login.";

            if (error instanceof TypeError) {
                text = "Cannot connect to the server. Make sure the backend is running on port 8080.";
            }

            showMessage(message, text, "error");
            resetButton(button);
        }

    });
}


/* ================= REGISTER ================= */

function setupRegister() {

    const form = document.getElementById("registerForm");
    if (!form) return;

    form.addEventListener("submit", async function (event) {

        event.preventDefault();

        const username = document.getElementById("registerUsername").value.trim();
        const password = document.getElementById("registerPassword").value;

        const message = document.getElementById("registerMessage");
        const button = form.querySelector("button[type='submit']");

        if (!username || !password) {
            showMessage(message, "Please enter username and password.", "error");
            return;
        }

        if (password.length < 4) {
            showMessage(message, "Password must be at least 4 characters.", "error");
            return;
        }

        setButtonLoading(button, "Creating account...");

        try {

            const response = await fetch(API_URL + "/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error("Please check the username and password.");
                }

                throw new Error("Registration failed (HTTP " + response.status + "). Please try again.");
            }

            showMessage(message, "Account created successfully! Redirecting to login...", "success");

            setTimeout(function () {
                window.location.href = "login.html";
            }, 800);

        } catch (error) {

            console.error("Registration error:", error);

            let text = error.message || "Unable to create account.";

            if (error instanceof TypeError) {
                text = "Cannot connect to the server. Make sure the backend is running on port 8080.";
            }

            showMessage(message, text, "error");
            resetButton(button);
        }

    });
}


/* ================= LOGOUT ================= */

function setupLogout() {

    const logoutButton = document.querySelector(".logout-btn");
    if (!logoutButton) return;

    logoutButton.addEventListener("click", async function () {

        const token = localStorage.getItem("token");

        logoutButton.disabled = true;
        logoutButton.textContent = "Logging out...";

        try {

            if (token) {
                await fetch(API_URL + "/logout", {
                    method: "POST",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                });
            }

        } catch (error) {
            console.warn("Logout request failed:", error);
        } finally {

            clearStorage();
            window.location.href = "login.html";
        }

    });
}


/* ================= PAGE PROTECTION ================= */

function protectPrivatePage() {

    const publicPages = ["login.html", "register.html", ""];

    const currentPage =
        window.location.pathname.split("/").pop() || "";

    if (!publicPages.includes(currentPage) && !localStorage.getItem("token")) {
        window.location.href = "login.html";
    }
}


/* ================= APP NAVIGATION ================= */

function injectAppNavigation() {

    const currentPage =
        window.location.pathname.split("/").pop() || "home.html";

    if (currentPage === "login.html" || currentPage === "register.html") {
        return;
    }

    if (document.querySelector(".app-sidebar")) {
        return;
    }

    const navItems = [
        ["home.html", "⌂", "Home"],
        ["restaurants.html", "🏪", "Restaurants"],
        ["foods.html", "🍴", "Food"],
        ["cart.html", "🛒", "Cart"],
        ["orders.html", "📦", "Orders"],
        ["profile.html", "👤", "Profile"]
    ];

    const sidebar = document.createElement("aside");
    sidebar.className = "app-sidebar";

    sidebar.innerHTML = `
        <div class="sidebar-brand">
            <div class="brand-mark">F</div>
            <div>
                <div class="sidebar-logo">Foodie<span>.</span></div>
                <small>Fresh & fast</small>
            </div>
        </div>

        <div class="sidebar-label">MENU</div>

        <nav class="sidebar-menu">
            ${navItems.map(item => `
                <a href="${item[0]}" class="sidebar-link ${currentPage === item[0] ? "active" : ""}">
                    <span class="sidebar-icon">${item[1]}</span>
                    <span>${item[2]}</span>
                </a>
            `).join("")}
        </nav>

        <div class="sidebar-footer">
            <div class="sidebar-user">
                <div class="sidebar-avatar">👤</div>
                <div>
                    <strong>${localStorage.getItem("username") || "Food Lover"}</strong>
                    <small>Customer</small>
                </div>
            </div>
            <button class="sidebar-logout logout-btn">
                ↪ Logout
            </button>
        </div>
    `;

    document.body.prepend(sidebar);
    document.body.classList.add("has-sidebar");

    const oldBottomNav = document.querySelector(".bottom-nav");

    if (oldBottomNav) {
        oldBottomNav.classList.add("mobile-only-nav");
    }
}


function clearStorage() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("userId");
    localStorage.removeItem("customerId");
}
