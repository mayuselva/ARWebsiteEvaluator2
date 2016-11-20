package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.TextEvaluation.VuforiaSamples.app.ColorDetect;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import shanefuzz.arwebsiteevaluator.R;

public class ColorDetectSplash extends AppCompatActivity {
    ProgressBar pbar;
  /*  private static long SPLASH_MILLIS = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.activity_color_detect_splash, null, false);

        addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                Intent intent = new Intent(ColorDetectSplash.this,
                        DetectColor.class);
                startActivity(intent);

            }

        }, SPLASH_MILLIS);
    }
*/
  private final int WAIT_TIME = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("LoadingScreenActivity  screen started");
        setContentView(R.layout.loading_screen);
        pbar = (ProgressBar) findViewById(R.id.mainSpinner1);
        pbar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Simulating a long running taskm
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Going to Profile Data");
	  /* Create an Intent that will start the ProfileData-Activity. */
                Intent mainIntent = new Intent(ColorDetectSplash.this,DetectColor.class);
                ColorDetectSplash.this.startActivity(mainIntent);
                ColorDetectSplash.this.finish();
            }
        }, WAIT_TIME);
    }
}

