package com.example.langsettingtest;

public class SnackFood {
    public String id;  // 사용자 id
    public String food; // 사용자명
    public String ingredient; // 계정명
    public String flavor; // 개인키
    public int spicy; // 비밀키
    public byte[] image;  // 이미지

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setFood(String food){
        this.food = food;
    }

    public String getFood(){
        return food;
    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    public String getIngredient(){
        return ingredient;
    }

    public void setFlavor(String flavor){
        this.flavor = flavor;
    }

    public String getFlavor(){
        return flavor;
    }

    public void setSpicy(int spicy){
        this.spicy = spicy;
    }

    public int getSpicy(){
        return spicy;
    }

    public void setImage(byte[] image){
        this.image = image;
    }

    public byte[] getImage(){
        return image;
    }
}
