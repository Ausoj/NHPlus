package model;

public class Caretaker extends Person {

    private long cid;
    private String phoneNumber;

    public Caretaker(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;

    }

    public Caretaker(long cid, String firstName, String surname, String phoneNumber) {
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
        return "Caretaker" + "\nID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
