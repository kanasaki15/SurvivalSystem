package xyz.n7mn.dev.survivalsystem.sql;

import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLFormat {
    public void createSQL(String tableName, String tableObject) {
        SQLConnection sqlConnection = SurvivalInstance.INSTANCE.getConnection();

        Connection connection = sqlConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists" + tableName + tableObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}