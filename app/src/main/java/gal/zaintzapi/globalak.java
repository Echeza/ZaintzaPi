package gal.zaintzapi;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public abstract class Globalak {

    protected static DropboxAPI<AndroidAuthSession> mApi=null;
    protected static String url_intra="192.168.1.55:8080";
    protected static String url_extra="echezaservidor.ddns.net";
    protected static Boolean konexio_konf=true;
    protected static Boolean mugimendu_konf=false;
    protected static Integer fitxategiKantitatea=null;
    protected static final String Pref_URL = "Mis_URL";

}
