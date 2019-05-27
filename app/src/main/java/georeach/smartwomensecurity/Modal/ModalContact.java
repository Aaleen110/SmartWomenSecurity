package georeach.smartwomensecurity.Modal;

/**
 * Created by Aaleen on 6/3/2017.
 */

public class ModalContact {

    String Name;
    String Mobile;

    public ModalContact(String name, String mobile) {
        Name = name;
        Mobile = mobile;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
