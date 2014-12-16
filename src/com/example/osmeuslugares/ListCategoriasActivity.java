package com.example.osmeuslugares;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListCategoriasActivity extends ListActivity {

	private ListCategoriasAdapter adaptadorCategorias;
	private LugaresDb db;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_categorias);
		
		registerForContextMenu(super.getListView());
		db = new LugaresDb(this);
		extras = new Bundle();
		adaptadorCategorias = new ListCategoriasAdapter(this);
		this.setListAdapter(adaptadorCategorias);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Categoria categoria = (Categoria) adaptadorCategorias.getItem(position);
		Bundle extras = categoria.getBundle();
		extras.putBoolean("flag", false);
		lanzarEditCategoria(extras);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_categorias, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.nueva_categoria: {
			extras.clear();
			extras.putBoolean("flag", true);
			lanzarEditCategoria(extras);
			return true;
		}
		case R.id.cerrar_en_listaCategorias: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_lista_categorias, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		Categoria categoria = (Categoria) adaptadorCategorias.getItem(info.position);

		switch (item.getItemId()) {
		case R.id.editar_categoria:

			Bundle extras = categoria.getBundle();
			extras.putBoolean("flag", false);
			lanzarEditCategoria(extras);
			return true;

		case R.id.borrar_categoria:
			db.deleteCategoria(categoria);
			onRestart();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void lanzarEditCategoria(Bundle extras) {
		Intent i = new Intent(this, EditCategoriaActivity.class);
		i.putExtras(extras);
		startActivity(i);

	}


	@Override
	protected void onRestart() {
		super.onRestart();
		adaptadorCategorias.cargarDatosDesdeBd();
		adaptadorCategorias.notifyDataSetChanged();
	}

}
