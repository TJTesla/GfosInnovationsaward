package gfos.beans;

import java.util.ArrayList;

public class FilterObject {
    private final ArrayList<Integer> field, level, time;
    private final Integer maxDistance;
    private final Boolean onlyApplied;
    private final Boolean favorites;

    public FilterObject() {
        field = null;
        level = null;
        time = null;
        maxDistance = null;
        onlyApplied = null;
        favorites = null;
    }

    public FilterObject(ArrayList<Integer> field, ArrayList<Integer> level, ArrayList<Integer> time, Integer maxDistance, Boolean onlyApplied, Boolean favorites) {
        this.field = field;
        this.level = level;
        this.time = time;
        if (maxDistance == -1) {
            this.maxDistance = null;
        } else {
            this.maxDistance = maxDistance;
        }
        this.onlyApplied = onlyApplied;
        this.favorites = favorites;
    }

    public ArrayList<Integer> getField() {
        return field;
    }

    public ArrayList<Integer> getLevel() {
        return level;
    }

    public ArrayList<Integer> getTime() {
        return time;
    }

    public Integer getMaxDistance() {
        return maxDistance;
    }

    public Boolean getOnlyApplied() {
        return onlyApplied;
    }

    public Boolean getFavorites() {
        return favorites;
    }
}
