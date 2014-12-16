package com.example.osmeuslugares;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class RecursoIcono {

	private Activity activity;
	private Resources resources;
	private TypedArray drawableIconosLugares;
	private List<String> valoresIconosLugares;

	public RecursoIcono(Activity activity) {
		super();
		this.activity = activity;
		resources = activity.getResources();
		drawableIconosLugares = resources.obtainTypedArray(R.array.iconos_lugares);
		valoresIconosLugares = (List<String>) Arrays.asList(resources.getStringArray(R.array.valores_iconos_lugares));
	}

	public Drawable obtenerDrawableIcon(String icon) {
		resources = activity.getResources();
		
		int posicion = valoresIconosLugares.indexOf(icon);
		if (posicion == -1) {
			posicion = 0;
		}
		return drawableIconosLugares.getDrawable(posicion);
	}
	
}
