package info.WPLUS.filesecure;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;

public class Parentactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentactivity);
        ActivityCompat.requestPermissions(Parentactivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(Parentactivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);



        Thread t=new Thread(){
    public void run()
    {try{
        int logotimer=0;
        while(logotimer<1001){
            sleep(1000);logotimer+=1000;

    };
      Intent intent = new Intent(Parentactivity.this, WelcomeActivity.class);
        startActivity(intent);
        //finish();
      //  Intent intent2= new Intent(Parentactivity.this, LoginActivity.class);
       // startActivity(intent2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        finally {
        finish();
    }
    }
    };


t.start();

    }


}
