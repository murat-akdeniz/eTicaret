package com.example.murat.eticaret.Model;

public class Urunler {
    private String urunAdi,urunTanimi,urunFiyati,image,kategori,urunID,date,time;

    public Urunler()
    {

    }

    public Urunler(String urunAdi, String urunTanimi, String urunFiyati, String image, String kategori, String urunID, String date, String time) {
        this.urunAdi = urunAdi;
        this.urunTanimi = urunTanimi;
        this.urunFiyati = urunFiyati;
        this.image = image;
        this.kategori = kategori;
        this.urunID = urunID;
        this.date = date;
        this.time = time;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getUrunTanimi() {
        return urunTanimi;
    }

    public void setUrunTanimi(String urunTanimi) {
        this.urunTanimi = urunTanimi;
    }

    public String getUrunFiyati() {
        return urunFiyati;
    }

    public void setUrunFiyati(String urunFiyati) {
        this.urunFiyati = urunFiyati;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getUrunID() {
        return urunID;
    }

    public void setUrunID(String urunID) {
        this.urunID = urunID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
