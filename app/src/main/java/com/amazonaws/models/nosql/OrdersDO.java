package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "fooddelivery-mobilehub-1629399668-Orders")

public class OrdersDO {
    private String _orderId;
    private String _city;
    private String _country;
    private String _customerName;
    private String _email;
    private String _fullAddress;
    private String _placeToShop;
    private String _postalCode;
    private String _whatTheyWant;

    @DynamoDBHashKey(attributeName = "orderId")
    @DynamoDBIndexHashKey(attributeName = "orderId", globalSecondaryIndexName = "email-find")
    public String getOrderId() {
        return _orderId;
    }

    public void setOrderId(final String _orderId) {
        this._orderId = _orderId;
    }
    @DynamoDBAttribute(attributeName = "City")
    public String getCity() {
        return _city;
    }

    public void setCity(final String _city) {
        this._city = _city;
    }
    @DynamoDBAttribute(attributeName = "Country")
    public String getCountry() {
        return _country;
    }

    public void setCountry(final String _country) {
        this._country = _country;
    }
    @DynamoDBAttribute(attributeName = "CustomerName")
    public String getCustomerName() {
        return _customerName;
    }

    public void setCustomerName(final String _customerName) {
        this._customerName = _customerName;
    }
    @DynamoDBIndexRangeKey(attributeName = "Email", globalSecondaryIndexName = "email-find")
    public String getEmail() {
        return _email;
    }

    public void setEmail(final String _email) {
        this._email = _email;
    }
    @DynamoDBAttribute(attributeName = "Full Address")
    public String getFullAddress() {
        return _fullAddress;
    }

    public void setFullAddress(final String _fullAddress) {
        this._fullAddress = _fullAddress;
    }
    @DynamoDBAttribute(attributeName = "PlaceToShop")
    public String getPlaceToShop() {
        return _placeToShop;
    }

    public void setPlaceToShop(final String _placeToShop) {
        this._placeToShop = _placeToShop;
    }
    @DynamoDBAttribute(attributeName = "PostalCode")
    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(final String _postalCode) {
        this._postalCode = _postalCode;
    }
    @DynamoDBAttribute(attributeName = "WhatTheyWant")
    public String getWhatTheyWant() {
        return _whatTheyWant;
    }

    public void setWhatTheyWant(final String _whatTheyWant) {
        this._whatTheyWant = _whatTheyWant;
    }

}
