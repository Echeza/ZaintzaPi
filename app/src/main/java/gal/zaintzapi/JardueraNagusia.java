package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class JardueraNagusia extends Activity {

    private Button bttn_saioa_hasi=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jarduera_nagusia);
        bttn_saioa_hasi=(Button)findViewById(R.id.saioa_hasi);
        bttn_saioa_hasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dropbox_jardueraIntent = new Intent(JardueraNagusia.this, dropbox_jarduera.class);
                startActivity(dropbox_jardueraIntent);
            }
        });
        SharedPreferences settings = getSharedPreferences(globalak.Pref_URL, 0);
        globalak.url_extra = settings.getString("url_extra", globalak.url_extra);
        globalak.url_intra = settings.getString("url_intra", globalak.url_intra);

    }

}
