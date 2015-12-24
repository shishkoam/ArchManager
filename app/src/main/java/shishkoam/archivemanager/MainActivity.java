package shishkoam.archivemanager;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.loopj.android.http.*;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;


import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MySimpleArrayAdapter adapter;
    ListView list;
    ImageView deleteButton;
    ImageView backButton;
    ImageView renameButton;
    Context cont;
    RelativeLayout toolbarRelativeLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.v7.widget.Toolbar toolbarDeleteMode;
    ArrayList<String> values;
    MainActivity activity;
    public AsyncHttpClient client = new AsyncHttpClient();
    String TextResponse = "";
    RequestParams params = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity activity = this;
        setContentView(R.layout.activity_main);
        cont = this;
        toolbarRelativeLayout = (RelativeLayout) findViewById(R.id.toolbarRelativeLayout);

//        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbarDeleteMode = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarDeleteMode);
        deleteButton = (ImageView) findViewById(R.id.delete);
        backButton = (ImageView) findViewById(R.id.exitFromDeleteMode);
        renameButton = (ImageView) findViewById(R.id.rename);

        list = (ListView) findViewById(R.id.listView);

        values = new ArrayList<String>();
        values.add("Android");
        values.add("iPhone");
        values.add("WindowsMobile");
        values.add("Blackberry");
        values.add("WebOS");
        values.add("Ubuntu");
        values.add("Windows7");
        values.add("Max OS X");
        values.add("Linux");
        initList();
    }

    private void initList() {
        adapter = new MySimpleArrayAdapter(this, values);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                view.setSelected(true);
                String item = (String) list.getAdapter().getItem(position);
                bringToFrontView(toolbarRelativeLayout);
//                switchToolBars(false);
//                setSupportActionBar(toolbarDeleteMode);
                final int selectedPosition = position;
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarRelativeLayout.setVisibility(View.INVISIBLE);
                        view.setSelected(false);
                    }
                });
                renameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRenameDialog(selectedPosition);
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        values.remove(selectedPosition);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void getRenameDialog(final int selectedPosition) {
        runOnUiThread(new Runnable() {
            public void run() {

                final EditText txtUrl = new EditText(cont);
                txtUrl.setTextColor(128);
                txtUrl.setVisibility(View.VISIBLE);

                new AlertDialog.Builder(cont)
                        .setTitle("Rename the item")
                        .setView(txtUrl)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String url = txtUrl.getText().toString();
                                values.set(selectedPosition, url);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });

    }

    public void bringToFrontView(View v) {
        v.bringToFront();
        v.getParent().requestLayout();
        v.invalidate();
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                String data = getText();
                Log.i("shishkoamonsucces", "getxml " + data);

                ParseUtils parseUtils = new ParseUtils();
                ArrayList<String> arrayList = new  ArrayList<String>();
                try {
//                    arrayList = parseUtils.getXml(data);
                    arrayList = parseUtils.getPackages(data);
                }catch (Exception e){
                    e.printStackTrace();
                }

                for (int i = 0; i < arrayList.size(); i++) {
                    Log.i("shishkoamonsucces", "array " + i + " = " + arrayList.get(i));
                    values.add(i,arrayList.get(i));
                }

//                values.add(data);
                        values = arrayList;
                        initList();
//                        adapter = new MySimpleArrayAdapter(cont, arrayList);
//                        list.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }
                });
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFileFromServer() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.20.28:8080/UserManagement/rest/PackageService/packages/1", new AsyncHttpResponseHandler(Looper.getMainLooper()) {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.i("shishkoamonsucces", "array ");
                // called when response HTTP status is "200 OK"
                String data = new String(response);
                Log.i("shishkoamonsucces", "array " + data);
                ParseUtils parseUtils = new ParseUtils();
                ArrayList<String> arrayList = parseUtils.getPackages(data);
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.i("shishkoamonsucces", "array " + i + " = " + arrayList.get(i));
                }
                values = arrayList;
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


    public String getText() {
        // get connection..
        client.get("http://192.168.1.151:8080/UserManagement/rest/PackageService/packages", new TextHttpResponseHandler() {
//            client.get("http://109.86.155.68:8050/FileManager/webresources/filemanager/printfile", new TextHttpResponseHandler() {

                @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                TextResponse = response;
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            }
        });
        return TextResponse;
    }


}