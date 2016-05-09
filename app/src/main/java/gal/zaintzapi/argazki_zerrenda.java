package gal.zaintzapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class argazki_zerrenda extends Activity{
    private Button delete;
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
        delete = (Button)findViewById(R.id.delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert=new AlertDialog.Builder(argazki_zerrenda.this).create();
                alert.setTitle("Uolaaas"+(int)v.getTag());
            }
        });
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
