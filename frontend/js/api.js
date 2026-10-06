// Frontend uses the deployed Spring Boot backend on Render.
const API_URL = "https://food-ordering-backend-p03x.onrender.com";

function getToken() {
    return localStorage.getItem("token");
}

function getAuthHeaders() {
    const token = getToken();

    return {
        "Content-Type": "application/json",
        ...(token ? { "Authorization": "Bearer " + token } : {})
    };
}

function handleUnauthorized(response) {
    if (response.status === 401) {
        clearStorage();
        window.location.href = "login.html";
        return true;
    }
    return false;
}