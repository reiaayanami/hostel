package org.example;

public class Client {
    private int id;
    private String fullName;
    private String passport;
    private String phone;
    private String email;

    public Client() {
    }

    public Client(int id, String fullName, String passport, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.passport = passport;
        this.phone = phone;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassport() { return passport; }
    public void setPassport(String passport) { this.passport = passport; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}