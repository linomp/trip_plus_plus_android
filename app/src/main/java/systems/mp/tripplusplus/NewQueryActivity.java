package systems.mp.tripplusplus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class NewQueryActivity extends AppCompatActivity {

    Button searchButton;
    TextView originTextView, destinationTextView;
    EditText travelDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query);

        searchButton = (Button) findViewById(R.id.searchButton);
        originTextView = (TextView) findViewById(R.id.origin_text_view);
        destinationTextView = (TextView) findViewById(R.id.destination_text_view);
        travelDatePicker = (EditText) findViewById(R.id.ticket_date_text_view);
        travelDatePicker.setOnClickListener( e -> showDatePickerDialog());

        searchButton.setOnClickListener(v ->{
            Intent i = new Intent(this, QueryResultsActivity.class);
            i.putExtra("ORIGIN", originTextView.getText().toString());
            i.putExtra("DESTINATION", destinationTextView.getText().toString());
            i.putExtra("DATE_STRING", travelDatePicker.getText().toString() );
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm");
            //i.putExtra("DATE_STRING", sdf.format(new Date()) );
            startActivity(i);
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                //final String selectedDate = day + "/" + (month+1) + "/" + year;
                final String selectedDate = (day < 10 ? "0" : "") + day +
                        "/" + (month+1 < 10 ? "0" : "") + (month+1) + "/" + year;
                travelDatePicker.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}



/*NumberPicker np = findViewById(R.id.numberPicker);
np.setMinValue(2);
np.setMaxValue(37);
np.setOnValueChangedListener((numberPicker, i1, i2) -> {
    Log.d( "PICKER_MSG", String.valueOf(numberPicker.getValue()) );

});*/
