package gal.zaintzapi;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public abstract class globalak {
    public static DropboxAPI<AndroidAuthSession> mApi=null;
    public static String url_intra="192.168.1.55:8080";
    public static String url_extra="echezaservidor.ddns.net";
    public static Boolean konexio_aukera=true;
    public static final String Pref_URL = "Nire_URLak";

}
