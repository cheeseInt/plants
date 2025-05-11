# -------------------------
# Konfigurierbare Variablen
# -------------------------
MODULES = plants-core
PROFILE ?= dev
JAR_CMD = mvn clean package spring-boot:repackage -DskipTests

# -------------------------
# Targets
# -------------------------

all: build up

build:
	@echo "🛠  Building Maven modules with profile '$(PROFILE)'..."
	$(JAR_CMD)

up: check-secrets
	@echo "🐳  Starting Docker Compose with profile '$(PROFILE)'..."
	docker compose --env-file .env --env-file .secret up -d --build

down:
	@echo "🛑  Stopping Docker Compose..."
	docker compose down

logs:
	@echo "📜  Showing Docker Compose logs..."
	docker compose logs -f

clean:
	@echo "🧹  Cleaning target directories..."
	rm -rf $(addsuffix /target, $(MODULES))

rebuild: clean build up

check-secrets:
	@if [ ! -f .secret ]; then \
		echo "❌ ERROR: Datei '.secret' fehlt!"; \
		exit 1; \
	fi

# -------------------------
# Profile-basierte Shortcuts
# -------------------------

dev:
	@echo "🔧 Starting DEV mode (only DB in Docker)..."
	docker compose \
		-f docker-compose.yml \
		-f docker-compose.override.yml \
		--env-file .env \
		--env-file .secret \
		up -d

prod:
	@echo "🚀 Starting PROD mode (everything in Docker)..."
	$(MAKE) PROFILE=prod build
	docker compose \
		-f docker-compose.yml \
		--env-file .env \
		--env-file .secret \
		up -d
