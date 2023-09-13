package com.example.application.manager;

import com.example.application.data.Person;
import com.example.application.database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.application.repository.PersonDataRepository.*;

public class DBPersonManager {
    private final DBConnection dbConnection = new DBConnection();

    public void readPersonTable() {
        try {
            dbConnection.openConnection();
            Statement st = dbConnection.getConnection().createStatement();
            String query = "SELECT * FROM persons";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Person person = new Person();
                person.setName(rs.getString("name"));
                person.setLastName(rs.getString("lastname"));
                person.setStreet(rs.getString("street"));
                person.setCity(rs.getString("city"));
                person.setCountry(rs.getString("country"));
                person.setPhoneNumber(rs.getInt("phone"));
                person.setEmail(rs.getString("email"));
                person.setId(rs.getInt("id"));

                getIdToPersonMap().put(person.getId(), person);
                getIdToPhoneMap().put(person.getId(), person.getPhoneNumber());
                getPhoneNumberSet().add(person.getPhoneNumber());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Exception is occurred while reading the persons from database",e);
        } finally {
            dbConnection.closeConnection();
        }
    }

    public void addPersonToDB(Person item) {
        try {
            dbConnection.openConnection();
            String query = "INSERT INTO persons(name, lastname, street, city, country, phone, email, id) VALUES( ?, ?, ?, ?, ?, ?, ?, ?)";
            dbConnection.setPreparedStatement(dbConnection.getConnection().prepareStatement(query));

            dbConnection.getPreparedStatement().setString(1, item.getName());
            dbConnection.getPreparedStatement().setString(2, item.getLastName());
            dbConnection.getPreparedStatement().setString(3, item.getStreet());
            dbConnection.getPreparedStatement().setString(4, item.getCity());
            dbConnection.getPreparedStatement().setString(5, item.getCountry());
            dbConnection.getPreparedStatement().setInt(6, item.getPhoneNumber());
            dbConnection.getPreparedStatement().setString(7, item.getEmail());
            dbConnection.getPreparedStatement().setInt(8, item.getId());

            dbConnection.getPreparedStatement().executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Exception is occurred while adding person to database",e);
        }
        finally {
            dbConnection.closeConnection();
        }
    }

    public void removePersonFromDB(Person item)  {
        try {
            dbConnection.openConnection();
            String query = "DELETE FROM persons where id=" + item.getId();
            dbConnection.setPreparedStatement(dbConnection.getConnection().prepareStatement(query));

            dbConnection.getPreparedStatement().executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Exception is occurred while deleting person from database",e);
        } finally {
            dbConnection.closeConnection();
        }
    }

    public void updatePersonInDB(Person item) {
        try {
            dbConnection.openConnection();

            String query = "UPDATE persons set name=?, lastname=?, street=?, city=?, country=?, phone=?, email=? where id=?";
            dbConnection.setPreparedStatement(dbConnection.getConnection().prepareStatement(query));

            dbConnection.getPreparedStatement().setString(1, item.getName());
            dbConnection.getPreparedStatement().setString(2, item.getLastName());
            dbConnection.getPreparedStatement().setString(3, item.getStreet());
            dbConnection.getPreparedStatement().setString(4, item.getCity());
            dbConnection.getPreparedStatement().setString(5, item.getCountry());
            dbConnection.getPreparedStatement().setInt(6, item.getPhoneNumber());
            dbConnection.getPreparedStatement().setString(7, item.getEmail());
            dbConnection.getPreparedStatement().setInt(8, item.getId());

            dbConnection.getPreparedStatement().executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Exception is occurred while updating person in database",e);
        } finally {
            dbConnection.closeConnection();
        }
    }

    public void updatePersonInDBWithoutPhone(Person item) {
        try {
            dbConnection.openConnection();

            String query = "UPDATE persons set name=?, lastname=?, street=?, city=?, country=?, email=? where id=?";
            dbConnection.setPreparedStatement(dbConnection.getConnection().prepareStatement(query));

            dbConnection.getPreparedStatement().setString(1, item.getName());
            dbConnection.getPreparedStatement().setString(2, item.getLastName());
            dbConnection.getPreparedStatement().setString(3, item.getStreet());
            dbConnection.getPreparedStatement().setString(4, item.getCity());
            dbConnection.getPreparedStatement().setString(5, item.getCountry());
            dbConnection.getPreparedStatement().setString(6, item.getEmail());
            dbConnection.getPreparedStatement().setInt(7, item.getId());

            dbConnection.getPreparedStatement().executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Exception is occurred while updating person in database",e);
        } finally {
            dbConnection.closeConnection();
        }
    }
}
