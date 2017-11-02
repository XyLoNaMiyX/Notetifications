package tk.lonamiwebs.notetifications;

/**
 * Created by Lonami on 14/12/2014.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotifAdapter extends ArrayAdapter<Notetification> {

    private final Context context;
    private final ArrayList<Notetification> itemsArrayList;

    public NotifAdapter(Context context, ArrayList<Notetification> itemsArrayList) {

        super(context, R.layout.listview_notif, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listview_notif, parent, false);

        // 3. Get the two text view from the rowView
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView content = (TextView) rowView.findViewById(R.id.content);

        // 4. Set the text for textView
        icon.setImageResource(MainActivity.resources.getIdentifier(itemsArrayList.get(position).icon, "drawable", MainActivity.PackageName));
        title.setText(itemsArrayList.get(position).title);
        content.setText(itemsArrayList.get(position).content);

        // 5. return rowView
        return rowView;
    }
}