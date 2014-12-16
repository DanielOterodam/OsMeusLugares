package com.example.osmeuslugares;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditCategoriaActivity extends Activity {

	private LugaresDb db = new LugaresDb(this);
	private TextView editTextNombreCategoria;
	private Spinner spinnerIcono;
	private Bundle extras;
	private Categoria categoriaNueva;
	private Categoria categoriaEdit;

	private boolean flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_categoria);

		editTextNombreCategoria = (TextView) findViewById(R.id.editNombreCategoria);
		spinnerIcono = (Spinner) findViewById(R.id.spinnerIcono);
		categoriaEdit = new Categoria();
		extras = new Bundle();
		extras = getIntent().getExtras();
		flag = extras.getBoolean("flag");
		if (!flag) {
			categoriaEdit.setBundle(extras);
		}
		establecerValoresEditar();
	}

	
	private void establecerValoresEditar() {
		
		editTextNombreCategoria.setText(categoriaEdit.getNombre());

		int position = -1;
		if (!flag) {
			position = obtenerIcono(categoriaEdit.getIcon());
		}
		spinnerIcono.setSelection(position);

	}

	private int obtenerIcono(String icono) {
		SpinnerAdapter adaptadorSpinner = this.spinnerIcono.getAdapter();
		String aux;
		int pos = 0;
		for (int i = 0; i < adaptadorSpinner.getCount(); i++) {
			aux = (String) adaptadorSpinner.getItem(i);
			if (aux.equals(icono)) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editar_categoria, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.guardar_en_editarCategoria: {
				if (flag) {
					db.createCategoria(getCategoriaDesdeCampos());
				} else {
					db.updateCategoria(getCategoriaEdit());
				}
				finish();
				break;
			}
			case R.id.cerrar_en_editarCategoria: {
				finish();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}


	private Categoria getCategoriaDesdeCampos() {
		categoriaNueva = new Categoria();
		categoriaNueva.setNombre(editTextNombreCategoria.getText().toString());
		categoriaNueva.setIcon(spinnerIcono.getSelectedItem().toString());
		return categoriaNueva;
	}

	private Categoria getCategoriaEdit() {
		categoriaEdit.setNombre(editTextNombreCategoria.getText().toString());
		categoriaEdit.setIcon(spinnerIcono.getSelectedItem().toString());
		return categoriaEdit;
	}

}
