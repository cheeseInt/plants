
# Docker Setup mit PostgreSQL, Grafana & pgAdmin (inkl. Backups)

## 📦 Docker Compose Grundstruktur (PostgreSQL)

```yaml
version: '3'

services:
  postgres:
    image: postgres:16-alpine
    container_name: elpro-postgres
    ports:
      - "15472:5432"
    environment:
      - PGDATA=/var/lib/postgresql/data/pgdata
      - POSTGRES_PASSWORD=postgres
    volumes:
      - ./volume/16/pgdata/:/var/lib/postgresql/data/pgdata
    networks:
      - postgres

networks:
  postgres:
```

- ✅ Die Datenbankdaten liegen im Host-Ordner `./volume/16/pgdata/`
- ✅ Können durch **Time Machine** gesichert werden (sofern nicht ausgeschlossen)

---

## 📊 Grafana hinzufügen (mit persistentem Speicher)

```yaml
  grafana:
    image: grafana/grafana-oss:latest
    container_name: elpro-grafana
    ports:
      - "13000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
    volumes:
      - ./volume/grafana/data:/var/lib/grafana
      - ./volume/grafana/provisioning:/etc/grafana/provisioning
    depends_on:
      - postgres
    networks:
      - elpro-net
```

```bash
mkdir -p volume/grafana/data
mkdir -p volume/grafana/provisioning
```

---

## 💾 Backup-Skript für PostgreSQL

### `pg_backup.sh`

```bash
#!/bin/bash

CONTAINER_NAME="elpro-postgres"
DB_NAME="elpro"
DB_USER="postgres"
BACKUP_DIR="./backups"
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
FILENAME="pg_backup_${DB_NAME}_$DATE.sql"

docker exec -t $CONTAINER_NAME pg_dump -U $DB_USER $DB_NAME > "$BACKUP_DIR/$FILENAME"
find "$BACKUP_DIR" -type f -name "*.sql" -mtime +7 -exec rm {} \;

echo "Backup gespeichert: $FILENAME"
```

```bash
chmod +x pg_backup.sh
```

### Mit `cron` täglich um 2 Uhr automatisieren

```bash
crontab -e
```

```cron
0 2 * * * /path/to/project/pg_backup.sh >> /path/to/project/backup.log 2>&1
```

---

## 🔁 Restore-Skript

### `pg_restore.sh`

```bash
#!/bin/bash

CONTAINER_NAME="elpro-postgres"
DB_NAME="elpro"
DB_USER="postgres"
DUMP_FILE="$1"

if [ -z "$DUMP_FILE" ]; then
  echo "❌ Bitte den Pfad zur Dump-Datei angeben"
  exit 1
fi

if [ ! -f "$DUMP_FILE" ]; then
  echo "❌ Datei nicht gefunden: $DUMP_FILE"
  exit 1
fi

docker exec -i $CONTAINER_NAME psql -U $DB_USER -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME';" | grep -q 1
if [ $? -ne 0 ]; then
  echo "ℹ️  Datenbank $DB_NAME existiert nicht – wird erstellt..."
  docker exec -i $CONTAINER_NAME psql -U $DB_USER -c "CREATE DATABASE $DB_NAME;"
fi

cat "$DUMP_FILE" | docker exec -i $CONTAINER_NAME psql -U $DB_USER $DB_NAME

echo "✅ Restore abgeschlossen."
```

---

## 🧭 pgAdmin als Web-GUI im Compose

```yaml
  pgadmin:
    image: dpage/pgadmin4
    container_name: elpro-pgadmin
    ports:
      - "15473:80"
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@example.com
      PGADMIN_DEFAULT_PASSWORD: admin
    volumes:
      - ./volume/pgadmin:/var/lib/pgadmin
    depends_on:
      - postgres
    networks:
      - elpro-net
```

```bash
mkdir -p volume/pgadmin
```

Zugriff: http://localhost:15473  
Login: `admin@example.com` / `admin`

---

## 🛠️ pgAdmin Provisioning

### Datei `volume/pgadmin/config/servers/servers.json`

```json
{
  "Servers": {
    "1": {
      "Name": "elpro-postgres",
      "Group": "Docker",
      "Host": "postgres",
      "Port": 5432,
      "MaintenanceDB": "postgres",
      "Username": "postgres",
      "SSLMode": "prefer",
      "PassFile": "/pgpassfile"
    }
  }
}
```

### Datei `volume/pgadmin/config/pgpassfile`

```
postgres:5432:postgres:postgres:postgres
```

### Docker Compose Ergänzung (pgAdmin)

```yaml
    volumes:
      - ./volume/pgadmin:/var/lib/pgadmin
      - ./volume/pgadmin/config/servers:/pgadmin4/servers
      - ./volume/pgadmin/config/pgpassfile:/pgpassfile:ro
```

---

## 🖥️ Alternative Tools

- **DBeaver** – vielseitig, Open Source
- **TablePlus** – macOS-nativ, schnell
- **pgAdmin** – Web-GUI (im Container)

---

✅ Alle Daten und Konfigurationen sind **lokal persistent** und können durch **Time Machine** gesichert werden.
