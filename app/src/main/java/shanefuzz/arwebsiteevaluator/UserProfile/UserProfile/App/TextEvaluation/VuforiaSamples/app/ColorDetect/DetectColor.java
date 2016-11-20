package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.TextEvaluation.VuforiaSamples.app.ColorDetect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import shanefuzz.arwebsiteevaluator.R;

public class DetectColor extends AppCompatActivity {

    Button buttonOpen;
    ImageButton imbtn;
    TextView textUri;
    ImageView imageView;
    TextView textVibrant, textVibrantDark, textVibrantLight;
    TextView textMuted, textMutedDark, textMutedLight,TxtPreccentage;
    View viewVibrant, viewVibrantDark, viewVibrantLight;
    View viewMuted, viewMutedDark, viewMutedLight;
    private WebView webView;
    private static final int RESULT_OK = 1;
    private  static final int CAMERA_REQUEST=1888;
    String value="0.00";
    private static final int RQS_OPEN_IMAGE = 1;

    Uri targetUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("i am here");
        setContentView(R.layout.activity_detectcolor_activity);

        textUri = (TextView) findViewById(R.id.texturi);
        imageView = (ImageView) findViewById(R.id.image);
        buttonOpen = (Button) findViewById(R.id.btnopen);
        buttonOpen.setOnClickListener(buttonOpenOnClickListener);
        imbtn= (ImageButton) findViewById(R.id.imageButton);
        TxtPreccentage= (TextView)findViewById(R.id.TxtPreccentage);
        textVibrant = (TextView)findViewById(R.id.textVibrant);
        textVibrantDark = (TextView)findViewById(R.id.textVibrantDark);
        textVibrantLight = (TextView)findViewById(R.id.textVibrantLight);
        textMuted = (TextView)findViewById(R.id.textMuted);
        textMutedDark = (TextView)findViewById(R.id.textMutedDark);
        textMutedLight = (TextView)findViewById(R.id.textMutedLight);

        viewVibrant = (View)findViewById(R.id.viewVibrant);
        viewVibrantDark = (View)findViewById(R.id.viewVibrantDark);
        viewVibrantLight = (View)findViewById(R.id.viewVibrantLight);
        viewMuted = (View)findViewById(R.id.viewMuted);
        viewMutedDark = (View)findViewById(R.id.viewMutedDark);
        viewMutedLight = (View)findViewById(R.id.viewMutedLight);
        webView = new WebView(this);

       // webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        // Add the custom WebViewClient class
        webView.setWebViewClient(new CustomWebViewClient());

        // Add the javascript interface
        webView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        webView.addJavascriptInterface(new IJavascriptHandler(), "cpjs");
        // Load the example html file to the WebView
        webView.loadUrl("file:///android_asset/ColorDetect/EvaluateColor.html");
        //to hold capture event;
        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureimage();
            }
        });

    }

    private void captureimage() {

        Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String partFilename = currentDateFormat();
        File file = new File("sdcard/image.jpg");
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraintent,CAMERA_REQUEST);

    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }



    View.OnClickListener buttonOpenOnClickListener =
            new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                    if (Build.VERSION.SDK_INT >=
                            Build.VERSION_CODES.KITKAT) {
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    } else {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                    }

                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // set MIME type for image
                    intent.setType("image/*");

                    startActivityForResult(intent, RQS_OPEN_IMAGE);


                }

            };
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {



            if (requestCode == RQS_OPEN_IMAGE) {
                Uri dataUri = data.getData();
                targetUri = dataUri;
                textUri.setText(dataUri.toString());
                updatImage(dataUri);

            }
            if(requestCode==CAMERA_REQUEST && resultCode == Activity.RESULT_OK){

               // Bitmap thumbnail = (Bitmap) data.getExtras().get("data")
               // imageView.setImageBitmap(thumbnail);
                //imageView.getLayoutParams().height=1000;
                //extractProminentColors(thumbnail);
                //methode.saveImage(ivImage);
                File file = new File("sdcard/image.jpg");
                Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                imageView.setImageBitmap(bitmap);
                extractProminentColors(bitmap);
            }
        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    private void updatImage(Uri uri){

        if (uri != null){
            Bitmap bm;
            try {
                bm = BitmapFactory.decodeStream(
                        getContentResolver()
                                .openInputStream(uri));
                imageView.setImageBitmap(bm);

                extractProminentColors(bm);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //extract prominent colors
    /*
    compile 'com.android.support:palette-v7:23.0.1'
    is needed in Gradle dependency
     */
    private void extractProminentColors(final Bitmap bitmap){
        final int defaultColor = 0x000000;
        final ProgressBar progressBar= (ProgressBar) findViewById(R.id.login_progress);
        final ScrollView scrollView= (ScrollView) findViewById(R.id.DcscrollView);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);

                        TxtPreccentage.setText("Overall Color Matching Precentage :" +value+"% ");

                    }
                });
            }
        },3000);
        Timer timer2 =new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Palette p = Palette.from(bitmap).generate();

                        int VibrantColor = p.getVibrantColor(defaultColor);
                        textVibrant.setText("VibrantColor: " + String.format("#%X", VibrantColor));
                        viewVibrant.setBackgroundColor(VibrantColor);

                        int VibrantColorDark = p.getDarkVibrantColor(defaultColor);
                        textVibrantDark.setText("VibrantColorDark: " + String.format("#%X", VibrantColorDark));
                        viewVibrantDark.setBackgroundColor(VibrantColorDark);

                        int VibrantColorLight = p.getLightVibrantColor(defaultColor);
                        textVibrantLight.setText("Detected Background Color: " + String.format("#%X", VibrantColorLight));
                        viewVibrantLight.setBackgroundColor(VibrantColorLight);

                        int MutedColor = p.getMutedColor(defaultColor);
                        textMuted.setText("MutedColor: " + String.format("#%X", MutedColor));
                        viewMuted.setBackgroundColor(MutedColor);

                        int MutedColorDark = p.getDarkMutedColor(defaultColor);
                        textMutedDark.setText("Detected Font color: " + String.format("#%X", MutedColorDark));
                        viewMutedDark.setBackgroundColor(MutedColorDark);

                        int MutedColorLight = p.getLightMutedColor(defaultColor);
                        textMutedLight.setText("MutedColorLight: " + String.format("#%X", MutedColorLight));
                        viewMutedLight.setBackgroundColor(MutedColorLight);
                        String c1= String.format("#%X", VibrantColor);
                        String c2 = String.format("#%X", VibrantColorDark);
                        String c3 = String.format("#%X", VibrantColorLight);
                        String c4 = String.format("#%X", MutedColor);
                        String c5 = String.format("#%X", MutedColorDark);
                        String c6 = String.format("#%X", MutedColorLight);
                        scrollView.getLayoutParams().height=1300;
                        webView.loadUrl("javascript:androidResponse(\'"+c1+"\',\'"+c2+"\',\'"+c3+"\',\'"+c4+"\',\'"+c5+"\',\'"+c6+"\');void(0)");
                                     }
                });
            }
        },2500);



    }

    final class IJavascriptHandler {
        IJavascriptHandler() {
        }

        // This annotation is required in Jelly Bean and later:
        @JavascriptInterface
        public void sendToAndroid(String text) {
            // this is called from JS with passed value
            Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            //t.show();
           // TxtPreccentage.setText("Overall Color Matching Precentage :" + text+"% ");
            value=text;
           if(Double.parseDouble(text)<50){
                System.out.println(text+"fgdhfgfggfgfghfghfghfgvghgh");

                TxtPreccentage.setTextColor(Color.RED);

            }
            else
               TxtPreccentage.setTextColor(Color.BLACK);


        }
    }
    /**
     * CustomWebViewclient is used to add a custom hook to the url loading.
     *
     */
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // If the url to be loaded starts with the custom protocol, skip
            // loading and do something else
            if (url.startsWith("caphal://")) {

                Toast.makeText(DetectColor.this, "Custom protocol call", Toast.LENGTH_LONG).show();

                return true;
            }

            return false;
        }
    }

    /**
     * JavaScriptInterface is the interface class for the application code calls. All public methods
     * annotated with @JavascriptInterface in this class can be called from JavaScript.
     *
     */
    private class JavaScriptInterface {

        @JavascriptInterface
        public void callFromJS(String abc) {
            Toast.makeText(DetectColor.this, abc+ "JavaScript interface call", Toast.LENGTH_LONG).show();
        }
    }

}
