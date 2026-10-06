const http = require("http");
const fs = require("fs");
const path = require("path");

const FRONTEND_DIR = path.join(__dirname, "frontend");
const BACKEND_HOST = "localhost";
const BACKEND_PORT = 8080;
const FRONTEND_PORT = 51103;

const MIME_TYPES = {
    ".html": "text/html; charset=utf-8",
    ".css": "text/css; charset=utf-8",
    ".js": "application/javascript; charset=utf-8",
    ".json": "application/json; charset=utf-8",
    ".png": "image/png",
    ".jpg": "image/jpeg",
    ".jpeg": "image/jpeg",
    ".gif": "image/gif",
    ".svg": "image/svg+xml",
    ".ico": "image/x-icon"
};

function safeFrontendPath(urlPath) {
    const decoded = decodeURIComponent(urlPath.split("?")[0]);
    const relative = decoded === "/" ? "home.html" : decoded.replace(/^\/+/, "");
    const filePath = path.resolve(FRONTEND_DIR, relative);
    const root = path.resolve(FRONTEND_DIR) + path.sep;

    if (filePath !== path.resolve(FRONTEND_DIR) && !filePath.startsWith(root)) {
        return null;
    }

    return filePath;
}

function serveFrontend(req, res, filePath) {
    fs.stat(filePath, (error, stats) => {
        if (!error && stats.isFile()) {
            const ext = path.extname(filePath).toLowerCase();
            res.writeHead(200, {
                "Content-Type": MIME_TYPES[ext] || "application/octet-stream",
                "Cache-Control": "no-store"
            });
            fs.createReadStream(filePath).pipe(res);
            return;
        }

        res.writeHead(404, { "Content-Type": "text/plain; charset=utf-8" });
        res.end("Not found");
    });
}

function proxyToBackend(req, res) {
    const options = {
        hostname: BACKEND_HOST,
        port: BACKEND_PORT,
        path: req.url,
        method: req.method,
        headers: { ...req.headers, host: `${BACKEND_HOST}:${BACKEND_PORT}` }
    };

    delete options.headers["content-length"];
    delete options.headers["connection"];

    const backendRequest = http.request(options, backendResponse => {
        const headers = { ...backendResponse.headers };
        delete headers["content-length"];
        delete headers["connection"];

        res.writeHead(backendResponse.statusCode || 502, headers);
        backendResponse.pipe(res);
    });

    backendRequest.on("error", error => {
        console.error("Backend connection error:", error.message);
        if (!res.headersSent) {
            res.writeHead(502, { "Content-Type": "application/json; charset=utf-8" });
        }
        res.end(JSON.stringify({
            message: "Cannot connect to Spring Boot backend on port 8080."
        }));
    });

    req.pipe(backendRequest);
}

const server = http.createServer((req, res) => {
    const pathname = decodeURIComponent((req.url || "/").split("?")[0]);

    // Existing static frontend files are served locally. Every other route
    // is an API request and is forwarded unchanged to Spring Boot.
    const filePath = safeFrontendPath(req.url || "/");

    if (req.method === "GET" || req.method === "HEAD") {
        fs.stat(filePath || "", (error, stats) => {
            if (!error && stats.isFile()) {
                serveFrontend(req, res, filePath);
            } else {
                proxyToBackend(req, res);
            }
        });
        return;
    }

    // POST/PUT/DELETE/OPTIONS etc. are API requests.
    proxyToBackend(req, res);
});

server.listen(FRONTEND_PORT, "localhost", () => {
    console.log(`Foodie frontend: http://localhost:${FRONTEND_PORT}`);
    console.log(`Spring Boot backend: http://localhost:${BACKEND_PORT}`);
    console.log("Frontend files are served locally; API requests are forwarded to Spring Boot.");
});
