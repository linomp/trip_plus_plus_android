package systems.mp.tripplusplus;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryResultsActivity extends AppCompatActivity {

    TextView route_text_view, travel_date_text_view, results_count_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_results);

        route_text_view = (TextView) findViewById(R.id.route_text_view);
        travel_date_text_view = (TextView) findViewById(R.id.travel_date_text_view);
        results_count_text_view = (TextView) findViewById(R.id.results_count_text_view);

        String originToLookup = getIntent().getExtras().getString("ORIGIN").replaceAll("\\s+","").toLowerCase();
        String destinationToLookup = getIntent().getExtras().getString("DESTINATION").replaceAll("\\s+","").toLowerCase();
        String dateStringToLookup = getIntent().getExtras().getString("DATE_STRING").replaceAll("\\s+","").toLowerCase();

        route_text_view.setText(originToLookup.trim().toUpperCase() + " - " + destinationToLookup.trim().toUpperCase());
        travel_date_text_view.setText( dateStringToLookup );

        // perform firebase query
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("routes")
                .whereEqualTo("origin", originToLookup)
                .whereEqualTo("destination", destinationToLookup)
                .whereEqualTo("date", dateStringToLookup)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        populateListViewWithResults(querySnapshot.getDocuments());
                    }
                });

        // get instance of data source, execute query
        //TicketDataMocker dataSource = new TicketDataMocker();
        //List<Ticket> results = dataSource.getTicketsByRouteAndDate(originToLookup, destinationToLookup, dateStringToLookup);
        //List<Ticket> results = dataSource.getTicketsByRoute(originToLookup, destinationToLookup);
        //List<Ticket> results = dataSource.getAllTickets();
    }


    void populateListViewWithResults(List<DocumentSnapshot> documentSnapshotsList){

        List<Ticket> results = processResults(documentSnapshotsList);

        results_count_text_view.setText( String.valueOf(results.size()) + " " + results_count_text_view.getText().toString() );
        // populate list view with results
        TicketAdapter adapter = new TicketAdapter(this, results);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        // attach event listeners to enable jumping to detailed view of a given result
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <? > arg0, View arg1, int position, long id) {
                Intent i = new Intent(QueryResultsActivity.this, SeeTicketDetailsActivity.class);
                TextView idTextView = arg1.findViewById(R.id.ticket_id_text_view);
                i.putExtra("TICKET_ID", idTextView.getText());
                startActivity(i);
            }
        });
    }

    List<Ticket> processResults(List<DocumentSnapshot> documentSnapshotsList){
        ArrayList<Ticket> results = new ArrayList<>();
        for (DocumentSnapshot doc : documentSnapshotsList){
            Ticket ticketFromDoc = new Ticket(doc.getId(),
                    doc.getString("companyName"),
                    5,
                    doc.getString("price"),
                    doc.getString("date"),
                    doc.getString("time"),
                    "",
                    doc.getString("origin"),
                    doc.getString("destination"),
                    Integer.parseInt(doc.getString("availableSeats")),
                    doc.get("soldSeats"),
                    R.drawable.logo_v_1,
                    doc.getString("busPlate"));
            //Log.d("FB_TEST", ticketFromDoc.toString());
            results.add(ticketFromDoc);
        }
        return results;
    }

}
