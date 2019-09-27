package com.webianks.hatkemessenger;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.webianks.hatkemessenger.activities.MainActivity;
import com.webianks.hatkemessenger.activities.SmsOrMailDialog;
import com.webianks.hatkemessenger.adapters.NewSmsAdapter;
import com.webianks.hatkemessenger.constants.Constants;
import com.webianks.hatkemessenger.constants.SmsContract;
import com.webianks.hatkemessenger.services.UpdateSMSService;
import com.webianks.hatkemessenger.utils.SimUtils;

import java.util.List;

public class NewSms extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    public EditText contact, sms;
    private TextView tv_email;
    public ImageView sendeBt, contactBt;
    public String message;
    public final static int RESULT_PICK_CONTACT=1;
    private long _Id;
    private String read;
    private boolean from_reciever;
    private int color;
    private RecyclerView recyclerView;
    private NewSmsAdapter newSmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sms);
        contact = (EditText)findViewById(R.id.txtphoneNo);
        tv_email = (TextView)findViewById(R.id.email);
        sms = (EditText)findViewById(R.id.txtMessage);
        sendeBt = (ImageView)findViewById(R.id.imbtnSendSMS);
        contactBt = (ImageView)findViewById(R.id.imbtncontact);
        // init();
        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setError(null);

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
        sendeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contact.getText().toString().isEmpty()) {
                    openDialog();
                }else contact.setError("Please add contact");

            }
        });

    }

    private void initRcv(List<Smspp> sms) {

        Intent intent = getIntent();
        _Id = intent.getLongExtra(Constants.SMS_ID,-123);
        color = intent.getIntExtra(Constants.COLOR,0);
        read = intent.getStringExtra(Constants.READ);

        from_reciever = intent.getBooleanExtra(Constants.FROM_SMS_RECIEVER, false);

        recyclerView = (RecyclerView) findViewById(R.id.rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerView(sms);

        if (read!=null && read.equals("0")) {
            setReadSMS();
        }
    }
    private void setRecyclerView(List<Smspp> sms) {

         newSmsAdapter= new NewSmsAdapter(this, sms,color);
        recyclerView.setAdapter(newSmsAdapter);
    }
    private void setReadSMS() {

        Intent intent = new Intent(this, UpdateSMSService.class);
        intent.putExtra("id", _Id);
        startService(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null;
                        String name = null;
                        String idContact = null;
                        String email = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int idContactIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);
                        idContact = cursor.getString(idContactIndex);

                        // get just the first email adress if there is
                        ContentResolver cr = getContentResolver();
                        Cursor crEmails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{idContact}, null);
                        if(crEmails.moveToFirst()) {
                            email = crEmails.getString(crEmails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        }
                        crEmails.close();

                        Log.e("Contact number is", name + "," + phoneNo);
                        Log.e("EmailContact is", name + ", " + email);

                        contact.setText(phoneNo);
                        tv_email.setText(email == null ? "No email" : email);

                        Ormlite ormlite = new Ormlite(getApplicationContext());
                        initRcv(ormlite.getSmsppByReceiverNumber(phoneNo));
                        Log.e("smsDB", ormlite.getSmsppByReceiverNumber(phoneNo).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("Failed", "Not able to pick contact");
        }
    }

    public void openDialog() {
        SmsOrMailDialog dialog = new SmsOrMailDialog(new SmsOrMailDialog.SenderInterface() {
            @Override
            public void sendSMS() {
                sendSMSMessage();
            }

            @Override
            public void sendMAIL() {
                sendMAILMessage();
            }
        });

        dialog.show(getSupportFragmentManager(), "dialog sms or mail");
    }

    private void bubble() {
        Ormlite ormlite = new Ormlite(getApplicationContext());
        if (ormlite.insertSms(
                new Smspp(sms.getText().toString(),false,"me",
                        contact.getText().toString()))){
            initRcv(ormlite.getSmsppByReceiverNumber(contact.getText().toString()));
        }
    }

    private void sendMAILMessage() {
        String email_address = tv_email.getText().toString();
        if(email_address.contains("@")) {
            message = sms.getText().toString();
            if (message != null && message.trim().length() > 0) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_address});
                intent.putExtra(Intent.EXTRA_SUBJECT, "ALLSMS");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                this.startActivity(Intent.createChooser(intent, "ALLSMS Par Mail"));
                bubble();
                sms.setText("");
            } else {
                sms.setError(getString(R.string.please_write_message));
            }
        } else {
            Toast.makeText(this, "No email", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendSMSMessage() {

            message = sms.getText().toString();

            if (message != null && message.trim().length() > 0)
                requestPermisions();
            else
                sms.setError(getString(R.string.please_write_message));


        }

    private void requestPermisions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else{
            sendSMSNow();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSNow();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void sendSMSNow() {

            if (new SimUtils().sendSMS(this, contact.getText().toString(),
                    null, sms.getText().toString(), null, null)) {
                Log.e("sending", "success");
                bubble();
                sms.setText("");
            } else {
                Log.e("sending", "false");
            }

//        BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
//        BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();
//
//        String SENT = "SMS_SENT";
//        String DELIVERED = "SMS_DELIVERED";
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
//
//        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
//        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
//
//        try {
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage(contact.getText().toString(), null, message, sentPI, deliveredPI);
//        }catch (Exception e){
//            Toast.makeText(this,getString(R.string.cant_send),Toast.LENGTH_SHORT).show();
//
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (from_reciever)
                    startActivity(new Intent(this, MainActivity.class));

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(Constants.CONVERSATION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] selectionArgs = {contact.getText().toString()};

        return new CursorLoader(this,
                SmsContract.ALL_SMS_URI,
                null,
                SmsContract.SMS_SELECTION,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("on finish","ok");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e("on reset","ok");
    }

}
