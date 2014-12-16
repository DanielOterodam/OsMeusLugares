package com.example.osmeuslugares;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListLugaresActivity extends ListActivity {


	private LugaresDb db;
	private ListLugaresAdapter adaptadorLugares;
	Bundle extras = new Bundle();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_lugares);

		db = new LugaresDb(this);
		adaptadorLugares = new ListLugaresAdapter(this);
		setListAdapter(adaptadorLugares);
		registerForContextMenu(super.getListView());
	
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Lugar itemLugar = (Lugar) getListAdapter().getItem(position);
		Bundle extras = itemLugar.getBundle();
		extras.putBoolean("flag", false);
		lanzarEditLugar(extras);
	}


	private void lanzarEditLugar(Bundle extras) {
		Intent i = new Intent(this, EditLugarActivity.class);
		i.putExtras(extras);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_lugares, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.nuevo_lugar:
			extras.clear();
			extras.putBoolean("flag", true);
			lanzarEditLugar(extras);
			return true;

		case R.id.acerca_de:
			Intent i = new Intent(this, ActivityAcercaDe.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_lista_lugares, menu);
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Lugar lugar = (Lugar) adaptadorLugares.getItem(info.position);

		switch (item.getItemId()) {
		case R.id.web_en_navegador:
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("http://" + lugar.getUrl()));
			this.startActivity(i);
			return true;
			
		case R.id.borrar_lugar:
			db.deleteLugar(lugar);
			onRestart();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		adaptadorLugares.actualizarDesdeDb();
		adaptadorLugares.notifyDataSetChanged();
	}
}
