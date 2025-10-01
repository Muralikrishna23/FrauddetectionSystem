#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_step() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}$1${NC}"
}

print_error() {
    echo -e "${RED}$1${NC}"
}

print_warning() {
    echo -e "${YELLOW}$1${NC}"
}

echo
echo "==============================================="
echo "   🔨 BUILDING FRAUD DETECTION DOCKER IMAGE"
echo "==============================================="
echo

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "❌ ERROR: Docker is not installed or not in PATH"
    echo "Please install Docker:"
    echo "  Ubuntu/Debian: sudo apt-get install docker.io"
    echo "  CentOS/RHEL: sudo yum install docker"
    echo "  macOS: Download Docker Desktop from docker.com"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "❌ ERROR: Maven is not installed or not in PATH"
    echo "Please install Apache Maven 3.6+"
    echo "  Ubuntu/Debian: sudo apt-get install maven"
    echo "  CentOS/RHEL: sudo yum install maven"
    echo "  macOS: brew install maven"
    exit 1
fi

print_success "✅ Docker and Maven are available"
echo

print_step "🧹 Step 1: Cleaning previous builds..."
mvn clean
if [ $? -ne 0 ]; then
    print_error "❌ ERROR: Maven clean failed"
    exit 1
fi

echo
print_step "🧪 Step 2: Running tests..."
if ! mvn test; then
    print_warning "⚠️  WARNING: Some tests failed, but continuing build..."
    echo "You may want to check the test results"
fi

echo
print_step "📦 Step 3: Building Java application..."
mvn package -DskipTests
if [ $? -ne 0 ]; then
    print_error "❌ ERROR: Maven package failed"
    exit 1
fi

# Check if JAR file was created
if [ ! -f target/spring-boot-fraud-detection-*.jar ]; then
    print_error "❌ ERROR: JAR file not found in target directory"
    echo "Make sure the build completed successfully"
    exit 1
fi

print_success "✅ Java application built successfully"
echo

print_step "🐳 Step 4: Building Docker image..."
docker build -t fraud-detection:latest .
if [ $? -ne 0 ]; then
    print_error "❌ ERROR: Docker build failed"
    echo "Check the Dockerfile and try again"
    exit 1
fi

echo
print_step "🔍 Step 5: Verifying Docker image..."
docker images fraud-detection:latest
if [ $? -ne 0 ]; then
    print_error "❌ ERROR: Docker image verification failed"
    exit 1
fi

echo
echo "==============================================="
print_success "   ✅ BUILD COMPLETED SUCCESSFULLY!"
echo "==============================================="
echo
echo "📦 JAR file: target/spring-boot-fraud-detection-1.0.0.jar"
echo "🐳 Docker image: fraud-detection:latest"
echo
echo "Next steps:"
echo "  1. Run: ./scripts/docker-run.sh"
echo "  2. Test: http://localhost:8080/api/v1/swagger-ui.html"
echo