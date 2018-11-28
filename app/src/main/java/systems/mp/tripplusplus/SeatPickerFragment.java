package systems.mp.tripplusplus;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class SeatPickerFragment extends DialogFragment implements View.OnClickListener, OnSeatSelected {

    private Button submit;
    private ArrayList<Integer> availableSeats;
    private ArrayList<Integer> selectedSeats;

    private static final int COLUMNS = 5;
    private AirplaneAdapter adapter;

    public interface SeatPickerDialogListener {
        void onFinishSeatPickerDialog(ArrayList<Integer> seats);
    }

    public SeatPickerFragment() {
    }

    public static SeatPickerFragment newInstance(ArrayList<Integer> seats) {
        SeatPickerFragment frag = new SeatPickerFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList("availableSeats", seats);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_picker, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<30; i++) {

            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        GridLayoutManager manager = new GridLayoutManager(getActivity(), COLUMNS);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        adapter = new AirplaneAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        availableSeats = getArguments().getIntegerArrayList("availableSeats");
        selectedSeats = new ArrayList<>();

        Log.d("SPFRAG", availableSeats.toString());

        submit = view.findViewById(R.id.buttonOk);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SeatPickerDialogListener listener = (SeatPickerDialogListener) getActivity();
        listener.onFinishSeatPickerDialog(selectedSeats);
        dismiss();
    }

    @Override
    public void onSeatSelected(int count) {
        Log.d("ADAPTER", adapter.getSelectedItems().toString());
        selectedSeats = (ArrayList<Integer>) adapter.getSelectedItems();
    }
}
