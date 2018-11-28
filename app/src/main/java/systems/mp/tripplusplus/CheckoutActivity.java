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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends Activity {

    TextView company, price, seats, route, departure, visa, mastercard, dollar;
    ArrayList<Integer> selectedSeats;
    Ticket mSelectedTicket;
    private FirebaseAuth mAuth;
    Button payBtn;
    enum PaymentMethod{CREDIT_CARD, DEBIT_CARD, E_WALLET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_checkout), iconFont);

        selectedSeats = getIntent().getExtras().getIntegerArrayList("SELECTED_SEATS");
        String idToLookup = getIntent().getExtras().getString("TICKET_ID");

        company = (TextView) findViewById(R.id.checkout_company_name);
        departure = (TextView) findViewById(R.id.checkout_departure_time);
        route = (TextView) findViewById(R.id.checkout_route);
        seats = (TextView) findViewById(R.id.checkout_seats);
        price = (TextView) findViewById(R.id.checkout_total_price);

        company.setText( getResources().getString(R.string.loading_msg) );
        departure.setText( getResources().getString(R.string.loading_msg) );
        route.setText( getResources().getString(R.string.loading_msg) );
        seats.setText( "..." );
        price.setText( "..." );

        //TicketDataMocker dataSource = new TicketDataMocker();
        //Ticket selectedTicket = dataSource.getTicketById( idToLookup );
        mSelectedTicket = new Ticket();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("routes")
                .whereEqualTo(FieldPath.documentId(), idToLookup)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        mSelectedTicket = getTicketFromDoc(querySnapshot.getDocuments());
                        Log.d("FB_TEST", mSelectedTicket.toString());
                        populateTextViews(mSelectedTicket);
                    }
                });


        mAuth = FirebaseAuth.getInstance();

        visa = (TextView) findViewById(R.id.visa);
        mastercard = (TextView) findViewById(R.id.mastercard);
        dollar = (TextView) findViewById(R.id.dollar);

        visa.setOnClickListener(e->{
            if(confirmPayment(PaymentMethod.CREDIT_CARD)) {
                uploadTransactionToFirestore();
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, NewQueryActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.payment_error_msg), Toast.LENGTH_LONG).show();
            }
        });

        mastercard.setOnClickListener(e->{
            if(confirmPayment(PaymentMethod.DEBIT_CARD)) {
                uploadTransactionToFirestore();
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, NewQueryActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.payment_error_msg), Toast.LENGTH_LONG).show();
            }
        });


        dollar.setOnClickListener(e->{
            if(confirmPayment(PaymentMethod.E_WALLET)) {
                uploadTransactionToFirestore();
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.demo_complete_msg), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, NewQueryActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(CheckoutActivity.this, getResources().getString(R.string.payment_error_msg), Toast.LENGTH_LONG).show();
            }
        });

    }

    // TODO
    // IMPLEMENT PAYMENT GATEWAY!!!!
    public boolean confirmPayment(PaymentMethod method){
        Log.d("PAYMENT", method.toString());
        switch(method){
            case E_WALLET:
                return false;
            case DEBIT_CARD:
                return true;
            case CREDIT_CARD:
                return true;
            default:
                return true;
        }
    }

    public void uploadTransactionToFirestore(){
        ArrayList<Integer> updatedSoldSeatsList = mSelectedTicket.getSoldSeats();
        updatedSoldSeatsList.addAll(selectedSeats);

        FirebaseFirestore.getInstance().collection("routes")
            .document(mSelectedTicket.getId())
            .update("soldSeats", updatedSoldSeatsList)
            .addOnSuccessListener(v ->
                Log.d("UPD", "DocumentSnapshot successfully updated!")
            );

        Map<String, Object> transaction = new HashMap<>();

        try {
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("UPDFB", user.getEmail());
            transaction.put("userEmail", user.getEmail());
        } catch(Exception e){
            Log.d("UPDFB", e.toString());
        }
        transaction.put("seats", selectedSeats);
        transaction.put("origin", mSelectedTicket.getOrigin());
        transaction.put("destination", mSelectedTicket.getDestination());
        transaction.put("departure", mSelectedTicket.getDepartureDateTimeString());
        transaction.put("company", mSelectedTicket.getCompanyName());

        FirebaseFirestore.getInstance().collection("transactions").document()
                .set(transaction, SetOptions.merge())
                .addOnSuccessListener(v ->
                    Log.d("UPDFB", "DocumentSnapshot successfully updated!")
                ).addOnFailureListener( e->
                    Log.w("UPD", "Error writing document", e)
                );
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
        }
        return ticketFromDoc;
    }

    private void populateTextViews(Ticket selectedTicket){
        try {
            double totalPrice = selectedTicket.getPrice() * selectedSeats.size();
            company.setText( selectedTicket.getCompanyName() );
            departure.setText( selectedTicket.getDepartureDateTimeString() );
            route.setText( selectedTicket.getRouteString().toUpperCase() );
            String seatsString = "";
            for(Integer i : selectedSeats){
                seatsString += ", " + String.valueOf(i);
            }
            seats.setText( getResources().getString(R.string.seats_field_name) + " " +
                    seatsString.substring(2,seatsString.length()) );
            price.setText( "$ " + String.valueOf(totalPrice) );
        }catch(Exception e){
            Log.d("POPULATE_EXCEPTION", e.toString());
        }
    }

}
