package tk.lonamiwebs.notetifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Lonami on 08/12/2014.
 */
public class Notetification {

    //region Properties

    public String title;
    public String content;
    public String icon;

    public int id;

    public boolean autoCancel;
    public boolean persists;

    //endregion

    //region Initializers

    public Notetification(String title, String content, String icon, int id, boolean autoCancel, boolean persists) {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.id = id;
        this.autoCancel = autoCancel;
        this.persists = persists;
    }

    public Notetification(String parsedNotif) {
        Notetification notif = unparse(parsedNotif);
        this.title = notif.title;
        this.content = notif.content;
        this.icon = notif.icon;
        this.id = notif.id;
    }

    //endregion

    //region Parse and unparse

    public String parse() {
        String title = this.title.replace(":", "&S;");
        String content = this.content.replace(":", "&S;");
        String icon = this.icon;
        String id = String.valueOf(this.id);
        String autoCancel = String.valueOf(this.autoCancel);
        String persists = String.valueOf(this.persists);

        return title + ":" + content + ":" + icon + ":" + id + ":" + autoCancel + ":" + persists;
    }

    public Notetification unparse(String parsedNotif) {
        String[] split = parsedNotif.split(":");

        String title = split[0].replace("&S;", ":");
        String content = split[1].replace("&S;", ":");
        String icon = split[2];
        int id = Integer.parseInt(split[3]);
        boolean autoCancel = Boolean.valueOf(split[4]);
        boolean persists = Boolean.valueOf(split[5]);

        return new Notetification(title, content, icon, id, autoCancel, persists);
    }

    //endregion

    //region Show / hide

    public void show() {
        Intent resultIntent = new Intent(MainActivity.MainContext, NotifManagerActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(MainActivity.MainContext, 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        int drawable = MainActivity.resources.getIdentifier(icon, "drawable", MainActivity.PackageName);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.MainContext)
                        .setSmallIcon(drawable).setContentTitle(title).setContentText(content)
                        .setAutoCancel(autoCancel).setOngoing(persists).setContentIntent(resultPendingIntent);

        MainActivity.NotifManager.notify(id, mBuilder.build());
    }

    public void hide() {
        MainActivity.NotifManager.cancel(id);
    }

    //endregion

    //region Override methods

    @Override
    public String toString() {
        return title + "\r\n" + content;
    }

    //endregion
}
