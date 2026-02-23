package org.example;

public class Client {
    private int id;
    private String fullName;
    private String passportData;
    private String phone;
    private String email;

    public Client(int id, String fullName, String passportData, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.passportData = passportData;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return fullName; // Щoб у спuску нe булo org.example.Client@...
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPassportData() { return passportData; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}