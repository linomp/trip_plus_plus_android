package systems.mp.tripplusplus;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeeTicketDetailsActivity extends AppCompatActivity {

    TextView companyNameTextView, price_detail_view, services_detail_view,
            route_details_concat, departure_detail_view, current_purchase_text_view;
    Button addToCartBtn, checkoutBtn;
    Spinner seatsDropdown;
    ArrayList<Integer> availableSeats;
    ArrayList<Integer> selectedSeats;
    Integer currentSelectedSeat;
    Ticket mSelectedTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ticket_details);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.star_container), iconFont);

        availableSeats = new ArrayList<>(40);
        selectedSeats = new ArrayList<>(4);

        // TODO: implement more specific departure info, link to google maps to visualize map
        companyNameTextView = (TextView) findViewById(R.id.company_name_text_view);
        price_detail_view = (TextView) findViewById(R.id.price_detail_view);
        services_detail_view = (TextView) findViewById(R.id.services_detail_view);
        route_details_concat = (TextView) findViewById(R.id.route_details_concat);
        departure_detail_view = (TextView) findViewById(R.id.departure_detail_view);
        current_purchase_text_view = (TextView) findViewById(R.id.current_purchase_text_view);

        // set initial texts to "loading.."
        companyNameTextView.setText( getResources().getString(R.string.loading_msg) );
        price_detail_view.setText( getResources().getString(R.string.loading_msg) );
        services_detail_view.setText( getResources().getString(R.string.loading_msg) );
        route_details_concat.setText( getResources().getString(R.string.loading_msg) );
        departure_detail_view.setText( getResources().getString(R.string.loading_msg) );


        // TODO: implement bus layout visual seat-picking mechanism (in a separate fragment just like datePicker)
        // display list of available seats
        seatsDropdown = findViewById(R.id.spinner1);
        //updateSeatDropdown(availableSeats);

        //TicketDataMocker dataSource = new TicketDataMocker();
        //String idToLookup = getIntent().getExtras().getString("TICKET_ID");
        //Ticket selectedTicket = dataSource.getTicketById( idToLookup );

        String idToLookup = getIntent().getExtras().getString("TICKET_ID");
        mSelectedTicket = new Ticket();

        FirebaseFirestore.getInstance().collection("routes")
            .whereEqualTo(FieldPath.documentId(), idToLookup)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    mSelectedTicket = getTicketFromDoc(querySnapshot.getDocuments());
                    Log.d("FB_TEST", mSelectedTicket.toString());
                    populateTextViews(mSelectedTicket);
                    populateBusData(mSelectedTicket);
                    //updateAvailableSeats(mSelectedTicket);
                }
            });


        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);
        checkoutBtn = (Button) findViewById(R.id.checkoutBtn);
        addToCartBtn.setOnClickListener(e->{
            if(currentSelectedSeat != null) {
                selectedSeats.add(currentSelectedSeat);
                checkoutBtn.setEnabled(true);
                checkoutBtn.setBackgroundResource(R.drawable.black_bg);
                availableSeats.remove(currentSelectedSeat);
                //updateSeatDropdown(availableSeats);
                current_purchase_text_view.setVisibility(View.VISIBLE);
                current_purchase_text_view.setText(String.valueOf(selectedSeats.size())
                        + " " + getResources().getString(R.string.current_purchase_msg));
                currentSelectedSeat = null;
            }
        });

        checkoutBtn.setOnClickListener(e->{
            Intent i = new Intent(this, CheckoutActivity.class);
            i.putIntegerArrayListExtra("SELECTED_SEATS", selectedSeats);
            i.putExtra("TICKET_ID", mSelectedTicket.getId());
            startActivity(i);
        });
    }

    public Ticket getTicketFromDoc(List<DocumentSnapshot> documentSnapshotsList){
        Ticket ticketFromDoc = new Ticket();
        if(!documentSnapshotsList.isEmpty()){
            DocumentSnapshot doc = documentSnapshotsList.get(0);
            ticketFromDoc = new Ticket(doc.getId(),
                    doc.getString("companyName"),
                    5,
                    doc.getString("price"),
                    doc.getString("date"),
                    doc.getString("time"),
                    doc.getString("busServices"),
                    doc.getString("origin"),
                    doc.getString("destination"),
                    Integer.parseInt(doc.getString("availableSeats")),
                    doc.get("soldSeats"),
                    R.drawable.logo_v_1,
                    doc.getString("busPlate"));
            //Log.d("FB_TEST", doc.getString("availableSeats"));
            Log.d("FB_TEST", doc.get("soldSeats").getClass().toString());
        }
        return ticketFromDoc;
    }

    private void populateBusData(Ticket selectedTicket){
        try {
            FirebaseFirestore.getInstance().collection("buses")
                    .whereEqualTo("plate", selectedTicket.getBusPlate())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            Bus bus = getBusFromDoc(querySnapshot.getDocuments());
                            Log.d("FB_TEST", bus.toString());
                            services_detail_view.setText(bus.getServices());
                        }
                    });
        }catch(Exception e){
            Log.d("POPULATE_EXCEPTION", e.toString());
        }
    }

    private Bus getBusFromDoc( List<DocumentSnapshot> documentSnapshotsList){
        Bus busFromDoc = new Bus();
        if(!documentSnapshotsList.isEmpty()){
            DocumentSnapshot doc = documentSnapshotsList.get(0);
            busFromDoc = new Bus(
                    doc.getString("number"),
                    doc.getString("plate"),
                    doc.getString("services"),
                    Integer.parseInt(doc.getString("seatCount")),
                    doc.getString("owner")
            );
        }
        return busFromDoc;
    }

    private void populateTextViews(Ticket selectedTicket){
        try {
            showBusCompanyRating(selectedTicket);
            companyNameTextView.setText(selectedTicket.getCompanyName());
            price_detail_view.setText("$ " + String.valueOf(selectedTicket.getPrice()));
            //services_detail_view.setText(selectedTicket.getServices());
            route_details_concat.setText(selectedTicket.getRouteString().toUpperCase());
            departure_detail_view.setText(selectedTicket.getDepartureDateTimeString());
        }catch(Exception e){
            Log.d("POPULATE_EXCEPTION", e.toString());
        }
    }

    private void updateAvailableSeats(Ticket selectedTicket){

        availableSeats = (ArrayList<Integer>) selectedTicket.getAvailableSeats().clone();

        updateSeatDropdown(availableSeats);
    }

    private void updateSeatDropdown(ArrayList<Integer> items){
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        seatsDropdown.setAdapter(adapter);
        seatsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelectedSeat = (Integer) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentSelectedSeat = null;
            }
        });
    }

    private void showBusCompanyRating(Ticket ticket){
        TextView[] stars = new TextView[]{
                (TextView) findViewById(R.id.star_1),
                (TextView) findViewById(R.id.star_2),
                (TextView) findViewById(R.id.star_3),
                (TextView) findViewById(R.id.star_4),
                (TextView) findViewById(R.id.star_5),
        };
        for (int i = 0; i < ticket.getCompanyRating(); i++){
            stars[i].setVisibility(View.VISIBLE);
        }
    }

}
