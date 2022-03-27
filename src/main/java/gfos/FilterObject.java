package gfos;

import java.util.ArrayList;

public class FilterObject {
    private final ArrayList<Integer> field, level, time;
    private final Integer maxDistance;

    public FilterObject() {
        field = null;
        level = null;
        time = null;
        maxDistance = null;
    }

    public FilterObject(ArrayList<Integer> field, ArrayList<Integer> level, ArrayList<Integer> time, Integer maxDistance) {
        this.field = field;
        this.level = level;
        this.time = time;
        if (maxDistance == -1) {
            this.maxDistance = null;
        } else {
            this.maxDistance = maxDistance;
        }
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
}
