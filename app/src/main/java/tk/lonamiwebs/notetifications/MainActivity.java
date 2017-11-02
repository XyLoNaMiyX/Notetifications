package tk.lonamiwebs.notetifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity implements OnItemSelectedListener {

    //region Static variables

    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;
    public static Set<Notetification> mynotifs = new HashSet<Notetification>();

    public final static String EXTRA_MESSAGE = "lowe.lonamiwebs.notetifications.MESSAGE";
    static final String pfk_myNotifications = "lowe.lonamiwebs.notetifications.MY_NOTIFICATIONS";

    public static Context MainContext;
    public static NotificationManager NotifManager;
    public static Resources resources;
    public static String PackageName;

    //endregion

    //region Variables

    int id = 0;

    //endregion

    //region Setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_notifIcon);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_notifIcon, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        MainContext = getApplicationContext();
        NotifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        resources = getResources();
        PackageName = getPackageName();

        sharedPref = getSharedPreferences(pfk_myNotifications, Context.MODE_PRIVATE);
        Set<String> parsedNotis = sharedPref.getStringSet(pfk_myNotifications, new HashSet<String>());

        for (String parsedNoti : parsedNotis)
            mynotifs.add(new Notetification(parsedNoti));

        refreshId();
    }

    //endregion

    //region Resume

    protected void onResume()
    {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pfk_showInTaskBar), Context.MODE_PRIVATE);
        boolean showIn = sharedPref.getBoolean(getString(R.string.pfk_showInTaskBar), false);

        if (showIn)
            showDefaultInTaskBar();
        else {
            NotificationManager nMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMan.cancel(10000);
        }
    }

    //endregion

    //region Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_close:
                closeApp();
                return true;
            case R.id.action_mynote:
                openNotifsmgr();
                return true;
            case R.id.action_contact:
                Intent openUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lonamiwebs.github.io/contacto.htm"));
                startActivity(openUrl);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings()
    { startActivity(new Intent(this, SettingsActivity.class)); }

    void openNotifsmgr()
    { startActivity(new Intent(this, NotifManagerActivity.class)); }

    public void closeApp()
    {
        super.finish();
    }

    //endregion

    //region Show in taskbar

    public void showDefaultInTaskBar()
    {
        //We do build Notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Setting up some things
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Create notetification")
                        .setContentText("Create new notetification now!");

        //Setting up an intent
        Intent notifIntent = new Intent(this, MainActivity.class);
        PendingIntent notifPendingIntent =
                PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        mBuilder.setContentIntent(notifPendingIntent);
        mBuilder.setOngoing(true);
        NotificationManager mNotifManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Show
        mNotifManager.notify(10000, mBuilder.build());
    }

    //endregion

    //region Warnings

    public void checkPersist(View view)
    {
        final CheckBox ch = (CheckBox) view;
        boolean checked = ch.isChecked();
        if (checked)
        {
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);

            alBuilder.setMessage(R.string.setPers)
                    .setTitle(R.string.setPersTitle)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yeh, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Clicked OK
                        }
                    })
                    .setNegativeButton(R.string.nay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Clicked Nay
                            ch.setChecked(false);
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = alBuilder.create();
            dialog.show();
        }

    }

    //endregion

    //region Icon

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        parent.getItemAtPosition(pos);
        String notifIcon = parent.getItemAtPosition(pos).toString();

        int drawableID = this.getResources().getIdentifier(notifIcon, "drawable", this.getPackageName());

        ImageView image = (ImageView) findViewById(R.id.imageViewIcon);
        image.setImageResource(drawableID);
    }

    public void onNothingSelected(AdapterView<?> parent) { /* Nothing */ }

    public void decreaseIndex(View view)
    {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_notifIcon);
        int beforeIndex = spinner.getSelectedItemPosition();

        if (beforeIndex != 0)
            spinner.setSelection(beforeIndex - 1);
    }

    public void increaseIndex(View view)
    {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_notifIcon);
        int beforeIndex = spinner.getSelectedItemPosition();

        String[] icons = getResources().getStringArray(R.array.spinner_notifIcon);
        int iCount = icons.length - 1;
        if (beforeIndex != iCount)
            spinner.setSelection(beforeIndex + 1);
    }

    //endregion

    //region Create notetification

    public void setNotif(View view)
    {
        String title = ((EditText) findViewById(R.id.edit_notifTitle)).getText().toString();
        String content = ((EditText) findViewById(R.id.edit_notif)).getText().toString();
        String icon = ((Spinner) findViewById(R.id.spinner_notifIcon)).getSelectedItem().toString();
        boolean autoCancel = ((CheckBox) findViewById(R.id.checkCancel)).isChecked();
        boolean persists = ((CheckBox) findViewById(R.id.checkPersist)).isChecked();

        Notetification notif = new Notetification(title, content, icon, id, autoCancel, persists);
        notif.show();
        addNoti(notif);

        Toast.makeText(getApplicationContext(), this.getString(R.string.notifCreated), Toast.LENGTH_SHORT).show();

        refreshId();
    }

    //endregion

    //region Others

    void refreshId() {
        while (existsNotifWithId(id))
            if (id < 9999)
                id++;
            else
                break;
    }

    //endregion

    //region Notetification utils

    public static void addNoti(Notetification noti) {
        for (Notetification note : mynotifs)
            if (note.id == noti.id) {
                mynotifs.remove(note);
                break;
            }

        mynotifs.add(noti);

        updatePrefs();
    }

    public static void updatePrefs() {
        editor = sharedPref.edit();

        Set<String> parsedNotifs = new HashSet<String>();
        for (Notetification noti : mynotifs)
            parsedNotifs.add(noti.parse());

        editor.putStringSet(pfk_myNotifications, parsedNotifs);

        editor.apply();
        //editor.commit();
    }

    public static void removeNotiWithId(int id) {
        for (Notetification note : mynotifs)
            if (note.id == id) {
                mynotifs.remove(note);
                updatePrefs();
                return;
            }

        updatePrefs();
    }

    public static Notetification getNotifById(int id) {
        for (Notetification note : mynotifs)
            if (note.id == id)
                return note;

        return null;
    }

    public static boolean existsNotifWithId(int id) {
        for (Notetification note : mynotifs)
            if (note.id == id)
                return true;

        return false;
    }

    //endregion

}