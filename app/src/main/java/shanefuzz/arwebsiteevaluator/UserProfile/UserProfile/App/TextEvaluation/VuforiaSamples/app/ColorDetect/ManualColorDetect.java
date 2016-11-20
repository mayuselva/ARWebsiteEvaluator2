package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.TextEvaluation.VuforiaSamples.app.ColorDetect;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import shanefuzz.arwebsiteevaluator.R;

public class ManualColorDetect extends AppCompatActivity {
    TextView TxtPreccentage,text1,text2,text3,text4,text5,text6,insidetext1,
            insidetext2,insidetext3,insidetext4,insidetext5,insidetext6;
    int color = 0xffffff00;
    View color1,color2,color3,color4,color5,color6;
    private WebView webView;
    public  String c1="",c2="",c3="",c4="",c5="",c6="",value;
    Button bt1,btn2;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_color_detect);
        color1 = (View)findViewById(R.id.color1);
        color2 = (View)findViewById(R.id.color2);
        color3 = (View)findViewById(R.id.color3);
        color4 = (View)findViewById(R.id.color4);
        color5 = (View)findViewById(R.id.color5);
        color6 = (View)findViewById(R.id.color6);
        bt1= (Button) findViewById(R.id.btncal);
        btn2= (Button) findViewById(R.id.cal2);
        TxtPreccentage = (TextView) findViewById(R.id.Textmanlualprecentage);
        progressBar = (ProgressBar) findViewById(R.id.progressbar2);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        text1= (TextView) findViewById(R.id.text1);
        text2= (TextView) findViewById(R.id.text2);
        text3= (TextView) findViewById(R.id.text3);
        text4= (TextView) findViewById(R.id.text4);
        text5= (TextView) findViewById(R.id.text5);
        text6= (TextView) findViewById(R.id.text6);

        insidetext1= (TextView) findViewById(R.id.insidetext1);
        insidetext2= (TextView) findViewById(R.id.insidetext2);
        insidetext3= (TextView) findViewById(R.id.insidetext3);
        insidetext4= (TextView) findViewById(R.id.insidetext4);
        insidetext5= (TextView) findViewById(R.id.insidetext5);
        insidetext6= (TextView) findViewById(R.id.insidetext6);

        color1.setBackgroundColor(Color.parseColor("#000000"));
        color2.setBackgroundColor(Color.parseColor("#000000"));
        color3.setBackgroundColor(Color.parseColor("#000000"));
        color4.setBackgroundColor(Color.parseColor("#000000"));
        color5.setBackgroundColor(Color.parseColor("#000000"));
        color6.setBackgroundColor(Color.parseColor("#000000"));

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,1);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,2);
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,3);
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,4);
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,5);
            }
        });
        color6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false,6);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrecentage(c1,c2,c3,c4,c5,c6);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompaireBackgroundWithFont(c1,c2);
            }
        });

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
    }



    void openDialog(boolean supportsAlpha , final int clickid) {
        ColorDialog dialog = new ColorDialog(ManualColorDetect.this, color, supportsAlpha, new ColorDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(ColorDialog dialog, int color) {
                ManualColorDetect.this.color = color;
                displayColor(clickid);
            }

            @Override
            public void onCancel(ColorDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
     void displayColor(int clickid) {
        String hexColor = "#" + Integer.toHexString(color).substring(2);
         if(clickid==1){
             color1.setBackgroundColor(Color.parseColor(hexColor));
             c1=hexColor;
         }
         else if(clickid==2){
             color2.setBackgroundColor(Color.parseColor(hexColor));
             c2=hexColor;
         }
         else if(clickid==3){
             color3.setBackgroundColor(Color.parseColor(hexColor));
             c3=hexColor;
         }
         else if(clickid==4){
             color4.setBackgroundColor(Color.parseColor(hexColor));
             c4=hexColor;
         }
         else if(clickid==5){
             color5.setBackgroundColor(Color.parseColor(hexColor));
             c5=hexColor;
         }
         else if(clickid==6){
             color6.setBackgroundColor(Color.parseColor(hexColor));
             c6=hexColor;
         }
        Toast.makeText(getApplicationContext(), hexColor, Toast.LENGTH_SHORT).show();
    }

    final class IJavascriptHandler {
        IJavascriptHandler() {
        }
        @JavascriptInterface
        public void sendToAndroid(String text) {
            // this is called from JS with passed value
            Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
           // t.show();
           // TxtPreccentage.setText("Overall Color Matching Precentage :" + text+"% ");

            value=text;

            if(Double.parseDouble(text)<50){

               TxtPreccentage.setTextColor(Color.RED);

            }
            else{
                TxtPreccentage.setTextColor(Color.BLACK);
            }



        }
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // If the url to be loaded starts with the custom protocol, skip
            // loading and do something else
            if (url.startsWith("caphal://")) {

                //Toast.makeText(DetectColor.this, "Custom protocol call", Toast.LENGTH_LONG).show();

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
           Toast.makeText(ManualColorDetect.this, abc+ "JavaScript interface call", Toast.LENGTH_LONG).show();
        }
    }

    private void setPrecentage(final String c1, final String c2, final String c3, final String c4, final String c5, final String c6) {

        if(c1 !="" && c2!="" && c3!= "" && c4!="" && c5!="" && c6!=""){
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("javascript:androidResponse(\'"+c3+"\',\'"+c2+"\',\'"+c4+"\',\'"+c5+"\',\'"+c1+"\',\'"+c6+"\');void(0)");
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


        }
        else
        {
            Toast.makeText(ManualColorDetect.this, "Please fill all the details", Toast.LENGTH_LONG).show();

        }

    }
    private void CompaireBackgroundWithFont(String c1, String c2) {
        if(c1 !="" && c2!=""){
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("javascript:androidResponse2(\'"+c1+"\',\'"+c2+"\');void(0)");
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


        }
        else
        {
            Toast.makeText(ManualColorDetect.this, "Please fill all the details", Toast.LENGTH_LONG).show();

        }

    }

}
