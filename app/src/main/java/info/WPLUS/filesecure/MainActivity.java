package info.WPLUS.filesecure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.R.attr.id;


public class MainActivity extends AppCompatActivity {
    private AdView mAdView;

    Button quickencrypt,encrypt,decrypt;
    String key = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4635831255863413/4149352688");

         mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        File folder=new File(Environment.getExternalStorageDirectory()+"/WPLUS");
        if(!folder.exists())
        {folder.mkdirs();}
        folder=new File(Environment.getExternalStorageDirectory()+"/WPLUS/decrypt");
        if(!folder.exists())
        {folder.mkdirs();
        }
        folder=new File(Environment.getExternalStorageDirectory()+"/WPLUS/encrypt");
        if(!folder.exists())
        {folder.mkdirs();
        }
        folder=new File(Environment.getExternalStorageDirectory()+"/WPLUS/keys");
        if(!folder.exists())
        {folder.mkdirs();
        }
        folder=new File(Environment.getExternalStorageDirectory()+"/.WPLUS");
        if(!folder.exists())
        {folder.mkdirs();
        }
        File keyfile=new File(Environment.getExternalStorageDirectory()+"/.WPLUS/defaultkey.Wplus");
        if(keyfile.exists())
        {
            try {
                byte[] bytes= new byte[(int) keyfile.length()];
                FileInputStream fileInputStream=new FileInputStream((keyfile));
                fileInputStream.read(bytes);
                String key2=new String (bytes);
key=key2;
                fileInputStream.close();

               // Toast toast1 = Toast.makeText(getApplicationContext(), key2, Toast.LENGTH_SHORT);
               // toast1.show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        { Intent intent =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        quickencrypt = (Button) findViewById(R.id.button3);

        encrypt = (Button) findViewById(R.id.button);
        decrypt = (Button) findViewById(R.id.button2);

        quickencrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle=new Bundle();
                // bundle.putInt("key",1);
                Intent intent = new Intent(MainActivity.this, EnDe_class.class);
                intent.putExtra("decider",1);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
;
                Intent intent = new Intent(MainActivity.this, EnDe_class.class);
               intent.putExtra("decider",1);
               intent.putExtra("key", "-1");
               startActivity(intent);



            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, EnDe_class.class);
                intent.putExtra("key","-1");
               intent.putExtra("decider",2);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {
            if(requestCode== Activity.RESULT_OK)
            {key=data.getStringExtra("key");
            }

        }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.forgetkey) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Find keys on: sdcard/WPLUS/keys/", Toast.LENGTH_SHORT);
            toast1.show();
            // TODO make list of all the file encrypted and show keys to all
            return true;
        }

        if (id == R.id.changequickkey) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));


            return true;
        }   if (id == R.id.bubblesetting) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "This feature is not available right now", Toast.LENGTH_SHORT);
            toast1.show();
            //TODO Bubble draw over activity
            return true;
        }   if (id == R.id.help) {     // but this is for testing
            PrefManager prefManager = new PrefManager(getApplicationContext());

            // make first time launch TRUE
            prefManager.setFirstTimeLaunch(true);

            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
            return true;
        } //  if (id == R.id.rate) {
        //   launchMarket();

          //  return true;
        //}
        if (id == R.id.aboutus) {

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("About us");
            alertDialog.setMessage(R.string.aboutustext);
            //alertDialog.setIcon(R.drawable.ic_discount);
            alertDialog.setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        }
                    });
            alertDialog.show();

            // TODO aboutus activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
   @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
               // .setIcon(android.R.drawable.ic_dialog_alert)

                .setMessage("Are you sure you want to close WPLUS secure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id="+getPackageName());
        Intent marketlink=new Intent(Intent.ACTION_VIEW,uri);
        try{
            startActivity(marketlink);
        }
        catch(ActivityNotFoundException ex) {
            Toast.makeText(this,"sorry not able to open!",Toast.LENGTH_SHORT).show();}
    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
