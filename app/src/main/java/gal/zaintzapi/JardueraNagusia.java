package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class JardueraNagusia extends Activity {

    private Button bttn_saioa_hasi=null;
    private Switch konexio_aukera=null;
    private TextView konexio_status=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jarduera_nagusia);
        bttn_saioa_hasi=(Button)findViewById(R.id.saioa_hasi);
        konexio_aukera=(Switch) findViewById(R.id.konexioa);
        konexio_status = (TextView) findViewById(R.id.konexioStatus);
        bttn_saioa_hasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dropbox_jardueraIntent = new Intent(JardueraNagusia.this, dropbox_jarduera.class);
                startActivity(dropbox_jardueraIntent);
            }
        });
        konexio_aukera.setChecked(true);
        konexio_aukera.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    konexio_status.setText("Momentuko aukera Sare barruko konexioa da");
                    globalak.konexio_aukera=true;
                }else{
                    konexio_status.setText("Momentuko aukera Sare kanpoko konexioa da");
                    globalak.konexio_aukera=false;
                }
            }
        });
        if(konexio_aukera.isChecked()){
            konexio_status.setText("Momentuko aukera Sare barruko konexioa da");
        }else {
            konexio_status.setText("Momentuko aukera Sare kanpoko konexioa da");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jarduera_nagusia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.konexio_konfiguraketa) {
            Intent konexioaIntent = new Intent(JardueraNagusia.this, konexioa.class);
            startActivity(konexioaIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
