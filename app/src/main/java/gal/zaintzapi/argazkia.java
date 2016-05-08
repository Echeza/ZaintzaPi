package gal.zaintzapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

public class argazkia extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.argazkia);
        TextView jpgIzena = (TextView) findViewById(R.id.jpgizena);
        ImageView jpgArgazkia = (ImageView) findViewById(R.id.jpgargazkia);
        Bundle extras = argazkia.this.getIntent().getExtras();
        String izena = extras.getString("izena");

        String myJpgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + izena;

        jpgIzena.setText(izena.split("/")[1].split(".jpg")[0]);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
        jpgArgazkia.setImageBitmap(bm);
    }
}
