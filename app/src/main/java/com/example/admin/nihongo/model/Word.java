package com.example.admin.nihongo.model;


/**
 * Created by Admin on 2017/8/3.
 */

public class Word {
    private int id;
    private String japanese;
    private String kanJi;
    private String nominal;
    private String chinese;

    public Word() {
    }

    public Word(int id, String japanese, String kanJi, String nominal, String chinese) {
        this.id = id;
        this.japanese = japanese;
        this.kanJi = kanJi;
        this.nominal = nominal;
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public String getJapanese() {
        return japanese;
    }

    public String getKanJi() {
        return kanJi;
    }

    public String getNominal() {
        return nominal;
    }

    public String getChinese() {
        return chinese;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setJapanese(String japanese) {

        this.japanese = japanese;
    }

    public void setKanJi(String kanJi) {
        this.kanJi = kanJi;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
