const API_URL = "http://localhost:8080";

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
