package com.example.murat.eticaret.Model;

public class Users {

    //yeni b
    private String ad,telefon,sifre,image,adres;

    public Users()
    {

    }

    public Users(String ad,String telefon,String sifre,String image,String adres){
        this.ad=ad;
        this.telefon=telefon;
        this.sifre=sifre;
        this.image=image;
        this.adres=adres;
    }
    public  String getAd(){
        return ad;
    }

    public void setAd(String ad){
        this.ad=ad;
    }
    public String getTelefon(){
        return telefon;
    }
    public void  setTelefon(String telefon){
        this.telefon=telefon;
    }
    public String getSifre(){
        return sifre;
    }
    public void setSifre(String sifre){
        this.sifre=sifre;
    }
    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image=image;
    }
    public String getAdres(){
        return adres;
    }
    public void setAdres(String adres){
        this.adres=adres;
    }
}
