package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ArgazkiZerrenda extends Activity{

    private ListView zerrenda;
    private Bundle extras;
    private String[] izenak;
    private ArrayList<String> izenakBistarako;
    private ListaLaguntzailea adapter;
    private Intent argazkiaIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazki_zerrenda);
        zerrenda = (ListView) findViewById(R.id.argazki_zerrenda);
        extras = ArgazkiZerrenda.this.getIntent().getExtras();
        izenak = extras.getStringArray("izenak");
        izenakBistarako =new ArrayList<>();
        for(int i =0;i<izenak.length;i++){
            izenakBistarako.add(i,izenak[i].split("/")[1].split(".jpg")[0]);
        }
        adapter = new ListaLaguntzailea(izenakBistarako, this);
        zerrenda.setAdapter(adapter);
        zerrenda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                argazkiaIntent = new Intent(ArgazkiZerrenda.this, Argazkia.class);
                argazkiaIntent.putExtra("izena", izenak[i]);
                startActivity(argazkiaIntent);
            }
        });
    }
}
