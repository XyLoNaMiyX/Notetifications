package tk.lonamiwebs.notetifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


public class EditNotifActivity extends Activity implements AdapterView.OnItemSelectedListener {

    //region Variables

    Notetification workingNotif;

    //endregion

    //region Setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notif);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_notifIcon);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_notifIcon, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("notifId", -1);

        workingNotif = MainActivity.getNotifById(id);


        ((EditText) findViewById(R.id.edit_notifTitle)).setText(workingNotif.title);
        ((EditText) findViewById(R.id.edit_notif)).setText(workingNotif.content);
        spinner.setSelection(getIndex(spinner, workingNotif.icon));
        ((CheckBox) findViewById(R.id.checkCancel)).setChecked(workingNotif.autoCancel);
        ((CheckBox) findViewById(R.id.checkPersist)).setChecked(workingNotif.persists);
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

    //region Views

    public void cancelB(View view) {
        finish();
    }

    public void showNotif(View view) {
        refreshNoti();
        workingNotif.show();
    }

    public void hideNotif(View view) {
        workingNotif.hide();
    }

    public void deleteB(View view) {
        MainActivity.removeNotiWithId(workingNotif.id);
        finish();
    }

    public void acceptB(View view) {
        refreshNoti();

        MainActivity.addNoti(workingNotif);
        finish();
    }

    //endregion

    //region Others

    int getIndex(Spinner spinner, String str)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
                index = i;
                break;
            }
        }
        return index;
    }

    void refreshNoti() {
        String title = ((EditText) findViewById(R.id.edit_notifTitle)).getText().toString();
        String content = ((EditText) findViewById(R.id.edit_notif)).getText().toString();
        String icon = ((Spinner) findViewById(R.id.spinner_notifIcon)).getSelectedItem().toString();
        boolean autoCancel = ((CheckBox) findViewById(R.id.checkCancel)).isChecked();
        boolean persists = ((CheckBox) findViewById(R.id.checkPersist)).isChecked();

        workingNotif = new Notetification(title, content, icon, workingNotif.id, autoCancel, persists);
    }

    //endregion

}
