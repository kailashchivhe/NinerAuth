package com.kai.shoppingcart.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    String id;
    String name;
    double price;
    int discount;
    String uri;
    String region;
    int quantity;

    protected Item(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readDouble();
        discount = in.readInt();
        uri = in.readString();
        region = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Item(String id, String name, double price, int discount, String uri, String region) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.uri = uri;
        this.region = region;
    }

    public Item(String name, double price, int discount, String uri) {
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(discount);
        dest.writeString(uri);
        dest.writeString(region);
        dest.writeInt(quantity);
    }
}
