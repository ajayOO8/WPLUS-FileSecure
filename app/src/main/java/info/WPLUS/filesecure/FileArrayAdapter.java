package info.WPLUS.filesecure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Leslie on 14/08/2014.
 */
public class FileArrayAdapter extends ArrayAdapter<Opcion> {

    private Context c;
    private int id;
    private List<Opcion> items;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Opcion> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public Opcion getItem(int i)
    {
        return items.get(i);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final Opcion o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.textView01);
            TextView t2 = (TextView) v.findViewById(R.id.textView02);

            if (t1 != null)
                t1.setText(o.getName());
            if (t2 != null)

                t2.setText(o.getData());


            ImageView iv = (ImageView) v.findViewById(R.id.imageView);
            if (o.getData().equalsIgnoreCase("folder"))
                iv.setImageResource(R.drawable.folder);
           else if (o.getData().equalsIgnoreCase("parent directory"))
                iv.setImageResource(R.drawable.parent);
            else
                iv.setImageResource(R.drawable.file);

            iv.setMaxHeight(3*t1.getHeight());
            iv.setMaxWidth(iv.getHeight()/2);
        } return v;
    }
}
