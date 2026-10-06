@echo off
cd /d "%~dp0"
echo Starting Foodie frontend on http://localhost:51103
node foodie-server.js
pause
