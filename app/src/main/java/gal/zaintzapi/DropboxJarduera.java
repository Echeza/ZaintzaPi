package gal.zaintzapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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


public class DropboxJarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";
    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private Button bttn_zerrenda_ikusi;
    private Button bttn_argazkia_atera;
    private Switch konexio_aukera=null;
    private TextView konexio_status=null;
    private Switch mugimendua=null;
    private TextView mugimendu_status=null;
    private String accessToken="";
    private String[] fnames = null;
    private AppKeyPair appKeys;
    private AndroidAuthSession session;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private DeiAsink deia;
    private Intent argazkiZerrendaIntent;
    private Intent argazkiaIntent;
    private DropboxAPI.Entry dirent;
    private ArrayList<DropboxAPI.Entry> files;
    private ArrayList<String> dir;
    private int i;
    private Date data;
    private SimpleDateFormat ft;
    private String momentuko_data;
    private String[] fitxategi_berria;
    private String momentuko_izena;
    private Intent konexioaIntent;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_jarduera);
        appKeys = new AppKeyPair(DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
        session = new AndroidAuthSession(appKeys);
        Globalak.mApi=new DropboxAPI<AndroidAuthSession>(session);
        Globalak.mApi.getSession().startOAuth2Authentication(DropboxJarduera.this);
        bttn_zerrenda_ikusi=(Button)findViewById(R.id.bttnDropbox_argazki_zaerrenda);
        konexio_aukera=(Switch) findViewById(R.id.konexioa);
        konexio_status = (TextView) findViewById(R.id.konexioStatus);
        mugimendua=(Switch) findViewById(R.id.mugimendua);
        mugimendu_status = (TextView) findViewById(R.id.mugimendustatus);
        settings = getSharedPreferences(Globalak.Pref_URL, 0);
        editor = settings.edit();
        bttn_zerrenda_ikusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                denaJaitsi();
                if (fnames!=null){
                    fitxategiakJaitsi();
                }
            }
        });
        bttn_argazkia_atera=(Button)findViewById(R.id.bttnArgazkia_atera);
        bttn_argazkia_atera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                argazkiaAtera();
            }
        });
        konexio_aukera.setChecked(Globalak.konexio_konf);
        konexio_aukera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    konexio_status.setText("Dentro de la red");
                    Globalak.konexio_konf=true;
                    editor.putBoolean("konexio_konf", Globalak.konexio_konf);
                }else{
                    konexio_status.setText("Fuera de la red");
                    Globalak.konexio_konf=false;
                    editor.putBoolean("konexio_konf", Globalak.konexio_konf);
                }
                editor.commit();
            }
        });
        if(konexio_aukera.isChecked()){
            konexio_status.setText("Dentro de la red");
        }else {
            konexio_status.setText("Fuera de la red");
        }
        mugimendua.setChecked(Globalak.mugimendu_konf);
        mugimendua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    deia =new DeiAsink(6,null);
                    if(!deia.getZuzena()) {
                        mugimendua.setChecked(false);
                        new AlertDialog.Builder(DropboxJarduera.this)
                                .setTitle("Problemas con la conexión!")
                                .setMessage("Compruebe que las direcciones y la red esten bien configuradas.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }else{
                        mugimendu_status.setText("SISTEMA AUTOMATICO");
                        Globalak.mugimendu_konf=true;
                        editor.putBoolean("mugimendu_konf", Globalak.mugimendu_konf);
                    }
                }else{
                    deia =new DeiAsink(4,null);
                    if(!deia.getZuzena()) {
                        mugimendua.setChecked(true);
                        new AlertDialog.Builder(DropboxJarduera.this)
                                .setTitle("Problemas con la conexión!")
                                .setMessage("Compruebe que las direcciones y la red esten bien configuradas.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }else{
                        mugimendu_status.setText("ESKUZKO SISTEMA");
                        Globalak.mugimendu_konf=false;
                        editor.putBoolean("mugimendu_konf", Globalak.mugimendu_konf);
                    }
                }
                editor.commit();
            }
        });
        if(mugimendua.isChecked()){
            mugimendu_status.setText("SISTEMA AUTOMATICO");
        }else {
            mugimendu_status.setText("SISTEMA MANUAL");
        }
    }

    protected void onResume() {
        super.onResume();
        if (Globalak.mApi.getSession().authenticationSuccessful()) {
            try {
                Globalak.mApi.getSession().finishAuthentication();
                accessToken = Globalak.mApi.getSession().getOAuth2AccessToken();
                new notifikazioBilaketa().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void fitxategiakJaitsi() {
        deia=new DeiAsink(1,fnames);
        if (deia.getZuzena()) {
            argazkiZerrendaIntent = new Intent(DropboxJarduera.this, ArgazkiZerrenda.class);
            argazkiZerrendaIntent.putExtra("izenak", fnames);
            startActivity(argazkiZerrendaIntent);
        }
    }

    private String[] denaJaitsi() {
        deia=new DeiAsink(0,fnames);
        if (deia.getZuzena()) {
            dirent= deia.getEmaitza();
            files = new ArrayList<DropboxAPI.Entry>();
            dir = new ArrayList<String>();
            i = 0;
            for (DropboxAPI.Entry ent : dirent.contents) {
                files.add(ent);
                dir.add(new String(files.get(i++).path));
            }
            fnames = dir.toArray(new String[dir.size()]);
        }
        return fnames;
    }

    private void argazkiaAtera() {
        mugimendua.setChecked(false);
        if (Globalak.fitxategiKantitatea!=null) {
            Globalak.fitxategiKantitatea += 1;
        }
        data = new Date();
        ft = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        ft.setTimeZone(TimeZone.getTimeZone("gmt"));
        momentuko_data = ft.format(data);
        deia = new DeiAsink(2, null);
        if (deia.getZuzena()) {
            denaJaitsi();
            fitxategi_berria = new String[1];
            for (int i = 0; i < fnames.length; i++) {
                momentuko_izena = fnames[i].split("/")[1].split(".jpg")[0];
                if (momentuko_izena.compareTo(momentuko_data) >= 0) {
                    fitxategi_berria[0] = fnames[i];
                    deia = new DeiAsink(1, fitxategi_berria);
                    if (deia.getZuzena()) {
                        argazkiaIntent = new Intent(DropboxJarduera.this, Argazkia.class);
                        argazkiaIntent.putExtra("izena", fitxategi_berria[0]);
                        startActivity(argazkiaIntent);
                    }
                }
            }
        }else {
                new AlertDialog.Builder(DropboxJarduera.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_konexioa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();
        if (id == R.id.konexio_konfiguraketa) {
            konexioaIntent = new Intent(DropboxJarduera.this, Konexioa.class);
            startActivity(konexioaIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class notifikazioBilaketa extends AsyncTask<String, Void, Object> {
        private String cursor = null;
        private String fitxategi_berria;
        private DropboxAPI.DeltaPage<DropboxAPI.Entry> result;
        private Intent intent;
        private File file;
        private FileOutputStream outputStream;
        private PendingIntent pIntent;
        private Notification n;
        private NotificationManager notificationManager;
        @Override
        protected Object doInBackground(String... params) {
            try {
                while (true) {
                    if (Globalak.mApi != null) {
                        result = Globalak.mApi.delta(cursor);
                        if (Globalak.fitxategiKantitatea!=null) {
                            if (result.entries.size()> Globalak.fitxategiKantitatea){
                                Globalak.fitxategiKantitatea = result.entries.size();
                                intent = new Intent(DropboxJarduera.this, Argazkia.class);
                                fitxategi_berria=result.entries.get(result.entries.size()-1).lcPath;
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi_berria);
                                if (!file.exists()) {
                                    outputStream = new FileOutputStream(file);
                                    Globalak.mApi.getFile(fitxategi_berria, null, outputStream, null);
                                    intent.putExtra("izena", fitxategi_berria);
                                    pIntent = PendingIntent.getActivity(DropboxJarduera.this, (int) System.currentTimeMillis(), intent, 0);
                                    n = new Notification.Builder(DropboxJarduera.this)
                                            .setContentTitle("Zaintza Pi")
                                            .setContentText("Movimiento detectado!!")
                                            .setSmallIcon(R.mipmap.zaintzapi)
                                            .setContentIntent(pIntent)
                                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                            .setLights(Color.RED, 3000, 3000)
                                            .setAutoCancel(true).build();
                                    notificationManager =
                                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                    notificationManager.notify(0, n);
                                }
                            }
                        }else{
                            Globalak.fitxategiKantitatea = result.entries.size();
                        }
                    }
                }
            } catch (DropboxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
