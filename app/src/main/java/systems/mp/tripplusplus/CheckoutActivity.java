package systems.mp.tripplusplus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckoutActivity extends Activity {

    TextView company, price, seats, route, departure, visa, mastercard, dollar;
    Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_checkout), iconFont);

        ArrayList<Integer> selectedSeats = getIntent().getExtras().getIntegerArrayList("SELECTED_SEATS");
        String idToLookup = getIntent().getExtras().getString("TICKET_ID");

        TicketDataMocker dataSource = new TicketDataMocker();
        Ticket selectedTicket = dataSource.getTicketById( idToLookup );

        double totalPrice = selectedTicket.getPrice() * selectedSeats.size();

        company = (TextView) findViewById(R.id.checkout_company_name);
        company.setText( selectedTicket.getCompanyName() );

        departure = (TextView) findViewById(R.id.checkout_departure_time);
        departure.setText( selectedTicket.getDepartureDateTimeString() );

        route = (TextView) findViewById(R.id.checkout_route);
        route.setText( selectedTicket.getRouteString().toUpperCase() );

        seats = (TextView) findViewById(R.id.checkout_seats);
        String seatsString = "";
        for(Integer i : selectedSeats){
            seatsString += ", " +String.valueOf(i);
        }
        seats.setText( getResources().getString(R.string.seats_field_name) + " " +
                seatsString.substring(2,seatsString.length()) );

        price = (TextView) findViewById(R.id.checkout_total_price);
        price.setText( "$ " + String.valueOf(totalPrice) );

        visa = (TextView) findViewById(R.id.visa);
        mastercard = (TextView) findViewById(R.id.mastercard);
        dollar = (TextView) findViewById(R.id.dollar);

        visa.setOnClickListener(e->{
            //Log.d("CARD", "visa");
            Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, NewQueryActivity.class);
            startActivity(i);
        });

        mastercard.setOnClickListener(e->{
            Log.d("CARD", "master");
            Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, NewQueryActivity.class);
            startActivity(i);
        });


        dollar.setOnClickListener(e->{
            Log.d("CARD", "dollar");
            Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, NewQueryActivity.class);
            startActivity(i);
        });

    }
}
