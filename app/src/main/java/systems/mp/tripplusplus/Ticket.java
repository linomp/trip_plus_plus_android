package systems.mp.tripplusplus;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Ticket {

    String mId;
    String mCompanyName;
    int mCompanyRating;
    double mPrice;
    Date mDeparture;
    String mTime;
    String mServices;
    String mOrigin;
    String mDestination;
    String mBusPlate;
    int mImageResId;
    int mSeatsCount;


    ArrayList<Integer> mSoldSeats;

    public Ticket(){

    }



    public Ticket(String id, String companyName, int companyRating, String price, String departureDateString, String departureTimeString, String services, String origin, String destination, int seatsCount, Object soldSeats, int imageResId, String busPlate){
        mId = id;
        mCompanyName = companyName;
        mCompanyRating = companyRating;
        mPrice = Double.parseDouble(price);
        mDeparture = formatIntoDate(departureDateString);
        mTime = departureTimeString;
        mServices = services;
        mOrigin = origin;
        mDestination = destination;
        mImageResId = imageResId;
        mBusPlate = busPlate;
        mSeatsCount = seatsCount;
        mSoldSeats = new ArrayList<Integer>(mSeatsCount);
        try {
            ArrayList<Long> test = (ArrayList<Long>) soldSeats;
            for (Long o : test){
                mSoldSeats.add(o.intValue());
            }
        } catch(Exception e){
            Log.d("SEATS", e.toString());
        }
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
        //SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm");
        //String dateString = sdf.format(mDeparture);
        return getDepartureDateString() + " " + getTime();
    }

    public String getDepartureTimeString() {
        return getTime();
    }

    public String getDepartureDateString() {
        SimpleDateFormat sdf=new SimpleDateFormat("mm/dd/yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String dateString = sdf.format(mDeparture);
        return dateString;
    }

    private Date formatIntoDate(String departureDateString) {
        Date date = new Date();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date = parser.parse(departureDateString);
        }catch(Exception e){
            Log.d("PARSE_EXCEPTION", e.toString());
        }
        return date;
    }


    public ArrayList<Integer> getAvailableSeats() {
        ArrayList<Integer> availableSeats = new ArrayList<>(30);

        // filter out the sold seats
        for(int i = 1; i < mSeatsCount+1; i++){
            if(!mSoldSeats.contains(i)){
                availableSeats.add(i);
            }
        }

        return availableSeats;
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

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setBusPlate(String busPlate) {
        mBusPlate = busPlate;
    }

    public String getBusPlate() {
        return mBusPlate;
    }

    public int getSeatsCount() {
        return mSeatsCount;
    }

    public void setSeatsCount(int seatsCount) {
        mSeatsCount = seatsCount;
    }

    public ArrayList<Integer> getSoldSeats() {
        return mSoldSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "mId='" + mId + '\'' +
                ", mCompanyName='" + mCompanyName + '\'' +
                ", mCompanyRating=" + mCompanyRating +
                ", mPrice=" + mPrice +
                ", mDeparture=" + mDeparture +
                ", mTime=" + mTime +
                ", mServices='" + mServices + '\'' +
                ", mOrigin='" + mOrigin + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mSeatsCount='" + mSeatsCount + '\'' +
                ", mBusPlate='" + mBusPlate + '\'' +
                ", mImageResId=" + mImageResId +
                '}';
    }

}
