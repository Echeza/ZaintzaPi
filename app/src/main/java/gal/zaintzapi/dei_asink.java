package gal.zaintzapi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class dei_asink extends Activity{

    private DropboxAPI.Entry emaitza=null;
    public boolean amaitua=false;
    private boolean zuzena=false;
    private DropboxAPI<AndroidAuthSession> mApi;
    String[] fnames=null;

    public dei_asink(int deia, DropboxAPI<AndroidAuthSession> mApi,String[] fnames)  {
        this.mApi=mApi;
        this.fnames=fnames;
        if (deia==0) {
            new fitxategien_pathak().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==1){
            new jaitsi_argazkia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }
    }

    public DropboxAPI.Entry getEmaitza() {
        return emaitza;
    }

    public boolean getZuzena() {
        return zuzena;
    }

    private class fitxategien_pathak extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params) {
            try {
                emaitza = mApi.metadata("/", 1000, null, true, null);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            zuzena=true;
            amaitua=true;
            return emaitza;

        }
    }

    private class jaitsi_argazkia extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String fitxategi=fnames[0];
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fitxategi);
                FileOutputStream outputStream = new FileOutputStream(file);
                DropboxAPI.DropboxFileInfo info = mApi.getFile(fitxategi, null, outputStream, null);
                Log.i("ExampleLog", "The file's rev is: " + info.getMetadata().rev);
            } catch (DropboxException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            zuzena=true;
            amaitua=true;
            return null;
        }
    }
}