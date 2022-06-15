package model;

public class Caregiver extends Person {

    private long cid;
    private String phoneNumber;

    public Caregiver(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;

    }

    public Caregiver(long cid, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.cid = cid;
        this.phoneNumber = phoneNumber;

    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "Caregiver" + "\nID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
