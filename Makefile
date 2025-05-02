# Datei: Makefile

# -------------------------
# Konfigurierbare Variablen
# -------------------------
MODULES = plants-core care-ui
PROFILE ?= dev
JAR_CMD = mvn clean package -DskipTests

# -------------------------
# Targets
# -------------------------

all: build up

build:
	@echo "🛠  Building Maven modules..."
	$(JAR_CMD)

up:
	@echo "🐳  Starting Docker Compose (profile=$(PROFILE))..."
	docker compose --env-file .env up -d --build

down:
	@echo "🛑  Stopping Docker Compose..."
	docker compose down

logs:
	@echo "📜  Showing logs..."
	docker compose logs -f

clean:
	@echo "🧹  Cleaning target directories..."
	rm -rf $(addsuffix /target, $(MODULES))

rebuild: clean all
