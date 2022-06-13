package xyz.n7mn.dev.survivalsystem.sql.table;

import xyz.n7mn.dev.survivalsystem.sql.SQLFormat;

public class UserData extends SQLFormat {

    public UserData() {
        create();
    }

    public void create() {
        createSQL("data", "(uuid string, difficulty int)");
    }
}
