package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class dropbox_jarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mApi;
    private Button bttn_zerrenda_ikusi;
    private Button bttn_argazkia_atera;
    String accessToken="";

    String[] fnames = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_jarduera);
        AppKeyPair appKeys = new AppKeyPair(DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        mApi.getSession().startOAuth2Authentication(dropbox_jarduera.this);
        bttn_zerrenda_ikusi=(Button)findViewById(R.id.bttnDropbox_argazki_zaerrenda);
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
    }

    protected void onResume() {
        super.onResume();

        if (mApi.getSession().authenticationSuccessful()) {
            try {
                mApi.getSession().finishAuthentication();
                accessToken = mApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void fitxategia_jaitsi() {
        dei_asink deia=new dei_asink(1,mApi,fnames);
        if (deia.getZuzena()) {
            Intent argazkiZerrendaIntent = new Intent(dropbox_jarduera.this, argazki_zerrenda.class);
            argazkiZerrendaIntent.putExtra("izenak", fnames);
            startActivity(argazkiZerrendaIntent);
        }
    }

    private String[] dena_jaitsi() {
        dei_asink deia=new dei_asink(0,mApi,fnames);
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
        String momentuko_data=ft.format(data);
        dei_asink deia=new dei_asink(2,mApi,null);
        if (deia.getZuzena()) {
            dena_jaitsi();
            String[] fitxategi_berria = new String[1];
            for (int i = 0; i < fnames.length; i++) {
                String momentuko_izena = fnames[i].split("/")[1].split(".jpg")[0];
                if (momentuko_izena.compareTo(momentuko_data) > 0) {
                    fitxategi_berria[0] = fnames[i];
                    dei_asink deia2 = new dei_asink(1, mApi, fitxategi_berria);
                    if (deia2.getZuzena()) {
                        Intent argazkiaIntent = new Intent(dropbox_jarduera.this, argazkia.class);
                        argazkiaIntent.putExtra("izena", fitxategi_berria[0]);
                        startActivity(argazkiaIntent);
                    }
                }
            }
        }
    }

    private void argazki_web_jaurti(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://echezaservidor.ddns.net/ZaintzaPi/servlet/ZaintzaPiArgazkia/");
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }
}
