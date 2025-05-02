# -------------------------
# Konfigurierbare Variablen
# -------------------------
MODULES = plants-core care-ui
PROFILE ?= prod
JAR_CMD = mvn clean package -DskipTests

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
# Shortcut-Profil-Targets
# -------------------------

dev:
	@echo "🔧 Starting DEV mode (only PostgreSQL in Docker)..."
	$(MAKE) PROFILE=dev up

prod:
	@echo "🚀 Starting PROD mode (everything in Docker)..."
	$(MAKE) PROFILE=prod build up
