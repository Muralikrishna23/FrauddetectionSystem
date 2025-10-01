#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

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

print_info() {
    echo -e "${CYAN}$1${NC}"
}

echo
echo "==============================================="
echo "   🚀 STARTING FRAUD DETECTION SYSTEM"
echo "==============================================="
echo

# Check if Docker is running
if ! docker ps >/dev/null 2>&1; then
    print_error "❌ ERROR: Docker is not running"
    echo "Please start Docker and try again:"
    echo "  Linux: sudo systemctl start docker"
    echo "  macOS: Start Docker Desktop application"
    exit 1
fi

# Check if docker-compose.yml exists
if [ ! -f "docker-compose.yml" ]; then
    print_error "❌ ERROR: docker-compose.yml not found"
    echo "Make sure you're in the project root directory"
    exit 1
fi

print_success "✅ Docker is running and docker-compose.yml found"
echo

print_step "🛑 Step 1: Stopping any existing services..."
if ! docker-compose down 2>/dev/null; then
    print_warning "⚠️  WARNING: Error stopping existing services (may not exist)"
fi

echo
print_step "🐳 Step 2: Starting services with Docker Compose..."
docker-compose up -d
if [ $? -ne 0 ]; then
    print_error "❌ ERROR: Failed to start services"
    echo "Check docker-compose.yml and try again"
    exit 1
fi

print_success "✅ Services started in background"
echo

print_step "⏳ Step 3: Waiting for services to initialize..."
echo "This may take 30-60 seconds for first run..."
for i in {1..45}; do
    echo -n "."
    sleep 1
done
echo

echo
print_step "🔍 Step 4: Checking service health..."
docker-compose ps

echo
print_step "💓 Step 5: Testing application health..."
if curl -f http://localhost:8080/api/v1/actuator/health >/dev/null 2>&1; then
    print_success "✅ Application is healthy and responding"
else
    print_warning "⚠️  Application may still be starting up..."
    echo "Wait a few more minutes and try accessing the URLs below"
fi

echo
echo "==============================================="
print_success "   🎉 SYSTEM STARTED SUCCESSFULLY!"
echo "==============================================="
echo
print_info "🌐 Access Points:"
echo "  • API Base:           http://localhost:8080/api/v1"
echo "  • Swagger UI:         http://localhost:8080/api/v1/swagger-ui.html"
echo "  • H2 Database:        http://localhost:8080/api/v1/h2-console"
echo "  • Health Check:       http://localhost:8080/api/v1/actuator/health"
echo "  • Prometheus:         http://localhost:9090"
echo "  • Grafana:            http://localhost:3000 (admin/admin)"
echo
print_info "🔐 API Credentials:"
echo "  • User:     fraud-user / password123"
echo "  • Analyst:  fraud-analyst / analyst123"
echo "  • Admin:    fraud-admin / admin123"
echo
print_info "📋 Useful Commands:"
echo "  • View logs:          docker-compose logs -f fraud-detection-app"
echo "  • Stop services:      docker-compose down"
echo "  • Restart app:        docker-compose restart fraud-detection-app"
echo
