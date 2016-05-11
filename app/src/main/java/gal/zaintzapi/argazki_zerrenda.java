package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class argazki_zerrenda extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazki_zerrenda);
        ListView zerrenda = (ListView) findViewById(R.id.argazki_zerrenda);
        Bundle extras = argazki_zerrenda.this.getIntent().getExtras();
        final String[] izenak = extras.getStringArray("izenak");
        ArrayList<String> izenak_bistarako =new ArrayList<>();
        for(int i =0;i<izenak.length;i++){
            izenak_bistarako.add(i,izenak[i].split("/")[1].split(".jpg")[0]);
        }

        lista_laguntzailea adapter = new lista_laguntzailea(izenak_bistarako, this);
        zerrenda.setAdapter(adapter);

        zerrenda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent argazkiaIntent = new Intent(argazki_zerrenda.this, argazkia.class);
                argazkiaIntent.putExtra("izena", izenak[i]);
                startActivity(argazkiaIntent);
            }
        });
    }
}
