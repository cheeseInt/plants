-- Delete a plant (and all its dependent rows) by matching the start of its name.
--
-- PostgreSQL. The Hibernate-generated foreign keys have no ON DELETE CASCADE,
-- so every child table must be cleared before the row in `plants` is removed.
--
-- Usage: set :name_prefix, then run the whole file inside one transaction.
--   psql -v name_prefix="'Basi'" -f delete_plant_by_name.sql
-- or replace :name_prefix below with a literal like 'Basi%'.

BEGIN;

-- Collect the ids to delete once, so every statement targets the same set.
CREATE TEMP TABLE _plants_to_delete ON COMMIT DROP AS
SELECT id
FROM plants
WHERE nickname ILIKE :name_prefix || '%';
-- Match on nickname; add OR common_name / scientific_name ILIKE ... if needed.

-- Optional safety check: see what will be removed before it happens.
SELECT id, nickname, scientific_name, common_name
FROM plants
WHERE id IN (SELECT id FROM _plants_to_delete);

-- Child tables referencing plants.id
DELETE FROM measurements   WHERE plant_id  IN (SELECT id FROM _plants_to_delete);
DELETE FROM plant_care     WHERE plant_id  IN (SELECT id FROM _plants_to_delete);
DELETE FROM battery_log    WHERE plant_id  IN (SELECT id FROM _plants_to_delete);

-- @ElementCollection tables (join column plant_entity_id)
DELETE FROM plant_entity_missing   WHERE plant_entity_id IN (SELECT id FROM _plants_to_delete);
DELETE FROM plant_entity_know_hows WHERE plant_entity_id IN (SELECT id FROM _plants_to_delete);
DELETE FROM plant_entity_actions   WHERE plant_entity_id IN (SELECT id FROM _plants_to_delete);

-- Finally the plant itself
DELETE FROM plants WHERE id IN (SELECT id FROM _plants_to_delete);

COMMIT;