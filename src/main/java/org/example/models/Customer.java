package org.example.models;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.database.Database;


import java.sql.*;
import java.util.ArrayList;

public class Customer {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty status;
    private final SimpleStringProperty name;
    private final SimpleStringProperty customerClass;
    private SimpleIntegerProperty age;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty location;
    private SimpleStringProperty stage;
    private SimpleStringProperty result;
    private SimpleIntegerProperty levelPrice;
    private SimpleIntegerProperty discount;
    private SimpleIntegerProperty cost;
    private SimpleStringProperty comment;
    private SimpleStringProperty imageUrl;

    public Customer() {
        this("", "", "", 0, "", "", "", "", 0, 0, 0, "", "");
    }

    public void copyFromCustomer(Customer c) {
        this.setId(c.getId());
        this.setName(c.getName());
        this.setAge(c.getAge());
        this.setPhoneNumber(c.getPhoneNumber());
        this.setLocation(c.getLocation());
        this.setStatus(c.getStatus());
        this.setCustomerClass(c.getCustomerClass());
        this.setStage(c.getStage());
        this.setResult(c.getResult());
        this.setLevelPrice(c.getLevelPrice());
        this.setDiscount(c.getDiscount());
        this.setCost(c.getCost());
        this.setComment(c.getComment());
        this.setImageUrl(c.getImageUrl());
    }

    public void empty() {
        this.setId(-1);
        this.setName("");
        this.setAge(0);
        this.setPhoneNumber("");
        this.setLocation("");
        this.setStatus("");
        this.setCustomerClass("");
        this.setStage("");
        this.setResult("");
        this.setLevelPrice(0);
        this.setDiscount(0);
        this.setCost(0);
        this.setComment("");
        this.setImageUrl("");
    }


    public Customer(String status, String name, String customerClass, int age, String phoneNumber, String location,
                    String stage, String result, int levelPrice, int discount, int cost, String comment, String imageUrl) {
        this.id = new SimpleIntegerProperty(-1);
        this.status = new SimpleStringProperty(status);
        this.name = new SimpleStringProperty(name);
        this.customerClass = new SimpleStringProperty(customerClass);
        this.age = new SimpleIntegerProperty(age);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.location = new SimpleStringProperty(location);
        this.stage = new SimpleStringProperty(stage);
        this.result = new SimpleStringProperty(result);
        this.levelPrice = new SimpleIntegerProperty(levelPrice);
        this.discount = new SimpleIntegerProperty(discount);
        this.cost = new SimpleIntegerProperty(cost);
        this.comment = new SimpleStringProperty(comment);
        this.imageUrl = new SimpleStringProperty(imageUrl);

    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCustomerClass() {
        return customerClass.get();
    }

    public SimpleStringProperty customerClassProperty() {
        return customerClass;
    }

    public void setCustomerClass(String customerClass) {
        this.customerClass.set(customerClass);
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getStage() {
        return stage.get();
    }

    public SimpleStringProperty stageProperty() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage.set(stage);
    }

    public String getResult() {
        return result.get();
    }

    public SimpleStringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public int getLevelPrice() {
        return levelPrice.get();
    }

    public SimpleIntegerProperty levelPriceProperty() {
        return levelPrice;
    }

    public void setLevelPrice(int levelPrice) {
        this.levelPrice.set(levelPrice);
    }

    public int getDiscount() {
        return discount.get();
    }

    public SimpleIntegerProperty discountProperty() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount.set(discount);
    }

    public int getCost() {
        return cost.get();
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
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

    public String getImageUrl() {
        return imageUrl.get();
    }

    public SimpleStringProperty imageUrlProperty() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", status=" + status +
                ", name=" + name +
                ", customerClass=" + customerClass +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", location=" + location +
                ", stage=" + stage +
                ", result=" + result +
                ", levelPrice=" + levelPrice +
                ", discount=" + discount +
                ", cost=" + cost +
                ", comment=" + comment +
                '}';
    }

    public boolean save() throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO customers VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        System.out.println(this);

        stmt.setString(2, this.getStatus());
        stmt.setString(3, this.getName());
        stmt.setString(4, this.getImageUrl());
        stmt.setString(5, this.getCustomerClass());
        stmt.setInt(6, this.getAge());
        stmt.setString(7, this.getPhoneNumber());
        stmt.setString(8, this.getLocation());
        stmt.setString(9, this.getStage());
        stmt.setString(10, this.getResult());
        stmt.setInt(11, this.getLevelPrice());
        stmt.setInt(12, this.getDiscount());
        stmt.setInt(13, this.getCost());
        stmt.setString(14, this.getComment());

        int i = stmt.executeUpdate();
        stmt.close();
        return true;
    }

    public boolean update() {
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(
                    "UPDATE customers SET status=?, name=? ," +
                            "image_url=? ," +
                            "class=? ," +
                            "age=? ," +
                            "phone_number=? ," +
                            "location=? ," +
                            "stage=? ," +
                            "result=? ," +
                            "level_price=? ," +
                            "discount=? ," +
                            "cost=? ," +
                            "comment=? " +
                            "WHERE _id=?");

            stmt.setString(1, this.getStatus());
            stmt.setString(2, this.getName());
            stmt.setString(3, this.getImageUrl());
            stmt.setString(4, this.getCustomerClass());
            stmt.setInt(5, this.getAge());
            stmt.setString(6, this.getPhoneNumber());
            stmt.setString(7, this.getLocation());
            stmt.setString(8, this.getStage());
            stmt.setString(9, this.getResult());
            stmt.setInt(10, this.getLevelPrice());
            stmt.setInt(11, this.getDiscount());
            stmt.setInt(12, this.getCost());
            stmt.setString(13, this.getComment());
            stmt.setInt(14, this.getId());

            int i = stmt.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    public boolean delete() throws SQLException {
        PreparedStatement stmt = Database.getConnection().prepareStatement("DELETE FROM customers WHERE _id=?");
        stmt.setInt(1, this.getId());
        stmt.executeUpdate();
        return true;
    }

    public ArrayList<Customer> getAll() throws SQLException {
        String statement = "select * from customers";
        System.out.println("This is" + Database.getConnection());
        Statement stmt = Database.getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(statement);
        ArrayList<Customer> allCustomers = new ArrayList<>();
        while (resultSet.next()) {
            Customer customer = new Customer();
            populateCustomer(resultSet, customer);
            allCustomers.add(customer);
        }
        stmt.close();


        return allCustomers;
    }

    public Customer get(int id) throws SQLException {
        String statement = "select * from customers where _id=?";
        PreparedStatement stmt = Database.getConnection().prepareStatement("SELECT * FROM customers WHERE _id=?");
        stmt.setInt(1, id);

        ResultSet resultSet = stmt.executeQuery(statement);
        Customer customer = new Customer();
        if (resultSet.next()) {
            populateCustomer(resultSet, customer);
        }


        return customer;
    }

    public ArrayList<Customer> search(String searchTerm) throws SQLException {
        searchTerm = "%" + searchTerm + "%";
        ArrayList<Customer> resultCustomers = new ArrayList<>();
        PreparedStatement stmt = Database.getConnection().prepareStatement("SELECT * FROM customers WHERE " +
                "name LIKE ? OR status LIKE ? OR comment LIKE ? OR phone_number LIKE ? OR location LIKE ? " +
                "OR stage LIKE ? OR result LIKE ?");
        stmt.setString(1, searchTerm);
        stmt.setString(2, searchTerm);
        stmt.setString(3, searchTerm);
        stmt.setString(4, searchTerm);
        stmt.setString(5, searchTerm);
        stmt.setString(6, searchTerm);
        stmt.setString(7, searchTerm);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            Customer customer = new Customer();
            populateCustomer(resultSet, customer);
            resultCustomers.add(customer);
        }
        stmt.close();


        return resultCustomers;
    }

    private void populateCustomer(ResultSet resultSet, Customer customer) throws SQLException {
        customer.setId(resultSet.getInt("_id"));
        customer.setName(resultSet.getString("name"));
        customer.setAge(resultSet.getInt("age"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));
        customer.setLocation(resultSet.getString("location"));
        customer.setStatus(resultSet.getString("status"));
        customer.setCustomerClass(resultSet.getString("class"));
        customer.setStage(resultSet.getString("stage"));
        customer.setResult(resultSet.getString("Result"));
        customer.setLevelPrice(resultSet.getInt("level_price"));
        customer.setDiscount(resultSet.getInt("discount"));
        customer.setCost(resultSet.getInt("cost"));
        customer.setComment(resultSet.getString("comment"));
        customer.setImageUrl(resultSet.getString("image_url"));
    }
}

