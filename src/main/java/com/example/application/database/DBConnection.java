package com.example.application.database;

import java.sql.*;

public class DBConnection {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook_project", "root", "lxMac34!2");
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Error while opening the database connection.", ex);

        }
    }
    public void closeConnection()  {
        try {
            connection.close();
        }
        catch (SQLException ex) {
            throw new RuntimeException("Error while closing the database connection.", ex);
        }
    }


}
