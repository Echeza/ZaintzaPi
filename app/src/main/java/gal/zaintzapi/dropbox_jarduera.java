package gal.zaintzapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private Switch mugimendua=null;
    private TextView mugimendu_status=null;
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
        mugimendua=(Switch) findViewById(R.id.mugimendua);
        mugimendu_status = (TextView) findViewById(R.id.mugimendustatus);

        SharedPreferences settings = getSharedPreferences(globalak.Pref_URL, 0);
        final SharedPreferences.Editor editor = settings.edit();

        bttn_zerrenda_ikusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dena_jaitsi();
                if (fnames!=null){
                    fitxategiak_jaitsi();
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
        konexio_aukera.setChecked(globalak.konexio_konf);
        konexio_aukera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    konexio_status.setText("SARE BARRUAN");
                    globalak.konexio_konf=true;
                    editor.putBoolean("konexio_konf", globalak.konexio_konf);
                }else{
                    konexio_status.setText("SARE KANPOAN");
                    globalak.konexio_konf=false;
                    editor.putBoolean("konexio_konf", globalak.konexio_konf);
                }
                editor.commit();
            }
        });
        if(konexio_aukera.isChecked()){
            konexio_status.setText("SARE BARRUAN");
        }else {
            konexio_status.setText("SARE KANPOAN");
        }
        mugimendua.setChecked(globalak.mugimendu_konf);
        mugimendua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dei_asink deia =new dei_asink(6,null);
                    if(!deia.getZuzena()) {
                        mugimendua.setChecked(false);
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
                    }else{
                        mugimendu_status.setText("SISTEMA AUTOMATIKOA");
                        globalak.mugimendu_konf=true;
                        editor.putBoolean("mugimendu_konf", globalak.mugimendu_konf);
                    }
                }else{
                    dei_asink deia =new dei_asink(4,null);
                    if(!deia.getZuzena()) {
                        mugimendua.setChecked(true);
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
                    }else{
                        mugimendu_status.setText("ESKUZKO SISTEMA");
                        globalak.mugimendu_konf=false;
                        editor.putBoolean("mugimendu_konf", globalak.mugimendu_konf);
                    }
                }
                editor.commit();
            }
        });
        if(mugimendua.isChecked()){
            mugimendu_status.setText("SISTEMA AUTOMATIKOA");
        }else {
            mugimendu_status.setText("ESKUZKO SISTEMA");
        }
    }

    protected void onResume() {
        super.onResume();

        if (globalak.mApi.getSession().authenticationSuccessful()) {
            try {
                globalak.mApi.getSession().finishAuthentication();
                accessToken = globalak.mApi.getSession().getOAuth2AccessToken();
                new notifikazioBilaketa().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void fitxategiak_jaitsi() {
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
        mugimendua.setChecked(false);
        globalak.fitxategiKantitatea+=1;
        Date data = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        ft.setTimeZone(TimeZone.getTimeZone("gmt"));
        String momentuko_data = ft.format(data);
        dei_asink deia = new dei_asink(2, null);
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
        }else {
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

    private class notifikazioBilaketa extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params) {
            try {
                String cursor = null;
                String fitxategi_berria = null;
                while (true) {
                    DropboxAPI.DeltaPage<DropboxAPI.Entry> result = null;
                    if (globalak.mApi != null) {
                        result = globalak.mApi.delta(cursor);
                        if (globalak.fitxategiKantitatea!=null) {
                            if (result.entries.size()>globalak.fitxategiKantitatea){
                                globalak.fitxategiKantitatea = result.entries.size();
                                Intent intent = new Intent(dropbox_jarduera.this, argazkia.class);
                                fitxategi_berria=result.entries.get(result.entries.size()-1).lcPath;
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi_berria);
                                if (!file.exists()) {
                                    FileOutputStream outputStream = new FileOutputStream(file);
                                    globalak.mApi.getFile(fitxategi_berria, null, outputStream, null);
                                    intent.putExtra("izena", fitxategi_berria);
                                    PendingIntent pIntent = PendingIntent.getActivity(dropbox_jarduera.this, (int) System.currentTimeMillis(), intent, 0);
                                    Notification n = new Notification.Builder(dropbox_jarduera.this)
                                            .setContentTitle("Zaintza Pi")
                                            .setContentText("Mugimendua egon da!!")
                                            .setSmallIcon(R.mipmap.zaintzapi)
                                            .setContentIntent(pIntent)
                                            .setAutoCancel(true).build();
                                    NotificationManager notificationManager =
                                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                    notificationManager.notify(0, n);
                                }
                            }
                        }else{
                            globalak.fitxategiKantitatea = result.entries.size();
                        }
                    }
                }

            } catch (DropboxException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
