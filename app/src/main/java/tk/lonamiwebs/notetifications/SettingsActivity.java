package tk.lonamiwebs.notetifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;


public class SettingsActivity extends Activity {

    //region Variables

    SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	Boolean showIn;

    //endregion

    //region Setup
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pfk_showInTaskBar), Context.MODE_PRIVATE);
		showIn = sharedPref.getBoolean(getString(R.string.pfk_showInTaskBar), false);

		((CheckBox) findViewById(R.id.checkShowInBar)).setChecked(showIn);
	}

    //endregion

    //region Save settings
	
	public void showInBar(View view) {
        sharedPref = getSharedPreferences(getString(R.string.pfk_showInTaskBar), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        final CheckBox ch = (CheckBox) view;

        editor.putBoolean(getString(R.string.pfk_showInTaskBar), ch.isChecked());
        editor.commit();
    }

    //endregion

    //region Spam

    public void goLw(View view)
    { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://lonamiwebs.github.io"))); }

    public void contact(View view)
    { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://lonamiwebs.github.io/contacto"))); }

    //endregion

    //region Remove notetifications
	
	public void remPers(View view)
	{
        NotificationManager nMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMan.cancelAll();
	}

    //endregion
}
