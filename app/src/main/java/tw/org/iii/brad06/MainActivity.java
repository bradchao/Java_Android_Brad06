package tw.org.iii.brad06;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private File sdroot, approot, file1, file2, photoFile;
    private WebView webview;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.img);
        webview = (WebView)findViewById(R.id.webview);

        String state = Environment.getExternalStorageState();
        Log.v("brad", state);
        sdroot = Environment.getExternalStorageDirectory();
        Log.v("brad", sdroot.getAbsolutePath());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }else {
            init();
        }

    }

    private void init(){
        approot = new File(sdroot, "Android/data/" + getPackageName());
        if (!approot.exists()) {
            approot.mkdirs();
        }
        initWebView();
    }

    private void initWebView(){
        webview.setWebViewClient(new WebViewClient());
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/brad.html");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    public void test1(View v){
        file1 = new File(approot, "brad.txt");
        try {
            FileWriter writer = new FileWriter(file1);
            writer.write("Hello, World\nLine2\nLine3\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Save1 OK", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void test2(View v){
        file2 = new File(sdroot, "brad.txt");
        try {
            FileWriter writer = new FileWriter(file2);
            writer.write("Hello, World\nLine2\nLine3\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Save2 OK", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // call other camera
    public void test3(View v){
        photoFile = new File(sdroot, "myicon.jpg");

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap bmp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            img.setImageBitmap(bmp);

            new Thread(){
                @Override
                public void run() {
                    try {
                        MultipartUtility mu = new MultipartUtility(
                                "http://10.0.2.2:8080/BradWeb/Brad03.jsp",
                                "UTF-8");
                        mu.addFilePart("upload", photoFile);
                        mu.finish();
                    }catch (Exception ee){

                    }

                }
            }.start();

        }
    }
}
