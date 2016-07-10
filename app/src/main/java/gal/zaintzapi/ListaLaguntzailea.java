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

public class ListaLaguntzailea extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private TextView listItemText;
    private Button deleteBtn;
    private LayoutInflater inflater;
    private View view;
    private String[] fitxategia;
    private DeiAsink deia;

    public ListaLaguntzailea(ArrayList<String> list, Context context) {
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
        view = convertView;
        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_laguntzailea, null);
        }
        listItemText = (TextView)view.findViewById(R.id.lerroa);
        listItemText.setText(list.get(position));
        deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fitxategia= new String[1];
                fitxategia[0]="/"+list.get(position)+".jpg";
                deia = new DeiAsink(5, fitxategia);
                if (deia.getZuzena()) {
                    deia = new DeiAsink(3, fitxategia);
                    if (deia.getZuzena()) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                }else{
                    new AlertDialog.Builder(parent.getContext())
                            .setTitle("Problemas con la conexión!")
                            .setMessage("Compruebe que las direcciones y la red esten bien configuradas.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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