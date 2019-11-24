package com.example.usandorecyclerview;

public class ItemBT {

    private String Name;
    private String MAC;
    private boolean isVinculado;

    public ItemBT(String name, String MAC, boolean isVinculado) {
        Name = name;
        this.MAC = MAC;
        this.isVinculado = isVinculado;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public boolean isVinculado() {
        return isVinculado;
    }

    public void setVinculado(boolean vinculado) {
        isVinculado = vinculado;
    }
}
