package xyz.n7mn.dev.survivalsystem.sql;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter @Setter
public class SQLConnection {
    boolean useSQL;

    long time;

    private Connection connection;

    public void init() {
        try {
            if (useSQL) {
                Class.forName("com.mysql.jdbc.Driver");
            } else {
                Class.forName("org.sqlite.JDBC");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connectionIsAlive()) connection = createConnection();
        } catch (SQLException e) {
            connection = createConnection();
        }
        return connection;
    }

    public Connection createConnection() {
        try {
            Plugin plugin = SurvivalInstance.INSTANCE.getPlugin();

            connection = useSQL ?
                    DriverManager.getConnection("jdbc:mysql://" + plugin.getConfig().getString("MySQL.Server") + ":" + plugin.getConfig().getInt("MySQL.Port") + "/" + plugin.getConfig().getString("MySQL.Database") + plugin.getConfig().getString("MySQL.Option"), plugin.getConfig().getString("MySQL.Username"), plugin.getConfig().getString("MySQL.Password")) :
                    DriverManager.getConnection("jdbc:sqlite:" + plugin.getConfig().getString("SQLiteLocation"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public boolean connectionIsAlive() throws SQLException {
        final long time = System.currentTimeMillis();
        if (useSQL && time - this.time > 900_000) {
            this.time = time;

            return this.connection.isValid(1);
        } else {
            return true;
        }
    }
}