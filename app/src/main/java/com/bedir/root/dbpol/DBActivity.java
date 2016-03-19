package com.bedir.root.dbpol;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class DBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        final DBPolHelper db = new DBPolHelper(this);

        db.addPlate(new Plate("43 ADSF 23", "Wei Meng Lee"));
        db.addPlate(new Plate("12 QEWR 993", "Bill Phillips and Brian Hardy"));
        db.addPlate(new Plate("06 URNU 324", "Wallace Jackson"));

        final List<Plate> plateList = db.getAllPlates();
        final ListView l = (ListView) findViewById(R.id.listView1);

        final ArrayAdapter<Plate> plateAdapter = new ArrayAdapter<Plate>(this,R.layout.list_item,R.id.list_text,plateList);

        l.setAdapter(plateAdapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        Runnable run = new Runnable() {
                public void run() {
                    //reload content
                    plateList.clear();
                    plateList.addAll(db.getAllPlates());
                    plateAdapter.notifyDataSetChanged();
                    l.invalidateViews();
                    l.refreshDrawableState();
                }
            };

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            AlertDialog.Builder dialogBuilder =
                    new AlertDialog.Builder(DBActivity.this);
            final Plate selected = plateList.get(position);
            dialogBuilder.setMessage(selected.getPlate()+" "+selected.getRecord())
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton(getString(R.string.erase_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Deletion code
                            db.deletePlate(selected);
                            runOnUiThread(run);
                            dialog.dismiss();
                        }
            }).setNeutralButton(getString(R.string.update_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Update page code
                    dialog.dismiss();
                }
                    });
            dialogBuilder.create().show();

        }
    });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatemen

        return super.onOptionsItemSelected(item);
    }
}
