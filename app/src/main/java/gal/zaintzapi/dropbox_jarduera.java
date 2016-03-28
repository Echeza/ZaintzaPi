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
    private DropboxAPI<AndroidAuthSession> mApi;
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
        AppKeyPair appKeys = new AppKeyPair(DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        bttn_ver_info=(Button)findViewById(R.id.bttnDrop_box_ver_info_cuenta);
        bttn_ver_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApi.getSession().startOAuth2Authentication(dropbox_jarduera.this);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        if (mApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mApi.getSession().finishAuthentication();

                String accessToken = mApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
}
