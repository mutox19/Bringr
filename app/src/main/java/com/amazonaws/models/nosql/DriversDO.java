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

@DynamoDBTable(tableName = "fooddelivery-mobilehub-1629399668-Drivers")

public class DriversDO {
    private String _userId;
    private String _cREATEDAT;
    private String _eMAIL;
    private String _fID;
    private String _fIRSTNAME;
    private String _lASTNAME;
    private String _phoneNum;
    private String _uID;
    private String _uSERNAME;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "email-find")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "CREATED_AT")
    public String getCREATEDAT() {
        return _cREATEDAT;
    }

    public void setCREATEDAT(final String _cREATEDAT) {
        this._cREATEDAT = _cREATEDAT;
    }
    @DynamoDBIndexRangeKey(attributeName = "EMAIL", globalSecondaryIndexName = "email-find")
    public String getEMAIL() {
        return _eMAIL;
    }

    public void setEMAIL(final String _eMAIL) {
        this._eMAIL = _eMAIL;
    }
    @DynamoDBAttribute(attributeName = "FID")
    public String getFID() {
        return _fID;
    }

    public void setFID(final String _fID) {
        this._fID = _fID;
    }
    @DynamoDBAttribute(attributeName = "FIRSTNAME")
    public String getFIRSTNAME() {
        return _fIRSTNAME;
    }

    public void setFIRSTNAME(final String _fIRSTNAME) {
        this._fIRSTNAME = _fIRSTNAME;
    }
    @DynamoDBAttribute(attributeName = "LASTNAME")
    public String getLASTNAME() {
        return _lASTNAME;
    }

    public void setLASTNAME(final String _lASTNAME) {
        this._lASTNAME = _lASTNAME;
    }
    @DynamoDBAttribute(attributeName = "PhoneNum")
    public String getPhoneNum() {
        return _phoneNum;
    }

    public void setPhoneNum(final String _phoneNum) {
        this._phoneNum = _phoneNum;
    }
    @DynamoDBAttribute(attributeName = "UID")
    public String getUID() {
        return _uID;
    }

    public void setUID(final String _uID) {
        this._uID = _uID;
    }
    @DynamoDBAttribute(attributeName = "USERNAME")
    public String getUSERNAME() {
        return _uSERNAME;
    }

    public void setUSERNAME(final String _uSERNAME) {
        this._uSERNAME = _uSERNAME;
    }

}
