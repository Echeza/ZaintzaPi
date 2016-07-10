package gal.zaintzapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Konexioa extends Activity {
    private Button gorde;
    private EditText intra;
    private EditText extra;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konexioa);
        gorde=(Button)findViewById(R.id.btn_gorde);
        intra=(EditText)findViewById(R.id.editIntra);
        extra=(EditText)findViewById(R.id.editExtra);
        intra.setText(Globalak.url_intra);
        extra.setText(Globalak.url_extra);
        gorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intra.getText().toString()!=null){
                    Globalak.url_intra=intra.getText().toString();
                }
                if(extra.getText().toString()!=null){
                    Globalak.url_extra=extra.getText().toString();
                }
                settings = getSharedPreferences(Globalak.Pref_URL, 0);
                editor = settings.edit();
                editor.putString("url_extra", Globalak.url_extra);
                editor.putString("url_intra", Globalak.url_intra);
                editor.commit();
                Konexioa.this.finish();
            }
        });
    }
}
