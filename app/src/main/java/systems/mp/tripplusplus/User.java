package systems.mp.tripplusplus;

public class User {

    private String mFirstName, mLastName, mId, mEmail;
    private boolean mIsCompany;

    public User() {
    }

    public User(String firstName, String lastName, String id, String email) {
        mFirstName = firstName;
        mLastName = lastName;
        mId = id;
        mEmail = email;
        mIsCompany = false;
    }

    public String getFirstName() {

        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public boolean isCompany(){return mIsCompany;};
}
