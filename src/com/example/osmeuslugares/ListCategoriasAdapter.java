package com.example.osmeuslugares;

import java.util.Vector;
import android.app.Activity;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCategoriasAdapter extends BaseAdapter {

	private final Activity activity;
	private Vector<Categoria> lista;
	private LugaresDb lugaresDb;
	private RecursoIcono recursoIcono;


	public ListCategoriasAdapter(Activity activity) {
		super();
		this.activity = activity;
		lugaresDb = new LugaresDb(activity);
		this.lista = new Vector<Categoria>();
		cargarDatosDesdeBd();
		this.recursoIcono = new RecursoIcono(activity);
	}


	public void cargarDatosDesdeBd() throws SQLException {
		this.lista = lugaresDb.cargarCategoriasDesdeBD(false);
	}

	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Object getItem(int position) {
		return lista.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		Categoria categoria = (Categoria) getItem(position);
		return categoria.getId();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.elemento_lista_categorias, null,true);
		cargaDatos(position, view);
		return view;
	}


	private void cargaDatos(int position, View view) {
	
		TextView textViewNombre = (TextView) view.findViewById(R.id.textViewNombreCat);
		TextView textViewIcono = (TextView) view.findViewById(R.id.textViewIconoCat);

		Categoria categoria = (Categoria) lista.elementAt(position);
		
		ImageView imageViewIcono = (ImageView) view.findViewById(R.id.imageViewIcono);
		Drawable icon = recursoIcono.obtenerDrawableIcon(categoria.getIcon());

		imageViewIcono.setImageDrawable(icon);
		textViewNombre.setText(categoria.getNombre());
		textViewIcono.setText(categoria.getIcon());

	}

	public int getPositionById(int id) {
		
		Categoria buscar = new Categoria();
		buscar.setId(id);
		return lista.indexOf(buscar);
	}
}
