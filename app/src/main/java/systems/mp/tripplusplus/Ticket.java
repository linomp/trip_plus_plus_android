package systems.mp.tripplusplus;

import java.text.SimpleDateFormat;
import java.util.Date;

class Ticket {

    String mId;
    String mCompanyName;
    int mCompanyRating;
    double mPrice;
    Date mDeparture;
    String mServices;
    String mOrigin;
    String mDestination;
    int mImageResId;

    public Ticket(String id, String companyName, int companyRating, double price, Date departure, String services, String origin, String destination, int imageResId){
        mId = id;
        mCompanyName = companyName;
        mCompanyRating = companyRating;
        mPrice = price;
        mDeparture = departure;
        mServices = services;
        mOrigin = origin;
        mDestination = destination;
        mImageResId = imageResId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public int getCompanyRating() {
        return mCompanyRating;
    }

    public String getServices() {
        return mServices;
    }
    public double getPrice() {
        return mPrice;
    }

    public String getDepartureDateTimeString() {
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm");
        String dateString = sdf.format(mDeparture);
        return dateString;
    }

    public String getDepartureDateString() {
        SimpleDateFormat sdf=new SimpleDateFormat("dd/M/YYYY");
        String dateString = sdf.format(mDeparture);
        return dateString;
    }

    public String getId(){
        return mId;
    }

    public Date getDeparture() {
        return mDeparture;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDestination() {
        return mDestination;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public String getRouteString(){
        return getOrigin() + " - " + getDestination();
    }

}
