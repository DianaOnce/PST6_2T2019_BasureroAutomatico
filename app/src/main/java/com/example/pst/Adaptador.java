package com.example.pst;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adaptador extends BaseAdapter {

    Context contexto;
    List<DatosListView> listaObjetos;
    private ArrayList<DatosListView> arraylist;

    public Adaptador(Context contexto, List<DatosListView> listaObjetos) {
        this.contexto = contexto;
        this.listaObjetos = listaObjetos;

        this.arraylist = new ArrayList<DatosListView>();
        this.arraylist.addAll(listaObjetos);
    }

    @Override
    public int getCount() {
        try {
            return listaObjetos.size(); //Retorna cantidad de elementos de lista
        }catch (NullPointerException n){
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return listaObjetos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(listaObjetos.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista= view;
        LayoutInflater inflate= LayoutInflater.from(contexto);
        vista=inflate.inflate(R.layout.item_list_view,null);
        TextView titulo= (TextView)vista.findViewById(R.id.tvTitulo);
        TextView sub1= (TextView)vista.findViewById(R.id.tvSub1);
        TextView sub2= (TextView)vista.findViewById(R.id.tvSub2);
        TextView sub3= (TextView)vista.findViewById(R.id.tvSub3);
        TextView sub4= (TextView)vista.findViewById(R.id.tvSub4);

        titulo.setText(listaObjetos.get(i).getTitulo());
        sub1.setText(listaObjetos.get(i).getSub1());
        sub2.setText(listaObjetos.get(i).getSub2());
        sub3.setText(listaObjetos.get(i).getSub3());
        sub4.setText(listaObjetos.get(i).getSub4());
        return vista;
    }


}
