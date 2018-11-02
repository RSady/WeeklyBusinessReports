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
    
    int id, incomeDate;
    String firstName, lastName, businessName;
    Address address;
    String phoneNumber, email;
    String accountType, sourceDetails, sourceSpecifics, sourceType;
    String intalledSvc;
    int surveyDate, installDate, history;
    String addOns, metricStatus;
    

    public Customer() {
    }

    public Customer(int id, int incomeDate, String firstName, String lastName, String businessName, Address address, String phoneNumber, String email, String accountType, String sourceDetails, String sourceSpecifics, String sourceType, String intalledSvc, int surveyDate, int intallDate, int history, String addOns, String metricStatus) {
        this.id = id;
        this.incomeDate = incomeDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.businessName = businessName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountType = accountType;
        this.sourceDetails = sourceDetails;
        this.sourceSpecifics = sourceSpecifics;
        this.sourceType = sourceType;
        this.intalledSvc = intalledSvc;
        this.surveyDate = surveyDate;
        this.installDate = intallDate;
        this.history = history;
        this.addOns = addOns;
        this.metricStatus = metricStatus;
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSourceDetails() {
        return sourceDetails;
    }

    public void setSourceDetails(String sourceDetails) {
        this.sourceDetails = sourceDetails;
    }

    public String getSourceSpecifics() {
        return sourceSpecifics;
    }

    public void setSourceSpecifics(String sourceSpecifics) {
        this.sourceSpecifics = sourceSpecifics;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getIntalledSvc() {
        return intalledSvc;
    }

    public void setIntalledSvc(String intalledSvc) {
        this.intalledSvc = intalledSvc;
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

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }
    
    public String getMetricStatus() {
        return metricStatus;
    }
    
    public void setMetricStatus(String status) {
        this.metricStatus = status;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", email=" + email + '}';
    }

    
}
