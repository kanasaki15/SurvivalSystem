package xyz.n7mn.dev.survivalsystem.cache;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @UtilityClass
public class GraveCache {
    private Map<UUID, GraveInventoryData> graveCache = new HashMap<>();


    public void handle() {
        for (GraveInventoryData data : graveCache.values()) {
            //TODO: お墓のチェックを追加
        }
    }
}
