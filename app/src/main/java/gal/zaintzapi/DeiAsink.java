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

public class DeiAsink extends Activity{
    private DropboxAPI.Entry emaitza=null;
    private boolean amaitua=false;
    private boolean zuzena=false;
    private String[] fnames=null;
    private long tStart;
    private long tEnd;
    private long tDelta;
    private double elapsedSeconds;

    public DeiAsink(int deia, String[] fnames)  {
        this.fnames=fnames;
        if (deia==0) {
            new FitxategienPathak().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==1){
            new JaitsiArgazkia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==2){
            new WebDeiaArgazkia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==3){
            new FitxategiaEzabatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==4){
            new WebDeiaAmatatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==5) {
            new WebDeiaEzabatu().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                if (elapsedSeconds > 120)
                    break;
            }
        }else if(deia==6) {
            new WebDeiaMugimendua().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tStart = System.currentTimeMillis();
            while (!amaitua) {
                tEnd = System.currentTimeMillis();
                tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
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

    private class FitxategienPathak extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params) {
            try {
                emaitza = Globalak.mApi.metadata("/", 1000, null, true, null);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            zuzena=true;
            amaitua=true;
            return emaitza;

        }
    }

    private class JaitsiArgazkia extends AsyncTask<String, Void, Void> {
        private String fitxategi;
        private File file;
        private DropboxAPI.DropboxFileInfo info;
        @Override
        protected Void doInBackground(String... params) {
            try {
                for (String fname : fnames) {
                    fitxategi = fname;
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi);
                    if (!file.exists()) {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        info = Globalak.mApi.getFile(fitxategi, null, outputStream, null);
                        Log.i("Fitxategia", "The file's rev is: " + info.getMetadata().rev);
                    }
                }
            } catch (DropboxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            zuzena=true;
            amaitua=true;
            return null;
        }
    }

    private class WebDeiaArgazkia extends AsyncTask<String, Void, Void> {
        private StringBuilder sb;
        private BufferedReader reader;
        private URL url;
        private HttpURLConnection connection;
        private int statusCode;
        private String line;
        @Override
        protected Void doInBackground(String... params) {
            try {
                if (Globalak.konexio_konf){
                    url = new URL("http://"+ Globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                }else{
                    url = new URL("http://"+ Globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiArgazkia");
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                connection.disconnect();
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

    private class FitxategiaEzabatu extends AsyncTask<String, Void, Void> {
        private String fitxategi;
        private File file;
        private Boolean ezabatua;
        @Override
        protected Void doInBackground(String... params) {
            try {
                for (String fname : fnames) {
                    fitxategi = fname;
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fitxategi);
                    ezabatua=file.delete();
                    if (!file.exists() && ezabatua) {
                        Globalak.mApi.delete(fitxategi);
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

    private class WebDeiaAmatatu extends AsyncTask<String, Void, Void> {
        private StringBuilder sb;
        private BufferedReader reader;
        private HttpURLConnection connection;
        private int statusCode;
        private String line;
        private URL url;
        @Override
        protected Void doInBackground(String... params) {
            try {
                if (Globalak.konexio_konf){
                    url = new URL("http://"+ Globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiAmatatu");
                }else{
                    url = new URL("http://"+ Globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiAmatatu");
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                connection.disconnect();
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

    private class WebDeiaEzabatu extends AsyncTask<String, Void, Void> {
        private String fitxategi;
        private StringBuilder sb;
        private BufferedReader reader;
        private HttpURLConnection connection;
        private int statusCode;
        private String line;
        private URL url;
        @Override
        protected Void doInBackground(String... params) {
            for (String fname : fnames) {
                fitxategi = fname;
                try {
                    if (Globalak.konexio_konf) {
                        url = new URL("http://" + Globalak.url_intra + "/ZaintzaPi/servlet/ZaintzaPiEzabatu?fitxategia=" + fitxategi);
                    } else {
                        url = new URL("http://" + Globalak.url_extra + "/ZaintzaPi/servlet/ZaintzaPiEzabatu?fitxategia=" + fitxategi);
                    }
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    statusCode = connection.getResponseCode();
                    Log.i("Status Kodea", "" + statusCode);
                    if (statusCode == 200) {
                        zuzena = true;
                        sb = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                    }
                    connection.disconnect();
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

    private class WebDeiaMugimendua extends AsyncTask<String, Void, Void> {
        private StringBuilder sb;
        private BufferedReader reader;
        private HttpURLConnection connection;
        private int statusCode;
        private String line;
        private URL url;
        @Override
        protected Void doInBackground(String... params) {
            try {
                if (Globalak.konexio_konf){
                    url = new URL("http://"+ Globalak.url_intra+"/ZaintzaPi/servlet/ZaintzaPiMugimendua");
                }else{
                    url = new URL("http://"+ Globalak.url_extra+"/ZaintzaPi/servlet/ZaintzaPiMugimendua");
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                statusCode = connection.getResponseCode();
                Log.i("Status Kodea", "" + statusCode);
                if (statusCode == 200) {
                    zuzena=true;
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                connection.disconnect();
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