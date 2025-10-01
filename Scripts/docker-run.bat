@echo off
setlocal enabledelayedexpansion

echo.
echo ===============================================
echo    🚀 STARTING FRAUD DETECTION SYSTEM
echo ===============================================
echo.

REM Check if Docker is running
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker is not running
    echo Please start Docker Desktop and try again
    pause
    exit /b 1
)

REM Check if docker-compose.yml exists
if not exist "docker-compose.yml" (
    echo ❌ ERROR: docker-compose.yml not found
    echo Make sure you're in the project root directory
    pause
    exit /b 1
)

echo ✅ Docker is running and docker-compose.yml found
echo.

echo 🛑 Step 1: Stopping any existing services...
docker-compose down
if %errorlevel% neq 0 (
    echo ⚠️  WARNING: Error stopping existing services (may not exist)
)

echo.
echo 🐳 Step 2: Starting services with Docker Compose...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ❌ ERROR: Failed to start services
    echo Check docker-compose.yml and try again
    pause
    exit /b 1
)

echo ✅ Services started in background
echo.

echo ⏳ Step 3: Waiting for services to initialize...
echo This may take 30-60 seconds for first run...
timeout /t 45 /nobreak

echo.
echo 🔍 Step 4: Checking service health...
docker-compose ps

echo.
echo 💓 Step 5: Testing application health...
curl -f http://localhost:8080/api/v1/actuator/health 2>nul
if %errorlevel% neq 0 (
    echo ⚠️  Application may still be starting up...
    echo Wait a few more minutes and try accessing the URLs below
) else (
    echo ✅ Application is healthy and responding
)

echo.
echo ===============================================
echo    🎉 SYSTEM STARTED SUCCESSFULLY!
echo ===============================================
echo.
echo 🌐 Access Points:
echo   • API Base:           http://localhost:8080/api/v1
echo   • Swagger UI:         http://localhost:8080/api/v1/swagger-ui.html
echo   • H2 Database:        http://localhost:8080/api/v1/h2-console
echo   • Health Check:       http://localhost:8080/api/v1/actuator/health
echo   • Prometheus:         http://localhost:9090
echo   • Grafana:            http://localhost:3000 (admin/admin)
echo.
echo 🔐 API Credentials:
echo   • User:     fraud-user / password123
echo   • Analyst:  fraud-analyst / analyst123
echo   • Admin:    fraud-admin / admin123
echo.
echo 📋 Useful Commands:
echo   • View logs:          docker-compose logs -f fraud-detection-app
echo   • Stop services:      docker-compose down
echo   • Restart app:        docker-compose restart fraud-detection-app
echo.
pause
