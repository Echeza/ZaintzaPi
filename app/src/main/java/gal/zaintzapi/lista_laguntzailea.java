package gal.zaintzapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class lista_laguntzailea extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public lista_laguntzailea(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_laguntzailea, null);
        }

        final TextView listItemText = (TextView)view.findViewById(R.id.lerroa);
        listItemText.setText(list.get(position));
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] fitxategia= new String[1];
                fitxategia[0]="/"+list.get(position)+".jpg";
                dei_asink deia = new dei_asink(5, fitxategia);
                if (deia.getZuzena()) {
                    dei_asink deia2 = new dei_asink(3, fitxategia);
                    if (deia2.getZuzena()) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                }else{
                    new AlertDialog.Builder(parent.getContext())
                            .setTitle("Konexioarekin arazoak")
                            .setMessage("Konproba ezazu ea helbideak eta sarea ondo adierazita dauden.")
                            .setPositiveButton("Ados", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        return view;
    }
}