package gal.zaintzapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class dropbox_jarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mApi;
    private Button bttn_ver_info;

    String accessToken="";


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

                //accessToken = mApi.getSession().getOAuth2AccessToken();
                bajar_todo();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DropboxException e) {
                e.printStackTrace();
            }
        }
    }

    private void ver_info() {
        if (accessToken != "") {
            try {
                String fitxategi="/prueba.txt";
                File file = new File(fitxategi);
                FileOutputStream outputStream = new FileOutputStream(file);
                DropboxAPI.DropboxFileInfo info = mApi.getFile(fitxategi, null, outputStream, null);
                Log.i("ExampleLog", "The file's rev is: " + info.getMetadata().rev);
            } catch (IllegalStateException e) {
                Log.i("AuthLog", "Error authenticating", e);
            } catch (FileNotFoundException e) {
                Log.i("File", "Error file", e);
            } catch (DropboxException e) {
                Log.i("Dropbox", "Error dropbox", e);
            }
        }
    }

    private void subir() throws FileNotFoundException, DropboxException {
        File file = new File("/data/data/weka.log");
        FileInputStream inputStream = new FileInputStream(file);
        DropboxAPI.Entry response = mApi.putFile("/magnum-opus.txt", inputStream, file.length(), null, null);
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
    }

    private String[] bajar_todo() throws FileNotFoundException, DropboxException {
        String[] fnames = null;
        DropboxAPI.Entry dirent=null;
        llamada_ws llamada=new llamada_ws(this,mApi);
        dirent=llamada.getResultado();
        ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
        ArrayList<String> dir = new ArrayList<String>();
        int i=0;
        for (DropboxAPI.Entry ent : dirent.contents) {
            files.add(ent);
            dir.add(new String(files.get(i++).path));
        }
        fnames = dir.toArray(new String[dir.size()]);

        return fnames;
    }
}
