package com.maxtree.automotive.dashboard.example;

import java.sql.*;

public class MyFirstPostgresql {
    public static void main(String[] args) {

        //Driver driver = new Driver;

        try {

//            DriverManager.registerDriver(new org.postgresql.Driver());

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/TB4";

            String user = "DL";

            String password = "123456";

            Connection conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();

            String sql = "select * from users";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

      System.out.println(resultSet.getString("username"));

   System.out.println(resultSet.getString("hashed"));

            }

        } catch (SQLException e1) {

            e1.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
