package gal.zaintzapi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class dei_asink extends Activity{

    private DropboxAPI.Entry emaitza=null;
    public boolean amaitua=false;
    private boolean zuzena=false;
    String[] fnames=null;

    public dei_asink(int deia,String[] fnames)  {
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
        }else if(deia==2){
            new web_deia_argazkia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==3){
            new fitxategia_ezabatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==4){
            new web_deia_amatatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==5) {
            new web_deia_ezabatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            long tStart = System.currentTimeMillis();
            while (!amaitua) {
                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==6) {
            new web_deia_mugimendua().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                emaitza = globalak.mApi.metadata("/", 1000, null, true, null);
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
                for (int i=0;i<fnames.length;i++){
                    String fitxategi = fnames[i];
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi);
                    if (!file.exists()) {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        DropboxAPI.DropboxFileInfo info = globalak.mApi.getFile(fitxategi, null, outputStream, null);
                        Log.i("Fitxategia", "The file's rev is: " + info.getMetadata().rev);
                    }
                }
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

    private class web_deia_argazkia extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;
            try {
                //URL url = new URL("http://192.168.1.55:8080/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                //URL url = new URL("http://echezaservidor.ddns.net/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                URL url =null;
                if (globalak.konexio_konf){
                    url = new URL("http://"+globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                }else{
                    url = new URL("http://"+globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }
                connection.disconnect();
                if (sb!=null)
                    serverResponse=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            amaitua=true;
            return null;
        }
    }

    private class fitxategia_ezabatu extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                for (int i=0;i<fnames.length;i++){
                    String fitxategi = fnames[i];
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi);
                    file.delete();
                    if (!file.exists()) {
                        globalak.mApi.delete(fitxategi);
                    }
                }
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            zuzena=true;
            amaitua=true;
            return null;
        }
    }

    private class web_deia_amatatu extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;
            try {
                URL url =null;
                if (globalak.konexio_konf){
                    url = new URL("http://"+globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiAmatatu");
                }else{
                    url = new URL("http://"+globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiAmatatu");
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }
                connection.disconnect();
                if (sb!=null)
                    serverResponse=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            amaitua=true;
            return null;
        }
    }

    private class web_deia_ezabatu extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;
            for (int i=0;i<fnames.length;i++) {
                String fitxategi = fnames[i];
                try {
                    URL url = null;
                    if (globalak.konexio_konf) {
                        url = new URL("http://" + globalak.url_intra + "/ZaintzaPi/servlet/ZaintzaPiEzabatu?fitxategia=" +fitxategi);
                    } else {
                        url = new URL("http://" + globalak.url_extra + "/ZaintzaPi/servlet/ZaintzaPiEzabatu?fitxategia=" +fitxategi);
                    }
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int statusCode = connection.getResponseCode();
                    Log.i("Status Kodea", "" + statusCode);
                    if (statusCode == 200) {
                        zuzena = true;
                        sb = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                    }
                    connection.disconnect();
                    if (sb != null)
                        serverResponse = sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                amaitua = true;
            }
            return null;
        }
    }

    private class web_deia_mugimendua extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;
            try {
                URL url =null;
                if (globalak.konexio_konf){
                    url = new URL("http://"+globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiMugimendua");
                }else{
                    url = new URL("http://"+globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiMugimendua");
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }
                connection.disconnect();
                if (sb!=null)
                    serverResponse=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            amaitua=true;
            return null;
        }
    }
}