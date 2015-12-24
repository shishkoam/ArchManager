package shishkoam.archivemanager;

/**
 * Created by ав on 21.12.2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private ArrayList<TextView> textViews;
    private ArrayList<EditText> editTexts;
    TextView textView;

    public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position));
//        EditText editText = (EditText) rowView.findViewById(R.id.renamelabel);
//        editText.setVisibility(View.INVISIBLE);
        String s = values.get(position);

        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
//            imageView.setImageResource(R.drawable.no);
        } else {
//            imageView.setImageResource(R.drawable.ok);
        }

        return rowView;
    }

    public View getEditView(int position){
        return editTexts.get(position);
    }
    public TextView getTextView(int position){
        return textView;
    }
    public void deleteItem(int position){
    }
}