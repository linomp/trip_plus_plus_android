package systems.mp.tripplusplus;

//import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketDataMocker {

    private List<Ticket> allTheTickets;

    public TicketDataMocker(){
        allTheTickets = new ArrayList<>();
       /* allTheTickets = new ArrayList<>();
        allTheTickets.add( new Ticket("00","Reina del Camino", 4, 10.00, new Date(),"00:00","TV, WiFi, WC", "quito", "manta", 30, R.drawable.rdc_logo, "000") );
        allTheTickets.add( new Ticket("01","TransEsmeraldas", 4, 6.00, new Date(),"00:00","TV, WC", "quito", "esmeraldas", 30, R.drawable.trans_esme_logo, "000") );
        allTheTickets.add( new Ticket("02","Flota Imbabura", 3, 10.00, new Date(),"00:00","TV", "quito", "manta", 30, R.drawable.flota_logo, "000") );
        allTheTickets.add( new Ticket("03","Reina del Camino", 4, 15.00, new Date(),"00:00","TV, WiFi, WC", "quito", "guayaquil", 30, R.drawable.rdc_logo, "000") );
        */
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
