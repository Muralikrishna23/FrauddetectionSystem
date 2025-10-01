@echo off
setlocal enabledelayedexpansion

echo.
echo ===============================================
echo    🔨 BUILDING FRAUD DETECTION DOCKER IMAGE
echo ===============================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker is not installed or not in PATH
    echo Please install Docker Desktop for Windows
    echo Download from: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven 3.6+
    pause
    exit /b 1
)

echo ✅ Docker and Maven are available
echo.

echo 🧹 Step 1: Cleaning previous builds...
call mvn clean
if %errorlevel% neq 0 (
    echo ❌ ERROR: Maven clean failed
    pause
    exit /b 1
)

echo.
echo 🧪 Step 2: Running tests...
call mvn test
if %errorlevel% neq 0 (
    echo ⚠️  WARNING: Some tests failed, but continuing build...
    echo You may want to check the test results
)

echo.
echo 📦 Step 3: Building Java application...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ ERROR: Maven package failed
    pause
    exit /b 1
)

REM Check if JAR file was created
if not exist "target\spring-boot-fraud-detection-*.jar" (
    echo ❌ ERROR: JAR file not found in target directory
    echo Make sure the build completed successfully
    pause
    exit /b 1
)

echo ✅ Java application built successfully
echo.

echo 🐳 Step 4: Building Docker image...
docker build -t fraud-detection:latest .
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker build failed
    echo Check the Dockerfile and try again
    pause
    exit /b 1
)

echo.
echo 🔍 Step 5: Verifying Docker image...
docker images fraud-detection:latest
if %errorlevel% neq 0 (
    echo ❌ ERROR: Docker image verification failed
    pause
    exit /b 1
)

echo.
echo ===============================================
echo    ✅ BUILD COMPLETED SUCCESSFULLY!
echo ===============================================
echo.
echo 📦 JAR file: target\spring-boot-fraud-detection-1.0.0.jar
echo 🐳 Docker image: fraud-detection:latest
echo.
echo Next steps:
echo   1. Run: scripts\docker-run.bat
echo   2. Test: http://localhost:8080/api/v1/swagger-ui.html
echo.
pause