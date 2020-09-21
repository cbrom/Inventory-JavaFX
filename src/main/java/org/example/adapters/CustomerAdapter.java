package org.example.adapters;

import com.google.gson.annotations.SerializedName;

public class CustomerAdapter {
    @SerializedName("age")
    private final Integer age;
    @SerializedName("name")
    private final String name;

    @SerializedName("_id")
    private final String id;
    @SerializedName("status")
    private final String status;
    @SerializedName("customer_class")
    private final String customerClass;
    @SerializedName("phone_number")
    private final String phoneNumber;
    @SerializedName("location")
    private final String location;
    @SerializedName("stage")
    private final String stage;
    @SerializedName("result")
    private final String result;
    @SerializedName("level_price")
    private final Integer levelPrice;
    @SerializedName("discount")
    private final Integer discount;
    @SerializedName("cost")
    private final Integer cost;
    @SerializedName("comment")
    private final String comment;
    @SerializedName("image_url")
    private final String imageUrl;

    public CustomerAdapter(){
        this(0, "", "", "", "", "", "", "",
                "", 0, 0, 0, "", "");
    }

    public CustomerAdapter(int age, String name, String id, String status, String customerClass, String phoneNumber,
                    String location, String stage, String result, int levelPrice, int discount, int cost,
                    String comment, String imageUrl) {
        this.age = age;
        this.name = name;
        this.id = id;
        this.status = status;
        this.customerClass = customerClass;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.stage = stage;
        this.result = result;
        this.levelPrice = levelPrice;
        this.discount = discount;
        this.cost = cost;
        this.comment = comment;
        this.imageUrl = imageUrl;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getCustomerClass() {
        return customerClass;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getLocation() {
        return location;
    }
    public String getStage() {
        return stage;
    }
    public String getResult() {
        return result;
    }
    public int getLevelPrice() {
        return levelPrice;
    }
    public int getDiscount() {
        return discount;
    }
    public int getCost() {
        return cost;
    }
    public String getComment() {
        return comment;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}