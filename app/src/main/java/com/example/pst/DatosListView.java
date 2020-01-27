package com.example.pst;

import java.io.Serializable;

public class DatosListView implements Serializable {
    private String Id;
    private String Titulo;
    private String sub1;
    private String sub2;
    private String sub3;
    private String sub4;



    public DatosListView(String id, String titulo, String sub1, String sub2, String sub3, String sub4) {
        Id=id;
        Titulo=titulo;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
        this.sub4 = sub4;
    }



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }


    public String getSub1() {
        return sub1;
    }

    public void setSub1(String sub1) {
        this.sub1 = sub1;
    }

    public String getSub2() {
        return sub2;
    }

    public void setSub2(String sub2) {
        this.sub2 = sub2;
    }

    public String getSub3() {
        return sub3;
    }

    public void setSub3(String sub3) {
        this.sub3 = sub3;
    }

    public String getSub4() {
        return sub4;
    }

    public void setSub4(String sub4) {
        this.sub4 = sub4;
    }
}
