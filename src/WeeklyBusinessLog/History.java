package WeeklyBusinessLog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Szady
 */
public class History {
    
    String date, description, user;

    public History() {
    }

    public History(String date, String description, String user) {
        this.date = date;
        this.description = description;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "History{" + "date=" + date + ", description=" + description + ", user=" + user + '}';
    }
    
    
    
}
