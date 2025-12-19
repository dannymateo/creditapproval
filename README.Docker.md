# Docker Deployment Guide

## Prerequisites

- Docker 20.10 or higher
- Docker Compose 2.0 or higher
- At least 2GB of available RAM
- Ports 8080 and 5432 available

## Quick Start

### 1. Configure Environment Variables

```bash
# Copy the example file
cp env.template .env

# Edit the .env file with your configurations
nano .env
```

### 2. Build and Start Services

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f app
```

### 3. Verify Status

```bash
# View container status
docker-compose ps
```

## Useful Commands

### Container Management

```bash
# Stop services
docker-compose stop

# Start stopped services
docker-compose start

# Restart services
docker-compose restart

# Stop and remove containers
docker-compose down

# Stop and remove containers + volumes
docker-compose down -v
```

### Log Access

```bash
# View logs in real-time
docker-compose logs -f

# View last 100 lines of logs
docker-compose logs --tail=100

# View database logs
docker-compose logs -f postgres
```

### Container Access

```bash
# Access application container
docker-compose exec app sh

# Access PostgreSQL container
docker-compose exec postgres psql -U postgres -d creditapproval
```

### Rebuild Application

```bash
# Rebuild image without cache
docker-compose build --no-cache

# Rebuild and start
docker-compose up -d --build
```

## Service Access

- **Application**: http://localhost:8080
- **Database**: localhost:5432

## Test Users

### Administrator
- **Email**: admin@cotrafaq.com
- **Password**: testAdmin!1

### Customer
- **Email**: cliente1@gmail.com
- **Password**: testCustomer!1

## Database Management

### Database Backup

```bash
# Create backup
docker-compose exec postgres pg_dump -U postgres creditapproval > backup.sql

# Create compressed backup
docker-compose exec postgres pg_dump -U postgres creditapproval | gzip > backup.sql.gz
```

### Restore Database

```bash
# Restore from backup
cat backup.sql | docker-compose exec -T postgres psql -U postgres creditapproval

# Restore from compressed backup
gunzip < backup.sql.gz | docker-compose exec -T postgres psql -U postgres creditapproval
```

### Reset Database

```bash
# Stop services
docker-compose down

# Remove volumes (WARNING: this deletes all data)
docker volume rm creditapproval_postgres_data

# Start services (init.sql will run again)
docker-compose up -d
```

## Troubleshooting

### Application Does Not Start

```bash
# View detailed logs
docker-compose logs app

# Check database status
docker-compose exec postgres pg_isready -U postgres
```

### Database Connection Issues

```bash
# Verify PostgreSQL is running
docker-compose ps postgres

# Check credentials in .env
cat .env | grep POSTGRES

# Restart database
docker-compose restart postgres
```

### Memory Issues

```bash
# Adjust JAVA_OPTS in .env
JAVA_OPTS=-Xms256m -Xmx512m

# Rebuild and restart
docker-compose up -d --build
```

### View Resource Usage

```bash
# View resource usage
docker stats

# View disk space
docker system df
```

