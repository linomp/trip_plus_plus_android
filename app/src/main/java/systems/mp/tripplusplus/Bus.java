package systems.mp.tripplusplus;

public class Bus {
    private String mNumber, mPlate, mServices, mOwner;
    int mSeatCount;


    public Bus() {
    }

    public Bus(String number, String plate, String services, int seatCount, String owner) {
        mNumber = number;
        mPlate = plate;
        mServices = services;
        mOwner = owner;
        mSeatCount = seatCount;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getPlate() {
        return mPlate;
    }

    public void setPlate(String plate) {
        mPlate = plate;
    }

    public String getServices() {
        return mServices;
    }

    public void setServices(String services) {
        mServices = services;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public int getSeatCount() {
        return mSeatCount;
    }

    public void setSeatCount(int seatCount) {
        mSeatCount = seatCount;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "mNumber='" + mNumber + '\'' +
                ", mPlate='" + mPlate + '\'' +
                ", mServices='" + mServices + '\'' +
                ", mOwner='" + mOwner + '\'' +
                ", mSeatCount=" + mSeatCount +
                '}';
    }

}
