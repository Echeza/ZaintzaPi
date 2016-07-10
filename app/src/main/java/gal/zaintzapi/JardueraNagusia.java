package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JardueraNagusia extends Activity {

    private Button bttn_saioa_hasi=null;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jarduera_nagusia);
        bttn_saioa_hasi=(Button)findViewById(R.id.saioa_hasi);
        bttn_saioa_hasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dropbox_jardueraIntent = new Intent(JardueraNagusia.this, DropboxJarduera.class);
                startActivity(dropbox_jardueraIntent);
            }
        });
        settings = getSharedPreferences(Globalak.Pref_URL, 0);
        Globalak.url_extra = settings.getString("url_extra", Globalak.url_extra);
        Globalak.url_intra = settings.getString("url_intra", Globalak.url_intra);
        Globalak.konexio_konf=settings.getBoolean("konexio_konf", Globalak.konexio_konf);
        Globalak.mugimendu_konf=settings.getBoolean("mugimendu_konf", Globalak.mugimendu_konf);

    }

}
