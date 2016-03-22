package gal.zaintzapi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.Session;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;



public class llamada_ws extends Activity{

    private Object resultado;
    public boolean terminada=false;
    private boolean correcta=false;
    public Exception excepcion=null;
    private DropboxAPI<Session> mApi;


    public llamada_ws(Activity act,DropboxAPI<Session> mApi)  {
        this.mApi=mApi;
        if (Build.VERSION.SDK_INT >= 20)
            new llamada_ws_tarea().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new llamada_ws_tarea().execute();

        long tStart = System.currentTimeMillis();
        while (!terminada) {
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;
            if (elapsedSeconds > 120)
                break;

        }


    }

    public Object getResultado() {

        return resultado;
    }

    public void setResultado(Object resultado) {

        this.resultado = resultado;
    }



    public boolean isCorrecta() {

        return correcta;
    }

    public void setCorrecta(boolean b) {

        this.correcta = correcta;
    }

    private class llamada_ws_tarea extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            DropboxAPI.Account cuenta=null;
            try {
                cuenta =  mApi.accountInfo();
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            correcta=true;
            terminada=true;
            resultado=cuenta;
            return cuenta;

        }
        protected void onPostExecute(String result) {

        }
    }
}