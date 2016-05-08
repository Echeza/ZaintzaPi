package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class argazki_zerrenda extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazki_zerrenda);
        ListView zerrenda = (ListView) findViewById(R.id.argazki_zerrenda);
        Bundle extras = argazki_zerrenda.this.getIntent().getExtras();
        final String[] izenak = extras.getStringArray("izenak");
        String[] izenak_bistarako=new String[izenak.length];
        for(int i =0;i<izenak.length;i++){
            izenak_bistarako[i]=izenak[i].split("/")[1].split(".jpg")[0];
        }
        final ArrayAdapter<String> Values=new ArrayAdapter<String>(argazki_zerrenda.this, R.layout.zerrenda_lerroa, izenak_bistarako);
        zerrenda.setAdapter(Values);
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