package shanefuzz.arwebsiteevaluator;

import java.io.Serializable;


/**
 * Contains user details, and give access to those details through set of getters and setters
 */
public class UserProfile implements Serializable {

    private int _id;
    private String _name;
    private String _email;
    private String _pswrd;
    private byte[] _image;


    public UserProfile() {
    }

    public UserProfile(String name, String email, String pswrd) {
        this._name = name;
        this._email = email;
        this._pswrd = pswrd;


    }

    public byte[] get_image() {

        return _image;
    }

    public void set_image(byte[] _image) {
        this._image = _image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_pswrd() {
        return _pswrd;
    }

    public void set_pswrd(String _pswrd) {
        this._pswrd = _pswrd;
    }


}
