package info.WPLUS.filesecure;
//TODO loading icon while processing file
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//TODO dialog before img and video
public class EnDe_class extends Activity {
    String sessionkey=null;
    int decider;
    boolean temp=false;
    public final String extension="-WPLUS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_en_de_class);

        Intent intent=getIntent();

        decider=intent.getIntExtra("decider", 0);//key to distinguish encryption and decryption
        String key=intent.getStringExtra("key");

        sessionkey=key;
       // if(sessionkey.equals("-1")){
       //     getkey();
     //   }
   // else
        {    Intent callfilechooser = new Intent(this, FileChooser.class);
        callfilechooser.putExtra("decider", decider);
    //    callfilechooser.putExtra("key",sessionkey);
        startActivityForResult(callfilechooser, 1);

    }}
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data == null) {
                finish();
            } else {
                {
                    if(resultCode==RESULT_OK)
                    {
                        String filepath = data.getStringExtra("File");
                     //   sessionkey = data.getStringExtra("key");

                        if(sessionkey.equals("-1"))
                            try {
                                getkey(filepath);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        else{
                            if (decider == 1) encode(filepath,sessionkey);
                            if (decider == 2) decode(filepath,sessionkey);
                        }
                    }
                    else{Toast toast=Toast.makeText(getApplicationContext(),"error(303) occur",Toast.LENGTH_SHORT);
                    toast.show();finish();}
                }
            }
        }
    }


    public void encode(String filepath,String key1) {
        File file = new File(filepath);
        String filename=filepath.substring(filepath.lastIndexOf('/')+1);
        String fileex=filename.substring(filename.lastIndexOf('.'));


        String enn = new String("/sdcard/WPLUS/encrypt/" + filename+ extension+fileex);
        File encryptedFile = new File(enn.toString());
        try {
            encryptedFile.createNewFile();
            //encryped file

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CryptoUtils.encrypt(key1, file, encryptedFile);     //calling encryption algo
            checkresult(1,encryptedFile,filename+fileex,key1);
        } catch (CryptoException ex) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "File is corrupted/Invalid password!", Toast.LENGTH_SHORT);
            toast1.show();
            System.out.println(ex.getMessage());

            ex.printStackTrace();

 finish();       }
        //Toast toast1 = Toast.makeText(getApplicationContext(), key1, Toast.LENGTH_SHORT);
        //toast1.show();
        temp=false;}

     public void decode(String filepath,String key1) {
        File file = new File(filepath);
        String filename=filepath.substring(filepath.lastIndexOf('/')+1,filepath.indexOf('.'));
         String fileex=filepath.substring(filepath.lastIndexOf('.'));
        File decryptedFile = new File("/sdcard/WPLUS/decrypt/"+filename+fileex);
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
         finish();
    }
    void checkresult(int k,File file, String filename,String key1) {
        if (k == 1) {
            if (CryptoUtils.success == 1) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "File encrypted :\\sdcard\\WPLUS\\encrypt", Toast.LENGTH_SHORT);
                toast1.show();
                //save password
                File keyfile=new File(Environment.getExternalStorageDirectory()+"/WPLUS/keys/"+filename.substring(0,filename.lastIndexOf('.'))+"password"+".txt");

                try {
                    FileOutputStream fileOutputStream=new FileOutputStream(keyfile);
                    fileOutputStream.write(key1.getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                String fileex=file.getPath().substring(file.getPath().lastIndexOf('.')+1);

                String mimeType = myMime.getMimeTypeFromExtension(fileex);

                Intent shareIntent= new Intent(Intent.ACTION_SEND);
                shareIntent.setType(mimeType);
                Uri uri= Uri.fromFile(file);
                shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(shareIntent,"Share Encrypted File"));
                finish();
            }
            else
            {Toast toast1 = Toast.makeText(getApplicationContext(), "Error(302) occur while encrypting file!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } else if (k == 2) {
            if (CryptoUtils.success == 1) {
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
            {Toast toast1 = Toast.makeText(getApplicationContext(), "Invalid password!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } finish();
    }

    void  getkey(final String filepath) throws InterruptedException {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EnDe_class.this);
      //  alertDialogBuilder.setTitle("Enter Password");
        alertDialogBuilder.setMessage("Enter Password");


        final EditText editText =  new EditText(this);
        alertDialogBuilder.setView(editText);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                        sessionkey = editText.getText().toString();

                        //    String filepath = data.getStringExtra("File");

                        if (decider == 1) encode(filepath,sessionkey);
                        if (decider == 2) decode(filepath,sessionkey);

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
}
