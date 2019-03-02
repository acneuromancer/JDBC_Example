package com.sfj.jdbc;

import java.util.List;

public class MainApp {

    private static DataBase db = null;

    public static void main(String[] args) {
        db = new DataBase();

        if(!fillDataBase()) {
            System.exit(0);
        }

        db.showUsersMeta();

        List<User> users = db.getAllUsers();
        for (User u : users) {
            System.out.println(u);
        }
    }

    private static boolean fillDataBase() {
        if (db == null) {
            System.out.println("First instantiate a DataBase object!");
            return false;
        }

        db.addUser("John Doe", 20);
        db.addUser("Kate Smith", 30);
        db.addUser("Tim Taylor", 31);
        db.addUser("John Smith", 25);

        return true;
    }
}
