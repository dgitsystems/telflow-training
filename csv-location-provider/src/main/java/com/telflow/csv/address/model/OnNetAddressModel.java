package com.telflow.csv.address.model;

/**
 * Data model for OnNet Address
 *
 */
public class OnNetAddressModel {
    
    private String siteCode;
    
    private String name;
    
    private String roadNumber1;
    
    private String roadName;
    
    private String roadTypeCode;
    
    private String cityOrTown;
    
    private String country;
    
    private String postcode;
    
    private String parcelID;
    
    private String localityName;
    
    private String status;
    
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoadNumber1() {
        return roadNumber1;
    }

    public void setRoadNumber1(String roadNumber1) {
        this.roadNumber1 = roadNumber1;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadTypeCode() {
        return roadTypeCode;
    }

    public void setRoadTypeCode(String roadTypeCode) {
        this.roadTypeCode = roadTypeCode;
    }

    public String getCityOrTown() {
        return cityOrTown;
    }

    public void setCityOrTown(String cityOrTown) {
        this.cityOrTown = cityOrTown;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return formattedAddress
     */
    public String getFormattedAddress() {
        String streetAddress = String.format("%s %s %s, %s, %s %s",
                this.roadNumber1, this.roadName, this.roadTypeCode, this.localityName, this.cityOrTown, this.postcode);

        return streetAddress;
    }
}
