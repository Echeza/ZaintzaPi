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
        TextView jpgName = (TextView) findViewById(R.id.jpgname);
        ImageView jpgView = (ImageView) findViewById(R.id.jpgview);
        Bundle extras = argazkia.this.getIntent().getExtras();
        String izena = extras.getString("izena");

        String myJpgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + izena;

        jpgName.setText(myJpgPath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
        jpgView.setImageBitmap(bm);
    }
}
