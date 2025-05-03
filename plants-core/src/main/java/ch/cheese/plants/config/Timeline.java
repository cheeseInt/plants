package ch.cheese.plants.config;

public enum Timeline {
    HOUR,
    DAY,
    WEEK,
    MONTH;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
