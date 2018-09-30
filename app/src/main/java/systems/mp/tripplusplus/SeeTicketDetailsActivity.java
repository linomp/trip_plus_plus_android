package systems.mp.tripplusplus;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SeeTicketDetailsActivity extends AppCompatActivity {

    TextView companyNameTextView, price_detail_view, services_detail_view,
            route_details_concat, departure_detail_view, current_purchase_text_view;
    Button addToCartBtn, checkoutBtn;
    Spinner seatsDropdown;
    ArrayList<Integer> availableSeats;
    ArrayList<Integer> selectedSeats;
    Integer currentSelectedSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ticket_details);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.star_container), iconFont);

        availableSeats = new ArrayList<>(40);
        selectedSeats = new ArrayList<>(4);

        TicketDataMocker dataSource = new TicketDataMocker();
        String idToLookup = getIntent().getExtras().getString("TICKET_ID");
        Ticket selectedTicket = dataSource.getTicketById( idToLookup );

        // TODO: implement more specific departure info, link to google maps to visualize map
        // get info from ticket; company, rating, departure, price
        showBusCompanyRating(selectedTicket);
        companyNameTextView = (TextView) findViewById(R.id.company_name_text_view);
        companyNameTextView.setText( selectedTicket.getCompanyName() );

        price_detail_view = (TextView) findViewById(R.id.price_detail_view);
        price_detail_view.setText( "$ " + String.valueOf(selectedTicket.getPrice()) );

        services_detail_view = (TextView) findViewById(R.id.services_detail_view);
        services_detail_view.setText( selectedTicket.getServices() );

        route_details_concat = (TextView) findViewById(R.id.route_details_concat);
        route_details_concat.setText( selectedTicket.getRouteString().toUpperCase() );

        departure_detail_view = (TextView) findViewById(R.id.departure_detail_view);
        departure_detail_view.setText( selectedTicket.getDepartureDateTimeString() );

        current_purchase_text_view = (TextView) findViewById(R.id.current_purchase_text_view);


        // TODO: implement bus layout visual seat-picking mechanism (in a separate fragment just like datePicker)
        // display list of available seats
        seatsDropdown = findViewById(R.id.spinner1);
         for(int i = 1; i < 37; i++) {
            availableSeats.add(i);
        }
        updateSeatDropdown(availableSeats);

        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);
        checkoutBtn = (Button) findViewById(R.id.checkoutBtn);
        addToCartBtn.setOnClickListener(e->{
            if(currentSelectedSeat != null) {
                selectedSeats.add(currentSelectedSeat);
                checkoutBtn.setEnabled(true);
                checkoutBtn.setBackgroundResource(R.drawable.black_bg);
                availableSeats.remove(currentSelectedSeat);
                updateSeatDropdown(availableSeats);
                current_purchase_text_view.setVisibility(View.VISIBLE);
                current_purchase_text_view.setText(String.valueOf(selectedSeats.size())
                        + " " + getResources().getString(R.string.current_purchase_msg));
                currentSelectedSeat = null;
            }
        });

        checkoutBtn.setOnClickListener(e->{
            Intent i = new Intent(this, CheckoutActivity.class);
            i.putIntegerArrayListExtra("SELECTED_SEATS", selectedSeats);
            i.putExtra("TICKET_ID", selectedTicket.getId());
            startActivity(i);
        });
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
