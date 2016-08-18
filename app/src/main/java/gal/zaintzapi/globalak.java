package gal.zaintzapi;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public abstract class Globalak {

    protected static DropboxAPI<AndroidAuthSession> mApi=null;
    protected static String urlIntra ="192.168.1.55:8080";
    protected static String urlExtra ="echezaservidor.ddns.net";
    protected static boolean konexioKonf =true;
    protected static boolean mugimenduKonf =false;
    protected static Integer fitxategiKantitatea=null;
    protected static final String prefUrl = "Mis_URL";

}
