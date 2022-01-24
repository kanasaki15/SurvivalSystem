package xyz.n7mn.dev.survivalsystem.sql.table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.sql.SQLFormat;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GraveTable extends SQLFormat {
    public GraveTable() {
        create();
    }

    public void create() {
        createSQL("grave", "(date datetime2, world string, name string, uuid string, location text, itemstack text, armorStand uuid)");
    }

    public void put(String world, String playerName, UUID uuid, Location location, List<ItemStack> itemStackList, UUID armorStand) {
        Bukkit.getScheduler().runTaskAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {
            try {
                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("insert into death values(?,?,?,?,?,?,?)");

                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(2, world);
                preparedStatement.setString(3, playerName);
                preparedStatement.setString(4, uuid.toString());
                preparedStatement.setObject(5, location);
                preparedStatement.setObject(6, itemStackList);
                preparedStatement.setString(7, armorStand.toString());

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
                PreparedStatement preparedStatement = SurvivalInstance.INSTANCE.getConnection().getConnection().prepareStatement("insert into death values(?,?,?,?,?,?,?)");

                preparedStatement.setTimestamp(1, data.getTimestamp());
                preparedStatement.setString(2, data.getWorld().getName());
                preparedStatement.setString(3, data.getPlayerName());
                preparedStatement.setString(4, data.getUUID().toString());
                preparedStatement.setObject(5, data.getLocation());
                preparedStatement.setObject(6, data.getItemStackList());
                preparedStatement.setString(7, data.getArmorStandUUID().toString());

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void put(Timestamp timestamp) {

    }
}