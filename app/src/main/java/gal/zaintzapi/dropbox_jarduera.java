package gal.zaintzapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


public class dropbox_jarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI mApi;
    private Button bttn_ver_info;


    private boolean downloadDropboxFile(String dbPath, File localFile) throws IOException {

        BufferedInputStream br = null;
        BufferedOutputStream bw = null;
        DropboxAPI.DropboxInputStream fd= null;
        try {
            if (!localFile.exists()) {
                localFile.createNewFile(); //otherwise dropbox client will fail silently
            }
            try {
              fd =  mApi.getFileStream(dbPath, null);
                //br = new BufferedInputStream(fd.);
                bw = new BufferedOutputStream(new FileOutputStream(localFile));

                byte[] buffer = new byte[4096];
                int read;
                while (true) {
                    read = br.read(buffer);
                    if (read <= 0) {
                        break;
                    }
                    bw.write(buffer, 0, read);
                }
            } catch (DropboxException e) {
                e.printStackTrace();
            }


        } finally {
            //in finally block:
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_jarduera);
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI(session);
       /* mApi.getFileStream('\\','hola');*/

        bttn_ver_info=(Button)findViewById(R.id.bttnDrop_box_ver_info_cuenta);
        bttn_ver_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada_ws l = new llamada_ws(dropbox_jarduera.this, mApi);
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (l.isCorrecta()) {
                    if (!l.getResultado().toString().equals("false")) {
                       Object obj = l.getResultado();
                        int a=0;

                    }
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private String[] getKeys() {
        String key = DROPBOX_APP_KEY;
        String secret = DROPBOX_APP_SECRET;
        if (key != null && secret != null) {
            String[] ret = new String[2];
            ret[0] = key;
            ret[1] = secret;
            return ret;
        } else {
            return null;
        }
    }


    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(DROPBOX_APP_KEY,DROPBOX_APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0],
                    stored[1]);
            session = new AndroidAuthSession(appKeyPair,ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }

}
