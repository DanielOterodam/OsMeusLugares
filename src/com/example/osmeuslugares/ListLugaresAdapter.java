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

public class ListLugaresAdapter extends BaseAdapter {


	private final Activity activity;
	private Vector<Lugar> lista;
	private LugaresDb lugaresDb;
	private RecursoIcono recursoIcono;


	public ListLugaresAdapter(Activity activity) {
		super();
		this.activity = activity;
		this.lista = new Vector<Lugar>();
		lugaresDb = new LugaresDb(activity);
		actualizarDesdeDb();
		this.recursoIcono = new RecursoIcono(activity);
	}


	public void actualizarDesdeDb() throws SQLException {
		this.lista = lugaresDb.cargarLugaresDesdeBD();
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
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.elemento_lista, null, true);
		
		TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewNombreLugar);
		TextView textViewLugar = (TextView) view.findViewById(R.id.textViewTipoBD);
		TextView textViewDireccion = (TextView) view.findViewById(R.id.textViewDireccionBD);
		TextView textViewCiudad = (TextView) view.findViewById(R.id.textViewCiudadBD);
		TextView textViewUrl = (TextView) view.findViewById(R.id.textViewUrlBD);
		TextView textViewTelefono = (TextView) view.findViewById(R.id.textViewTelefonBD);

		Lugar lugar = (Lugar) lista.elementAt(position);

		ImageView imageViewIcono = (ImageView) view.findViewById(R.id.iconoCategoria);
		Drawable icon = recursoIcono.obtenerDrawableIcon(lugar.getCategoria().getIcon());

		imageViewIcono.setImageDrawable(icon);
		textViewTitulo.setText(lugar.getNombre());
		textViewLugar.setText(lugar.getCategoria().getNombre());
		textViewDireccion.setText(lugar.getDireccion());
		textViewCiudad.setText(lugar.getCiudad());
		textViewUrl.setText(lugar.getUrl());
		textViewTelefono.setText(lugar.getTelefono());
		
		return view;
	}
	
}