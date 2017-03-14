package info.WPLUS.filesecure;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
//TODO img not sutaible
//TODO show icon for dialog box
public class Broadcast extends AppCompatActivity {
//When a file of EXTENSION is opened this funtion is called
private String tag="tagopen";
    String sessionkey;
    private String filepath="";
    private Uri uri2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_broadcast);
        final Intent intent= getIntent();
        final String action =intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)) {
            uri2=intent.getData();
            filepath=uri2.getPath();

      //      Toast toast2=Toast.makeText(getApplicationContext(),filepath,Toast.LENGTH_LONG);
        //    toast2.show();
           //if(!filepath.contains(".doc"))
           //{Toast toast=Toast.makeText(getApplicationContext(),"cannot open this file",Toast.LENGTH_LONG);
          // toast.show();
           //this.finishAffinity();}

            getkey();

        }
    }
   public void getkey()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Broadcast.this);
        alertDialogBuilder.setIcon(R.drawable.w);
        alertDialogBuilder.setTitle("Enter Password");
        alertDialogBuilder.setMessage("Enter password to view this file");


        final EditText editText =  new EditText(this);
        alertDialogBuilder.setView(editText);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                        sessionkey = editText.getText().toString();

                        //    String filepath = data.getStringExtra("File");


                     decode2(filepath,sessionkey);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        if(editText.getText().toString().length()!=16)
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().length()==16)
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });
    }

    public void decode2(String filepath,String key1) {
        File file = new File(filepath);
        String filename=filepath.substring(filepath.lastIndexOf('/')+1);
        File decryptedFile = new File("/sdcard/WPLUS/decrypt/"+filename);
        try {
            decryptedFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CryptoUtils.decrypt(key1, file, decryptedFile);
            checkresult(2,decryptedFile,filename,key1);
            //calling decryption algo
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(); Toast toast1 = Toast.makeText(getApplicationContext(), "Error(301) occur", Toast.LENGTH_SHORT);
            toast1.show();finish();
        }
    }
    void checkresult(int k,File file, String filename,String key1) {

        {if (CryptoUtils.success == 1) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "File decrypted :\\sdcard\\WPLUS\\decrypt", Toast.LENGTH_SHORT);
                toast1.show();
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String fileex=file.getPath().substring(file.getPath().lastIndexOf('.')+1);
                String mimeType = myMime.getMimeTypeFromExtension(fileex);
                newIntent.setDataAndType(Uri.fromFile(new File(file.getPath())), mimeType);

                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "No Suitable Application found.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            else
            {Toast toast1 = Toast.makeText(getApplicationContext(), "Error(305) occur", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } finish();
    }
}
