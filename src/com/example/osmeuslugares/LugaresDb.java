package com.example.osmeuslugares;

import java.util.Vector;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LugaresDb extends SQLiteOpenHelper {


	private SQLiteDatabase db;
	private static String nombre = "lugares.db";
	private static CursorFactory factory = null;
	private static int version = 5;
	private static String sql;


	public LugaresDb(Context context) {
		super(context, nombre, factory, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		
			crearTablaLugar(db);

			crearTablaCategoria(db);

			insertarCategoriasPrueba();

			insertarLugaresPrueba();
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
				Log.i("INFO", "Base de datos: onUpgrade"+oldVersion+"->"+newVersion);
				if (newVersion > oldVersion) {
					try {
					db.execSQL("DROP TABLE IF EXISTS lugar");
					db.execSQL("DROP INDEX IF EXISTS idx_lug_nombre");
					db.execSQL("DROP TABLE IF EXISTS categoria");
					db.execSQL("DROP INDEX IF EXISTS idx_cat_nombre");
					}catch(Exception e){
						Log.e(this.getClass().toString(), e.getMessage());
					}
					onCreate(db);
					
					Log.i(this.getClass().toString(), "Base de datos actualizada. versi—n 2");
				}
		
	}


	private void crearTablaLugar(SQLiteDatabase db) {
		sql = "CREATE TABLE lugar("
				+ "lug_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "lug_nombre TEXT NOT NULL, "
				+ "lug_categoria_id INTEGER NOT NULL," + "lug_direccion TEXT,"
				+ "lug_ciudad TEXT," + "lug_telefono TEXT, " + "lug_url TEXT);";

		db.execSQL(sql);

		sql = "CREATE UNIQUE INDEX idx_lug_nombre ON Lugar(lug_nombre ASC)";
		db.execSQL(sql);
	}


	private void crearTablaCategoria(SQLiteDatabase db) {
		sql = "CREATE TABLE Categoria("
				+ "cat_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "cat_nombre TEXT NOT NULL, " + "cat_icono TEXT);";

		db.execSQL(sql);

		sql = "CREATE UNIQUE INDEX idx_cat_nombre ON Categoria(cat_nombre ASC)";
		db.execSQL(sql);
	}


	private void insertarCategoriasPrueba() {
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icono) "
				+ "VALUES('Playas','icono_playa')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icono) "
				+ "VALUES('Restaurantes','icono_restaurante')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icono) "
				+ "VALUES('Hoteles','icono_hotel')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icono) "
				+ "VALUES('Otros','icono_otros')");
	}


	private void insertarLugaresPrueba() {
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url) "
				+ "VALUES('Playa de riazor',1, 'Riazor','A Coruña','981000000','www.forocoches.com')");
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url) "
				+ "VALUES('Playa de Miño',1, 'Miño','A Coruña','981000000','www.lavozdegalicia.com')");
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url) "
				+ "VALUES('La mamma',2, 'Comandante Fontanes, 10','A Coruña','981000000','www.rugbyzalaeta.com')");
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url) "
				+ "VALUES('Hotel Nuñez',3, 'Calle montefaro,25','Lugo','982203215','www.google.com')");
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url) "
				+ "VALUES('As Burgas',4, 'Ourense','Ourense','988130406','www.facebook.com')");
		Log.i("INFO", "Registros de prueba insertados");
	}


	public Vector<Lugar> cargarLugaresDesdeBD() {
		Vector<Lugar> resultado = new Vector<Lugar>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT Lugar.*, cat_nombre, cat_icono "
				+ "FROM Lugar join Categoria on lug_categoria_id = cat_id",
				null);
		// Se podr’a usar query() en vez de rawQuery
				// join para recoger nombre categoria, previamente crear tabla de
				// categorias
		while (cursor.moveToNext()) {
			Lugar lugar = new Lugar();
			lugar.setId(cursor.getLong(0));
			lugar.setNombre(cursor.getString(cursor.getColumnIndex(Lugar.C_NOMBRE)));
			int idCategoria = cursor.getInt(cursor.getColumnIndex(Lugar.C_CATEGORIA_ID));
			String nombreCategoria = cursor.getString(cursor.getColumnIndex(Categoria.C_NOMBRE));
			String iconoCategoria = cursor.getString(cursor.getColumnIndex(Categoria.C_ICONO));
			lugar.setCategoria(new Categoria(idCategoria, nombreCategoria,iconoCategoria));
			lugar.setDireccion(cursor.getString(cursor.getColumnIndex(Lugar.C_DIRECCION)));
			lugar.setCiudad(cursor.getString(cursor.getColumnIndex(Lugar.C_CIUDAD)));
			lugar.setTelefono(cursor.getString(cursor.getColumnIndex(Lugar.C_TELEFONO)));
			lugar.setUrl(cursor.getString(cursor.getColumnIndex(Lugar.C_URL)));
			resultado.add(lugar);
		}
		return resultado;
	}

	
	public Vector<Categoria> cargarCategoriasDesdeBD(boolean opcSeleccionar) {
		Vector<Categoria> resultado = new Vector<Categoria>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Categoria ORDER By cat_nombre", null);
		//Como es para un spinner incluir una primera opci—n por defecto
		if (opcSeleccionar) {
			
			resultado.add(new Categoria(0, "Escoge", "icono_nd"));
		}
		
		while (cursor.moveToNext()) {
			Categoria categoria = new Categoria();
			categoria.setId(cursor.getInt(cursor.getColumnIndex(Categoria.C_ID)));
			categoria.setNombre(cursor.getString(cursor.getColumnIndex(Categoria.C_NOMBRE)));
			categoria.setIcon(cursor.getString(cursor.getColumnIndex(Categoria.C_ICONO)));
			resultado.add(categoria);
		}
		return resultado;
	}


	public void createLugar(Lugar newLugar) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert("Lugar", null, newLugar.getContentValues());
	}

	
	public void updateLugar(Lugar lugarEdit) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update("Lugar", lugarEdit.getContentValues(),"lug_id=" + lugarEdit.getId(), null);
	}

	
	public void deleteLugar(Lugar lugarDel) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Lugar", "lug_id=" + lugarDel.getId(), null);
	}


	public void createCategoria(Categoria newCategoria) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert("Categoria", null, newCategoria.getContentValues());
	}

	
	public void updateCategoria(Categoria categoriaEdit) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update("Categoria", categoriaEdit.getContentValues(), "cat_id="+ categoriaEdit.getId(), null);
	}

	public void deleteCategoria(Categoria categoriaDel) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Categoria", "cat_id=" + categoriaDel.getId(), null);
	}

}
