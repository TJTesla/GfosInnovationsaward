package gfos;

import java.util.ArrayList;

public class FilterObject {
    private final ArrayList<String> field, level, time;
    private final Integer maxDistance;

    public FilterObject(ArrayList<String> field, ArrayList<String> level, ArrayList<String> time, Integer maxDistance) {
        this.field = field;
        this.level = level;
        this.time = time;
        this.maxDistance = maxDistance;
    }

    public ArrayList<String> getField() {
        return field;
    }

    public ArrayList<String> getLevel() {
        return level;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public Integer getMaxDistance() {
        return maxDistance;
    }
}
