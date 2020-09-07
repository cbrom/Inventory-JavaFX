package org.example.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.database.Database;

import java.sql.*;

public class User {
    private SimpleIntegerProperty id;
    private SimpleStringProperty userName;
    private SimpleStringProperty password;
    private SimpleBooleanProperty isLoggedIn;

    public User() {
        this("", "", false);
    }

    public User(String userName, String password, Boolean isLoggedIn) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.isLoggedIn = new SimpleBooleanProperty(isLoggedIn);
        this.id = new SimpleIntegerProperty(-1);
    }

    public String getUserName() {
        return userName.get();
    }

    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn.get();
    }

    public SimpleBooleanProperty isLoggedInProperty() {
        return isLoggedIn;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName=" + userName +
                ", password=" + password +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn.set(isLoggedIn);
    }

    public boolean createUser() throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO users VALUES(?, ?, ?, ?)");
        System.out.println(this);

        stmt.setString(2, this.getUserName());
        stmt.setString(3, this.getPassword());
        stmt.setBoolean(4, false);

        int i = stmt.executeUpdate();
        stmt.close();
        return true;
    }

    public boolean isLoggedIn() throws SQLException {
        String statement = "SELECT COUNT(*) AS total FROM users WHERE is_logged_in=true";
        Statement stmt = Database.getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(statement);
        if (resultSet.next()) {
            return resultSet.getInt("total") > 0;
        }
        return false;
    }

    public boolean updateUser() {
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(
                    "UPDATE users SET user_name=?, password=?, is_logged_in=? WHERE _id=?");

            stmt.setString(1, this.getUserName());
            stmt.setString(2, this.getPassword());
            stmt.setBoolean(3, this.isLoggedIn());
            stmt.setInt(4, this.getId());
            System.out.println(stmt);

            int i = stmt.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static User getUser(String userName, String password) throws SQLException {
        String statement = "SELECT * FROM users WHERE user_name=? AND password=?";
        PreparedStatement stmt = Database.getConnection().prepareStatement(statement);
        stmt.setString(1, userName);
        stmt.setString(2, password);

        ResultSet resultSet = stmt.executeQuery();
        User user = new User();
        if (resultSet.next()) {
            user.setId(resultSet.getInt("_id"));
            user.setIsLoggedIn(resultSet.getBoolean("is_logged_in"));
            user.setUserName(resultSet.getString("user_name"));
            user.setPassword(resultSet.getString("password"));
        }

        return user;
    }

    public static User getLoggedInUser() throws SQLException {
        String statement = "SELECT * FROM users WHERE is_logged_in=?";
        PreparedStatement stmt = Database.getConnection().prepareStatement(statement);
        stmt.setBoolean(1, true);

        ResultSet resultSet = stmt.executeQuery();
        User user = new User();
        if (resultSet.next()) {
            user.setId(resultSet.getInt("_id"));
            user.setIsLoggedIn(resultSet.getBoolean("is_logged_in"));
            user.setUserName(resultSet.getString("user_name"));
            user.setPassword(resultSet.getString("password"));
        }

        return user;
    }

    public boolean login() throws SQLException {
        User user = getUser(this.getUserName(), this.getPassword());
        if (user.getId() > 0){
            user.setIsLoggedIn(true);
            PreparedStatement stmt = Database.getConnection().prepareStatement("UPDATE users SET is_logged_in=? WHERE _id=?");
            stmt.setBoolean(1, true);
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        return false;
    }

    public boolean logout() throws SQLException {
        // set is logged in to false
        this.setIsLoggedIn(false);
        PreparedStatement stmt = Database.getConnection().prepareStatement("UPDATE users SET is_logged_in=? WHERE _id=?");
        stmt.setBoolean(1, false);
        stmt.setInt(2, this.getId());
        stmt.executeUpdate();
        stmt.close();
//        this.updateUser();
        return true;
    }

    public int countUsers() throws SQLException {
        String statement = "SELECT COUNT(*) AS total FROM users";
        Statement stmt = Database.getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(statement);
        if (resultSet.next()) {
            return resultSet.getInt("total");
        }
        return 0;
    }
}
