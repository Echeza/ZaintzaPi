package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.util.ArrayList;


public class dropbox_jarduera extends Activity {

    final static public String DROPBOX_APP_KEY = "2szq959c1bypkwm";
    final static public String DROPBOX_APP_SECRET = "4v8lv8bv5cm9iqp";

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mApi;
    private Button bttn_ver_info;
    String accessToken="";

    String[] fnames = null;

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
                mApi.getSession().finishAuthentication();
                accessToken = mApi.getSession().getOAuth2AccessToken();
                bajar_todo();
                if (fnames.length!=0){
                    fitxatgia_jaitsi();
                }
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void fitxatgia_jaitsi() {
        dei_asink deia=new dei_asink(1,mApi,fnames);
        if (deia.getZuzena()) {
            setContentView(R.layout.argazkia);
            TextView jpgName = (TextView) findViewById(R.id.jpgname);
            ImageView jpgView = (ImageView) findViewById(R.id.jpgview);

            String myJpgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+fnames[0];

            jpgName.setText(myJpgPath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
            jpgView.setImageBitmap(bm);
        }
    }

    private String[] bajar_todo() {
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
}
