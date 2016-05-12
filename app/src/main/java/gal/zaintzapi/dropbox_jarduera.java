package gal.zaintzapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class dropbox_jarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private Button bttn_zerrenda_ikusi;
    private Button bttn_argazkia_atera;
    private Switch konexio_aukera=null;
    private TextView konexio_status=null;
    String accessToken="";

    String[] fnames = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_jarduera);
        AppKeyPair appKeys = new AppKeyPair(DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        globalak.mApi = new DropboxAPI<AndroidAuthSession>(session);
        globalak.mApi.getSession().startOAuth2Authentication(dropbox_jarduera.this);
        bttn_zerrenda_ikusi=(Button)findViewById(R.id.bttnDropbox_argazki_zaerrenda);
        konexio_aukera=(Switch) findViewById(R.id.konexioa);
        konexio_status = (TextView) findViewById(R.id.konexioStatus);
        bttn_zerrenda_ikusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dena_jaitsi();
                if (fnames.length!=0){
                    fitxategia_jaitsi();
                }
            }
        });
        bttn_argazkia_atera=(Button)findViewById(R.id.bttnArgazkia_atera);
        bttn_argazkia_atera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                argazkia_atera();
            }
        });
        konexio_aukera.setChecked(true);
        konexio_aukera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    konexio_status.setText("SARE BARRUAN");
                    globalak.konexio_aukera=true;
                }else{
                    konexio_status.setText("SARE KANPOAN");
                    globalak.konexio_aukera=false;
                }
            }
        });
        if(konexio_aukera.isChecked()){
            konexio_status.setText("SARE BARRUAN");
        }else {
            konexio_status.setText("SARE KANPOAN");
        }
    }

    protected void onResume() {
        super.onResume();

        if (globalak.mApi.getSession().authenticationSuccessful()) {
            try {
                globalak.mApi.getSession().finishAuthentication();
                accessToken = globalak.mApi.getSession().getOAuth2AccessToken();
                //Aldaketak dauden ikusteko
                dei_asink deia=new dei_asink(4,fnames);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void fitxategia_jaitsi() {
        dei_asink deia=new dei_asink(1,fnames);
        if (deia.getZuzena()) {
            Intent argazkiZerrendaIntent = new Intent(dropbox_jarduera.this, argazki_zerrenda.class);
            argazkiZerrendaIntent.putExtra("izenak", fnames);
            startActivity(argazkiZerrendaIntent);
        }
    }

    private String[] dena_jaitsi() {
        dei_asink deia=new dei_asink(0,fnames);
        if (deia.getZuzena()) {
            DropboxAPI.Entry dirent= deia.getEmaitza();
            ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
            ArrayList<String> dir = new ArrayList<String>();
            int i = 0;
            for (DropboxAPI.Entry ent : dirent.contents) {
                files.add(ent);
                dir.add(new String(files.get(i++).path));
            }
            fnames = dir.toArray(new String[dir.size()]);
        }

        return fnames;
    }

    private void argazkia_atera() {

        Date data=new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_HH.mm.ss");
        ft.setTimeZone(TimeZone.getTimeZone("gmt"));
        String momentuko_data=ft.format(data);
        dei_asink deia=new dei_asink(2,null);
        if (deia.getZuzena()) {
            dena_jaitsi();
            String[] fitxategi_berria = new String[1];
            for (int i = 0; i < fnames.length; i++) {
                String momentuko_izena = fnames[i].split("/")[1].split(".jpg")[0];
                if (momentuko_izena.compareTo(momentuko_data) >= 0) {
                    fitxategi_berria[0] = fnames[i];
                    dei_asink deia2 = new dei_asink(1, fitxategi_berria);
                    if (deia2.getZuzena()) {
                        Intent argazkiaIntent = new Intent(dropbox_jarduera.this, argazkia.class);
                        argazkiaIntent.putExtra("izena", fitxategi_berria[0]);
                        startActivity(argazkiaIntent);
                    }
                }
            }
        }else{
            new AlertDialog.Builder(dropbox_jarduera.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_konexioa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.konexio_konfiguraketa) {
            Intent konexioaIntent = new Intent(dropbox_jarduera.this, konexioa.class);
            startActivity(konexioaIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
