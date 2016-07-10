package gal.zaintzapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import gal.zaintzapi.TouchView.TouchImageView;

public class Argazkia extends Activity{

    private TextView jpgIzena;
    private TouchImageView jpgArgazkia;
    private Bundle extras;
    private String izena;
    private String myJpgPath;
    private BitmapFactory.Options options;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazkia);
        jpgIzena = (TextView) findViewById(R.id.jpgizena);
        jpgArgazkia = (TouchImageView) findViewById(R.id.jpgargazkia);
        extras = Argazkia.this.getIntent().getExtras();
        izena = extras.getString("izena");
        myJpgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + izena;
        jpgIzena.setText(izena.split("/")[1].split(".jpg")[0]);
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bm = BitmapFactory.decodeFile(myJpgPath, options);
        jpgArgazkia.setImageBitmap(bm);
    }
}
