package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class argazki_zerrenda extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazki_zerrenda);
        ListView zerrenda = (ListView) findViewById(R.id.listView);
        Bundle extras = argazki_zerrenda.this.getIntent().getExtras();
        final String[] izenak = extras.getStringArray("izenak");
        final ArrayAdapter<String> Values=new ArrayAdapter<String>(argazki_zerrenda.this, android.R.layout.simple_list_item_1, izenak);
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
