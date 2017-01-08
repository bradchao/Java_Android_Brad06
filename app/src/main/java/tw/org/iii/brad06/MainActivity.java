package tw.org.iii.brad06;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private File sdroot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String state = Environment.getExternalStorageState();
        Log.v("brad", state);
        sdroot = Environment.getExternalStorageDirectory();
        Log.v("brad", sdroot.getAbsolutePath());

    }


    public void test1(View v){

    }
}
