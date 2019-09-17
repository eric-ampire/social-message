package com.webianks.hatkemessenger.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Telephony;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.NewSms;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.SMS;
import com.webianks.hatkemessenger.adapters.AllConversationAdapter;
import com.webianks.hatkemessenger.adapters.ItemCLickListener;
import com.webianks.hatkemessenger.constants.Constants;
import com.webianks.hatkemessenger.constants.SmsContract;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ItemCLickListener, LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, MultiplePermissionsListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private AllConversationAdapter allConversationAdapter;
    private String TAG = MainActivity.class.getSimpleName();
    private String mCurFilter;
    private List<SMS> data;
    private LinearLayoutManager linearLayoutManager;
    private BroadcastReceiver mReceiver;
    private ProgressBar progressBar;
    private List<Contact> contacts;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.fab_new);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener(this);

        if (checkDefaultSettings()) {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        List<String> permission = new ArrayList<>();
        permission.add(Manifest.permission.READ_SMS);
        permission.add(Manifest.permission.RECEIVE_SMS);
        permission.add(Manifest.permission.READ_CONTACTS);
        permission.add("android.permission.WRITE_SMS");
        permission.add(Manifest.permission.SEND_SMS);
        permission.add(Manifest.permission.READ_PHONE_STATE);

        Dexter.withActivity(this)
            .withPermissions(permission)
            .withListener(this)
            .check();
    }

    private boolean checkDefaultSettings() {

        boolean isDefault = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("This app is not set as your default messaging app. Do you want to set it as default?")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissions();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(19)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                                startActivity(intent);
                                checkPermissions();
                            }
                        });
                builder.show();

                isDefault = false;
            } else
                isDefault = true;
        }
        return isDefault;
    }


    private void setRecyclerView(List<SMS> data, List<Contact> contacts) {
        allConversationAdapter = new AllConversationAdapter(this, data, contacts);
        allConversationAdapter.setItemClickListener(this);
        recyclerView.setAdapter(allConversationAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab_new:

                startActivity(new Intent(this, NewSms.class)
                        .putExtra(Constants.COLOR,2)
                        .putExtra(Constants.SMS_ID, 0)
                        .putExtra(Constants.READ, ""));
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                boolean new_sms = intent.getBooleanExtra("new_sms", false);

                if (new_sms)
                    getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, MainActivity.this);

            }
        };

        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_settings:
                startActivity(new Intent(this, Groupes.class));
                break;
            case R.id.ic_preference:
                startActivity(new Intent(this, Preference.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Can't access messages.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


    @Override
    public void itemClicked(int color, String contact, long id,String read) {

        Intent intent = new Intent(this, SmsDetailedView.class);
        intent.putExtra(Constants.CONTACT_NAME, contact);
        intent.putExtra(Constants.COLOR, color);
        intent.putExtra(Constants.SMS_ID, id);
        intent.putExtra(Constants.READ, read);
        startActivity(intent);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        contacts = allContact();
        String selection = null;
        String[] selectionArgs = null;

        if (mCurFilter != null) {
            selection = SmsContract.SMS_SELECTION_SEARCH;
            selectionArgs = new String[]{"%" + mCurFilter + "%", "%" + mCurFilter + "%"};
        }

        return new CursorLoader(this,
                SmsContract.ALL_SMS_URI,
                null,
                selection,
                selectionArgs,
                SmsContract.SORT_DESC);
    }

    public List<Contact> allContact() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<Contact> contacts = new ArrayList<Contact>();
        while (phones.moveToNext()) {
            for (int i = 0; i < phones.getColumnCount(); i++) {

                try {
                    Log.i(phones.getColumnName(i) + "", phones.getString(i));
                } catch (Exception e) {
                    Log.i("ereur", i + "");
                }

            }

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i("num ", name);
            Log.i("nom ", phoneNumber);
            contacts.add(new Contact(name, phoneNumber, 0));
        }
        phones.close();
        return contacts;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, final Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {
            getAllSmsToFile(cursor);
            progressBar.setVisibility(View.GONE);


        } else {
            //no sms
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        data = null;
        allConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurFilter = !TextUtils.isEmpty(query) ? query : null;
        getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, this);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(this.mReceiver);
        getSupportLoaderManager().destroyLoader(Constants.ALL_SMS_LOADER);
    }


    public void getAllSmsToFile(Cursor c) {

        List<SMS> lstSms = new ArrayList<SMS>();
        SMS objSMS = null;
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                try {
                    objSMS = new SMS();
                    objSMS.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
                    objSMS.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                    objSMS.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSMS.setReadState(c.getString(c.getColumnIndex("read")));
                    objSMS.setTime(c.getLong(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSMS.setFolderName("inbox");
                    } else {
                        objSMS.setFolderName("sent");
                    }

                } catch (Exception e) {

                } finally {

                    lstSms.add(objSMS);
                    c.moveToNext();
                }
            }
        }
        c.close();

        data = lstSms;

        //Log.d(TAG,"Size before "+data.size());
        sortAndSetToRecycler(lstSms);

    }

    private void sortAndSetToRecycler(List<SMS> lstSms) {

        Set<SMS> s = new LinkedHashSet<>(lstSms);
        data = new ArrayList<>(s);
        setRecyclerView(data, contacts);

        convertToJson(lstSms);
    }

    private void convertToJson(List<SMS> lstSms) {

        Type listType = new TypeToken<List<SMS>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(lstSms, listType);

        SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SMS_JSON, json);
        editor.apply();
        //List<String> target2 = gson.fromJson(json, listType);
        //Log.d(TAG, json);

    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

    }
}
