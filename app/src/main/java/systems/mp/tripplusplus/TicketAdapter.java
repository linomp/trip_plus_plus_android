package systems.mp.tripplusplus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TicketAdapter extends ArrayAdapter<Ticket> {

    public TicketAdapter(Activity context, List<Ticket> words){
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Ticket currentTicket = getItem(position);

        TextView ticketIdView = (TextView) listItemView.findViewById(R.id.ticket_id_text_view);
        ticketIdView.setText( currentTicket.getId() );

        TextView companyTextView = (TextView) listItemView.findViewById(R.id.company_text_view);
        companyTextView.setText(currentTicket.getCompanyName());

        TextView companyRatingTextView = (TextView) listItemView.findViewById(R.id.company_rating);
        companyRatingTextView.setText(  String.valueOf(currentTicket.getCompanyRating()) + "/5" );
        //companyRatingTextView.setText("4/5");

        TextView servicesTextView = (TextView) listItemView.findViewById(R.id.services_text_view);
        servicesTextView.setText(currentTicket.getServices());

        TextView departureTextView = (TextView) listItemView.findViewById(R.id.departure_text_view);
        departureTextView.setText(currentTicket.getDepartureDateTimeString());

        TextView ticketPriceTextView = (TextView) listItemView.findViewById(R.id.ticket_price_text_view);
        ticketPriceTextView.setText( "$ "+ String.valueOf(currentTicket.getPrice()) );

        // Find the TextView in the list_item.xml layout with the default translation of the word
        ImageView graphicalDescription = (ImageView) listItemView.findViewById(R.id.company_logo);
        graphicalDescription.setImageResource(currentTicket.getImageResId());
        /*if(!currentWord.hasImage()) {
            graphicalDescription.setVisibility(View.GONE);
        }*/

        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

}
