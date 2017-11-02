package tk.lonamiwebs.notetifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import java.util.ArrayList;

public class NotifManagerActivity extends Activity {

    //region Setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_manager);

        final ListView lv = (ListView) findViewById(R.id.listView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showNotif(((Notetification) lv.getItemAtPosition(position)).id);
            }
        });

        registerForContextMenu(lv);
    }

    //endregion

    //region Menus


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notif_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_showAll:
                for (Notetification note : MainActivity.mynotifs)
                    note.show();
                return true;
            case R.id.action_hideAll:
                for (Notetification note : MainActivity.mynotifs)
                    note.hide();
                return true;
            case R.id.action_deleteAll:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void deleteAll() {

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.listview_notif_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        ListView lv = (ListView) findViewById(R.id.listView);
        Notetification noti = (Notetification)lv.getItemAtPosition(info.position);

        switch(item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, EditNotifActivity.class).putExtra("notifId", noti.id));
                return true;
            case R.id.delete:
                MainActivity.removeNotiWithId(noti.id);
                onResume();
                return true;
            case R.id.show:
                noti.show();
                return true;
            case R.id.hide:
                noti.hide();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //endregion

    //region Resume

    protected void onResume() {
        super.onResume();

        final ListView lv = (ListView) findViewById(R.id.listView);

        ArrayList<Notetification> notifs = new ArrayList<Notetification>(MainActivity.mynotifs);

        NotifAdapter adapter = new NotifAdapter(this, notifs);

        lv.setAdapter(adapter);
    }

    //endregion

    //region On item selected

    void showNotif(int notifId) {
        MainActivity.getNotifById(notifId).show();
    }

    //endregion

    //region Menu

    //endregion
}