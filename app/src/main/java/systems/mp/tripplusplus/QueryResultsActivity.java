package systems.mp.tripplusplus;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

        String originToLookup = getIntent().getExtras().getString("ORIGIN");
        String destinationToLookup = getIntent().getExtras().getString("DESTINATION");
        String dateStringToLookup = getIntent().getExtras().getString("DATE_STRING");

        route_text_view.setText(originToLookup.trim().toUpperCase() + " - " + destinationToLookup.trim().toUpperCase());
        travel_date_text_view.setText( dateStringToLookup );

        // get instance of data source, execute query
        TicketDataMocker dataSource = new TicketDataMocker();
        List<Ticket> results = dataSource.getTicketsByRouteAndDate(originToLookup, destinationToLookup, dateStringToLookup);
        //List<Ticket> results = dataSource.getTicketsByRoute(originToLookup, destinationToLookup);
        //List<Ticket> results = dataSource.getAllTickets();

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
}
