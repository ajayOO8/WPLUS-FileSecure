package info.WPLUS.filesecure;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO remove waste toast
public class FileChooser extends ListActivity{
String sessionkey=null;
    int decider=0;
    public String extension=new String("WPLUS");
    private File Fileroot;
    private FileArrayAdapter adapter;
    String fileex;
    Opcion o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Choose File ");
        Intent intent=getIntent();
//setTheme(R.style.FileChooserTheme);
        decider=intent.getIntExtra("decider",0);
        if(decider==2) {
            Fileroot = new File(Environment.getExternalStorageDirectory().getPath() + "/WPLUS/encrypt/");
        findfile(Fileroot);}
        else
        {
            Fileroot= new File(Environment.getExternalStorageDirectory().getPath());
            findfile(Fileroot);}
    }


    private void findfile(File f){

        File[]dirs = f.listFiles();
      //  this.setTitle("Current Dir: "+f.getName());
        List<Opcion> dir = new ArrayList<Opcion>();
        List<Opcion>fls = new ArrayList<Opcion>();




        for(File ff: dirs)
        {
            String ffname=ff.getName();

            if(ffname.charAt(0)=='.') continue;
            if(ff.isDirectory())
                dir.add(new Opcion(ff.getName(),"Folder",ff.getAbsolutePath()));
            else
            {float size=ff.length();
                if(size<1024)

                    fls.add(new Opcion(ff.getName(),"File Size: "+size+" B",ff.getAbsolutePath()));
                else if(size>1024&&size<1048576)
                    fls.add(new Opcion(ff.getName(),"File Size: "+ BigDecimal.valueOf(size/1024)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()+" KB",ff.getAbsolutePath()));
                else
                    fls.add(new Opcion(ff.getName(),"File Size: "+BigDecimal.valueOf((size/1024)/1024)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()+"  MB",ff.getAbsolutePath()));
            }
        }

        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);

        if (decider==2)
        {}

        else if(!f.getName().equalsIgnoreCase(Environment.getExternalStorageDirectory().getName().toString()))
        {dir.add(0, new Opcion("..", "Parent Directory", f.getParent()));
        }
        adapter = new FileArrayAdapter(FileChooser.this,R.layout.activity_file_chooser,dir);
        this.setListAdapter(adapter);
    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        o = adapter.getItem(position);
        //Toast toast =Toast.makeText(getApplicationContext(),direccion.toString(),Toast.LENGTH_SHORT);
        //3toast.show();

        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
            Fileroot = new File(o.getPath());
            findfile(Fileroot);

        }
        else {


            // Toast toast =Toast.makeText(getApplicationContext(),o.getPath(),Toast.LENGTH_SHORT);
            // toast.show();
            File file = new File(o.getPath());
            if(+BigDecimal.valueOf((file.length()/1024)/1024)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue()>50) {
                Toast toast=Toast.makeText(getApplicationContext(),"Get premium for encrypting largerfiles",Toast.LENGTH_SHORT);
                toast.show(); return;}
            final String filepath=new String(o.getPath());
            Intent intent=getIntent();

           decider=intent.getIntExtra("decider",0);
           // sessionkey=intent.getStringExtra("key");
            //key to distinguish encryption and decryption

            String filename=filepath.substring(filepath.lastIndexOf('/')+1);
            if(decider==1)
            {   //getkey();
                if(filename.contains(extension))
                { Toast toast1 = Toast.makeText(getApplicationContext(), "file already encrypted", Toast.LENGTH_SHORT);
                    toast1.show();}

                else
                {   fileex=filename.substring(filename.lastIndexOf('.')+1);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FileChooser.this);
               //     alertDialogBuilder.setTitle("Choose Operation");

                    alertDialogBuilder.setMessage("select this file and proceed?");


                    alertDialogBuilder.setCancelable(true)
                            .setPositiveButton("View file", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //resultText.setText("Hello, " + editText.getText());
                                    //Checking for the file is exist or not
                                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                    Uri path = Uri.fromFile(new File(o.getPath()));
                                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                                    Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                    String mimeType = myMime.getMimeTypeFromExtension(fileex);
                                    newIntent.setDataAndType(Uri.fromFile(new File(filepath)), mimeType);

                                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(newIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(getApplicationContext(), "No Suitable Application found.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            })
                            .setNegativeButton("Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            Intent intent2 =new Intent();
                                            intent2.putExtra("File",filepath);
                                         //   intent2.putExtra("Key",sessionkey);

                                            setResult(RESULT_OK,intent2);
                                          //  startActivity(intent2);
                                            finish();

                                        }
                                                            });



                    // create an alert dialog
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();


                }}

            else if(decider==2)
            {
                if(!filename.contains(extension))
                {Toast toast1 = Toast.makeText(getApplicationContext(), "file is not encrypted", Toast.LENGTH_SHORT);
                    toast1.show();}

                else {  Intent intent2 =new Intent();
                    intent2.putExtra("File",filepath);
                //    intent2.putExtra("Key",sessionkey);

                    setResult(RESULT_OK,intent2);
                   // startActivity(intent2);
                    finish();


                }


            }}}
    void  getkey(final String filepath)
    {if(sessionkey.equals("-1"))
    { AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FileChooser.this);
        alertDialogBuilder.setTitle("Enter Password");
        alertDialogBuilder.setMessage("enter session password");


        final EditText editText =  new EditText(this);
        alertDialogBuilder.setView(editText);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                        sessionkey = editText.getText().toString();

                        //    String filepath = data.getStringExtra("File");
                        Intent intent2 =new Intent();
                        intent2.putExtra("File",filepath);
                       // intent2.putExtra("Key",sessionkey);

                        setResult(RESULT_OK,intent2);
                        startActivity(intent2);
                        finish();


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
       else{ Intent intent2 =new Intent();
        intent2.putExtra("File",filepath);
        //intent2.putExtra("Key",sessionkey);

        setResult(RESULT_OK,intent2);
        startActivity(intent2);
        finish();}
    }


}
