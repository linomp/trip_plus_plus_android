package systems.mp.tripplusplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SeatPickerActivity extends AppCompatActivity implements OnSeatSelected {

    private Button submit;
    private ArrayList<Integer> availableSeats;
    private ArrayList<Integer> selectedSeats;
    private static final int COLUMNS = 5;
    private AirplaneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seat_picker);

        availableSeats = getIntent().getExtras().getIntegerArrayList("availableSeats");

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<40; i++) {
            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);

        adapter = new AirplaneAdapter(this, items);
        recyclerView.setAdapter(adapter);
        submit = findViewById(R.id.buttonOk);
        submit.setOnClickListener(v->{
            Intent i = new Intent(this, SeeTicketDetailsActivity.class);
            i.putExtra("selectedSeats", selectedSeats);
            i.putExtra("TICKET_ID", getIntent().getExtras().getString("TICKET_ID"));
            startActivity(i);
        });
    }

    @Override
    public void onSeatSelected(int count) {
        selectedSeats = (ArrayList<Integer>) adapter.getSelectedItems();
    }
}


