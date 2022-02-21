package xyz.n7mn.dev.survivalsystem.sql.table;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.cache.serializable.ItemStackData;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.sql.SQLFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * これが動く仕組み
 * - ItemStackをSerializableに変換します
 * - GsonでJSONに変換します
 * - GsonでSerializableに戻します
 * - ItemStackに変換します
 */
public class GraveTable extends SQLFormat {

    public GraveTable() {
        create();
    }

    public void create() {
        createSQL("grave", "(date datetime2, world string, name string, uuid string, itemstack text, armorStand string, active boolean)");
    }

    Type type = new TypeToken<List<ItemStackData>>(){}.getType();

    public void put(String world, String playerName, UUID uuid, String itemGson, UUID armorStand) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("insert into grave values(?,?,?,?,?,?,?)");

                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(2, world);
                preparedStatement.setString(3, playerName);
                preparedStatement.setString(4, uuid.toString());
                preparedStatement.setString(5, itemGson);
                preparedStatement.setString(6, armorStand.toString());
                preparedStatement.setBoolean(7, true);

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void put(GraveInventoryData data) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("insert into grave values(?,?,?,?,?,?,?)");

                preparedStatement.setTimestamp(1, data.getTimestamp());
                preparedStatement.setString(2, data.getWorld().getName());
                preparedStatement.setString(3, data.getPlayerName());
                preparedStatement.setString(4, data.getUUID().toString());
                preparedStatement.setString(5, new Gson().toJson(data.getItemStack()));
                preparedStatement.setString(6, data.getArmorStandUUID().toString());
                preparedStatement.setBoolean(7, true);

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateActive(GraveInventoryData data, boolean active) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("update grave set active = ? where armorStand = ?");
                preparedStatement.setBoolean(1, active);
                preparedStatement.setString(2, data.getArmorStandUUID().toString());

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void get(UUID uuid, Consumer<GraveInventoryData> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("select * from grave where armorStand = ?");
                preparedStatement.setString(1, uuid.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                Gson gson = new GsonBuilder().setObjectToNumberStrategy(CustomNumberStrategy.INTEGER_OR_DOUBLE).create();

                if (resultSet.next() && resultSet.getBoolean(7)) {
                    consumer.accept(new GraveInventoryData(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getString(3), UUID.fromString(resultSet.getString(4)), gson.fromJson(resultSet.getString(5), type), UUID.fromString(resultSet.getString(6))));
                } else {
                    consumer.accept(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void get(Consumer<Map<UUID, GraveInventoryData>> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                Map<UUID, GraveInventoryData> hashMap = new HashMap<>();

                ResultSet resultSet = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("select * from grave").executeQuery();

                Gson gson = new GsonBuilder().setObjectToNumberStrategy(CustomNumberStrategy.INTEGER_OR_DOUBLE).create();

                while (resultSet.next()) {
                    if (resultSet.getBoolean(7)) {
                        //アーマースタンドのUUID
                        UUID armorStand = UUID.fromString(resultSet.getString(6));

                        hashMap.put(armorStand, new GraveInventoryData(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getString(3), UUID.fromString(resultSet.getString(4)), gson.fromJson(resultSet.getString(5), type), armorStand));
                    }
                }

                resultSet.close();
                consumer.accept(hashMap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void getAll(Consumer<Map<UUID, GraveInventoryData>> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                Map<UUID, GraveInventoryData> hashMap = new HashMap<>();

                ResultSet resultSet = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("select * from grave").executeQuery();

                Gson gson = new GsonBuilder().setObjectToNumberStrategy(CustomNumberStrategy.INTEGER_OR_DOUBLE).create();

                while (resultSet.next()) {
                    //アーマースタンドのUUID
                    UUID armorStand = UUID.fromString(resultSet.getString(6));

                    hashMap.put(armorStand, new GraveInventoryData(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getString(3), UUID.fromString(resultSet.getString(4)), gson.fromJson(resultSet.getString(5), type), armorStand, resultSet.getBoolean(7)));
                }

                resultSet.close();
                consumer.accept(hashMap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void getAll(UUID uuid, Consumer<List<GraveInventoryData>> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                List<GraveInventoryData> list = new ArrayList<>();

                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("select * from grave where uuid = ?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                Gson gson = new GsonBuilder().setObjectToNumberStrategy(CustomNumberStrategy.INTEGER_OR_DOUBLE).create();

                while (resultSet.next()) {
                    list.add(new GraveInventoryData(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getString(3), UUID.fromString(resultSet.getString(4)), gson.fromJson(resultSet.getString(5), type), UUID.fromString(resultSet.getString(6)), resultSet.getBoolean(7)));
                }

                resultSet.close();
                consumer.accept(list);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public enum CustomNumberStrategy implements ToNumberStrategy {
        INTEGER_OR_DOUBLE {
            public Number readNumber(JsonReader in) throws IOException, JsonParseException {
                String value = in.nextString();
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException integerE) {
                    try {
                        Double d = Double.valueOf(value);
                        if ((d.isInfinite() || d.isNaN()) && !in.isLenient()) {
                            throw new MalformedJsonException("JSON forbids NaN and infinities: " + d + "; at path " + in.getPreviousPath());
                        }
                        return d;
                    } catch (NumberFormatException doubleE) {
                        throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), doubleE);
                    }
                }
            }
        }
    }
}