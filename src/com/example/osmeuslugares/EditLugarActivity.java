package com.example.osmeuslugares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditLugarActivity extends Activity {
	private LugaresDb db = new LugaresDb(this);
	private Lugar lugarNuevo;
	private Lugar lugarEdit;
	
	private Spinner spinnerCategoria;
	private TextView editTextNombre;
	private TextView editTextCiudad;
	private TextView editTextDireccion;
	private TextView editTextTelefono;
	private TextView editTextUrl;
	
	CategoriasAdapter categoriasAdapter;
	
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_lugar);

		editTextNombre = (TextView) findViewById(R.id.editTextNombre);
		editTextCiudad = (TextView) findViewById(R.id.editTextCiudad);
		editTextDireccion = (TextView) findViewById(R.id.editTextDireccion);
		editTextTelefono = (TextView) findViewById(R.id.editTextTelefono);
		editTextUrl = (TextView) findViewById(R.id.editTextUrl);
		
		spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
		categoriasAdapter = new CategoriasAdapter(this);
		spinnerCategoria.setAdapter(categoriasAdapter);
		
		lugarEdit = new Lugar();

		Bundle extras = new Bundle();
		extras = getIntent().getExtras();
		flag = extras.getBoolean("flag");
		if (!flag){
			lugarEdit.setBundle(extras);
		}

		establecerValoresEditar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editar_lugar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.guardar_en_editarLugar: {

				if (flag) {
					crearLugarEnBd();
				} else {
					actualizarLugarEnBd();
				}
				finish();
			break;
		}
		case R.id.cerrar_en_editarLugar: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void crearLugarEnBd() {
		db.createLugar(getLugarDesdeCampos());
	}

	private void actualizarLugarEnBd() {
		db.updateLugar(getLugarEdit());
	}

	private void eliminarLugarEnBd() {
		db.deleteLugar(lugarEdit);
	}

	private Lugar getLugarDesdeCampos() {

		lugarNuevo = new Lugar();
		
		lugarNuevo.setNombre(editTextNombre.getText().toString());
		lugarNuevo.setCiudad(editTextCiudad.getText().toString());
		lugarNuevo.setDireccion(editTextDireccion.getText().toString());
		lugarNuevo.setTelefono(editTextTelefono.getText().toString());
		lugarNuevo.setUrl(editTextUrl.getText().toString());

		int position = spinnerCategoria.getSelectedItemPosition();
		lugarNuevo.setCategoria((Categoria) categoriasAdapter.getItem(position));
		
		return lugarNuevo;
	}

	private Lugar getLugarEdit() {
		lugarEdit.setNombre(editTextNombre.getText().toString());
		lugarEdit.setCiudad(editTextCiudad.getText().toString());
		lugarEdit.setDireccion(editTextDireccion.getText().toString());
		lugarEdit.setTelefono(editTextTelefono.getText().toString());
		lugarEdit.setUrl(editTextUrl.getText().toString());
		
		int position = spinnerCategoria.getSelectedItemPosition();
		lugarEdit.setCategoria((Categoria) categoriasAdapter.getItem(position));
		
		return lugarEdit;
	}

	private void establecerValoresEditar() {

		editTextNombre.setText(lugarEdit.getNombre());

		int position = 0;
		if (!flag) {
			position = categoriasAdapter.getPositionById(lugarEdit.getCategoria().getId());
		}
		spinnerCategoria.setSelection(position);
		editTextCiudad.setText(lugarEdit.getCiudad());
		editTextDireccion.setText(lugarEdit.getDireccion());
		editTextTelefono.setText(lugarEdit.getTelefono());
		editTextUrl.setText(lugarEdit.getUrl());
	}

	public void onButtonClickCategoria(View v) {
		Intent i = new Intent(this, ListCategoriasActivity.class);
		startActivity(i);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		categoriasAdapter.cargarDatosBd();
		categoriasAdapter.notifyDataSetChanged();
	}
}