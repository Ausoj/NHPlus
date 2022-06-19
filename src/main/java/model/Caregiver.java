package model;

public class Caregiver extends Person {

    private long id;
    private String phoneNumber;

    public Caregiver(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;

    }

    public Caregiver(long id, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.id = id;
        this.phoneNumber = phoneNumber;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "Caregiver" + "\nID: " + this.id +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
