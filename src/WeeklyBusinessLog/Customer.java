/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeeklyBusinessLog;

import java.util.ArrayList;

/**
 *
 * @author Sady
 */
public class Customer {
    
    int id;
    int incomeDate, surveyDate, installDate;
    String source;
    String firstName, lastName;
    Address address;
    String phoneNumber, email, sourceDetails, specifics, sourceType, status, installedSvc, businessName;
    ArrayList<String> addons;
    History history;

    public Customer() {
    }

    public Customer(int id, int incomeDate, int surveyDate, int installDate, String source, String firstName, String lastName, Address address, String phoneNumber, String email, String sourceDetails, String specifics, String sourceType, String status, String installedSvc, String businessName, ArrayList<String> addons, History history) {
        this.id = id;
        this.incomeDate = incomeDate;
        this.surveyDate = surveyDate;
        this.installDate = installDate;
        this.source = source;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.sourceDetails = sourceDetails;
        this.specifics = specifics;
        this.sourceType = sourceType;
        this.status = status;
        this.installedSvc = installedSvc;
        this.businessName = businessName;
        this.addons = addons;
        this.history = history;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(int incomeDate) {
        this.incomeDate = incomeDate;
    }

    public int getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(int surveyDate) {
        this.surveyDate = surveyDate;
    }

    public int getInstallDate() {
        return installDate;
    }

    public void setInstallDate(int installDate) {
        this.installDate = installDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstalledSvc() {
        return installedSvc;
    }

    public void setInstalledSvc(String installedSvc) {
        this.installedSvc = installedSvc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSourceDetails() {
        return sourceDetails;
    }

    public void setSourceDetails(String sourceDetails) {
        this.sourceDetails = sourceDetails;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public ArrayList<String> getAddons() {
        return addons;
    }

    public void setAddons(ArrayList<String> addons) {
        this.addons = addons;
    }
    
    public History getHistory() {
        return history;
    }
    
    public void setHistory(History history) {
        this.history = history;
    }
}
