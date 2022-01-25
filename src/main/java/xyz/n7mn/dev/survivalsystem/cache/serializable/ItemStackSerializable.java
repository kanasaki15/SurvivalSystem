package xyz.n7mn.dev.survivalsystem.cache.serializable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class ItemStackSerializable implements Serializable {

    public ItemStackSerializable(List<Map<String, Object>> itemStack) {
        this.itemStack = itemStack;
    }

    @Serial //現在のバージョンは1...
    private static final long serialVersionUID = 1;

    private List<Map<String, Object>> itemStack;

    public List<Map<String, Object>> getSerializable() {
        return itemStack;
    }

    public void setSerializable(List<Map<String, Object>> itemStack) {
        this.itemStack = itemStack;
    }
}