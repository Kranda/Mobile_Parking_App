package abayomi.code.parking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import abayomi.code.parking.R;

/**
 * Created by Abayomi S on 21.09.2016.
 */
public class ParkingListViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Parking> objects;
    View view;
    Parking parking;
    boolean flag[] = {true, false, false, false, true};


    public ParkingListViewAdapter(Context context, ArrayList<Parking> objects) {
        this.context = context;
        this.objects = objects;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


       // view = inflter.inflate(R.layout.activity_gridview, null);
       // ImageView icon = (ImageView) view.findViewById(R.id.icon);
       // icon.setImageResource(flags[i]);
       // return view;




        view = convertView;
        if (view == null) {
            //view = layoutInflater.inflate(R.layout.parking_item, parent, false);
            view = layoutInflater.inflate(R.layout.parking_grid, parent, false);
        }
        parking = getParking(position);
        view.setClickable(true);
        view.setFocusable(true);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                parking = getParking(position);
                Log.d("Item pressed: ", parking.parkingName);
                Intent goToSpecific = new Intent(v.getContext(),SpecificParkingActivity.class);
                goToSpecific.putExtra("Object", (Serializable) parking);
                v.getContext().startActivity(goToSpecific);
            }

        });


        //((TextView)(view.findViewById(R.id.textView_ParkingItem_ParkingName))).setText(parking.parkingName);
        //if(parking.capacity>0)
        //    ((ImageView)(view.findViewById(R.id.imageView_ParkingItem_Checker))).setImageResource(R.drawable.greentick);
        //else ((ImageView)(view.findViewById(R.id.imageView_ParkingItem_Checker))).setImageResource(R.drawable.redcross);

        return view;
    }

    Parking getParking(int position) {
        return ((Parking) getItem(position));
    }

}
