package com.example.proyectoeatq.ControlListaCompra;

public class LCItem {

    public String texto;
    public boolean checked;

    public LCItem(String texto, boolean comprado){
        this.texto = texto;
        this.checked = comprado;
    }

    @Override
    public String toString(){
        return texto + ":" + checked;
    }

    public static LCItem fromString(String s){
        String [] parts = s.split(":");
        if(parts.length < 2){
            return new LCItem(s.trim(), false);
        }
        return new LCItem(parts[0].trim(), Boolean.parseBoolean(parts[1].trim()));
    }
}
