package georeach.smartwomensecurity.Modal;

/**
 * Created by Aaleen on 6/7/2017.
 */

public class ModalEmergency {

    String contactName, contactNumber;

    public ModalEmergency(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
