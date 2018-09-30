package systems.mp.tripplusplus;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TicketDataMocker {

    private List<Ticket> allTheTickets;

    public TicketDataMocker(){
        allTheTickets = new ArrayList<>();
        allTheTickets.add( new Ticket("00","Reina del Camino", 4, 10.00, new Date(),"TV, WiFi, WC", "quito", "manta", R.drawable.rdc_logo) );
        allTheTickets.add( new Ticket("01","TransEsmeraldas", 4, 6.00, new Date(),"TV, WC", "quito", "esmeraldas", R.drawable.trans_esme_logo) );
        allTheTickets.add( new Ticket("02","Flota Imbabura", 3, 10.00, new Date(),"TV", "quito", "manta", R.drawable.flota_logo) );
        allTheTickets.add( new Ticket("03","Reina del Camino", 4, 15.00, new Date(),"TV, WiFi, WC", "quito", "guayaquil", R.drawable.rdc_logo) );
    }

    public Ticket getTicketById(String id){
        for (Ticket t : allTheTickets){
            if(t.getId().equals(id)){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Ticket> getTicketsByRoute(String origin, String destination){
        ArrayList<Ticket> results = new ArrayList<>();
        for (Ticket t : allTheTickets){
            //Log.d("SEARCH_MSG", t.getOrigin() + "-" + origin + "-" + t.getDestination()+ "-" + destination);
            if( t.getOrigin().replaceAll("\\s+","").equalsIgnoreCase(origin.replaceAll("\\s+",""))
                    && t.getDestination().replaceAll("\\s+","").equalsIgnoreCase(destination.replaceAll("\\s+","")) ){
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Ticket> getTicketsByRouteAndDate(String origin, String destination, String dateString){
        ArrayList<Ticket> results = new ArrayList<>();
        for (Ticket t : allTheTickets){
            //Log.d("SEARCH_MSG", t.getOrigin() + "-" + origin + "-" + t.getDestination()+ "-" + destination
            // + t.getDepartureDateString()+);
            //Log.d("DATE_TEST", t.getDepartureDateString() + " - " + dateString);
            if( t.getOrigin().replaceAll("\\s+","").equalsIgnoreCase(origin.replaceAll("\\s+",""))
                    && t.getDestination().replaceAll("\\s+","").equalsIgnoreCase(destination.replaceAll("\\s+",""))
                    && t.getDepartureDateString().equals(dateString)){
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Ticket> getAllTickets(){
        return (ArrayList<Ticket>) allTheTickets;
    }

}
