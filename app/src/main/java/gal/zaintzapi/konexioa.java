package gal.zaintzapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class konexioa extends Activity {
    private Button gorde=null;
    private EditText intra=null;
    private EditText extra=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konexioa);
        gorde=(Button)findViewById(R.id.btn_gorde);
        intra=(EditText)findViewById(R.id.editIntra);
        extra=(EditText)findViewById(R.id.editExtra);
        intra.setText(globalak.url_intra);
        extra.setText(globalak.url_extra);
        gorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intra.getText().toString()!=null){
                    globalak.url_intra=intra.getText().toString();
                }
                if(extra.getText().toString()!=null){
                    globalak.url_extra=extra.getText().toString();
                }
                konexioa.this.finish();
            }
        });
    }
}
