package xyz.n7mn.dev.survivalsystem.cache.serializable;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class ItemStackData {
    public ItemStackData(Map<String, Object> stack, Map<String, Object> meta) {
        this.stack = stack;
        this.meta = meta;
    }

    private Map<String, Object> stack;
    private Map<String, Object> meta;
}