package com.webianks.hatkemessenger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class Ormlite extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Allsms";
    private static final int DATABASE_VERSION = 1;
    private Dao<Smspp, Integer> tableSmspp;
    private Dao<Smspg, Integer> tableSmspg;
    private Dao<Groupe, Integer> tableGroupe;
    private Dao<Contact, Integer>tableContact;
    private Dao<Diffusion, Integer> tableDiffusion;
    public Ormlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,Smspp.class);
            Log.e("Oncreat DB","creat table Smspp success");
            TableUtils.createTable(connectionSource,Groupe.class);
            Log.e("Oncreat DB","creat table Groupe success");
            TableUtils.createTable(connectionSource,Contact.class);
            Log.e("Oncreat DB","creat table Contact success");
            TableUtils.createTable(connectionSource,Diffusion.class);
            Log.e("Oncreat DB","creat table Diffusion success");
            TableUtils.createTable(connectionSource,Smspg.class);
            Log.e("Oncreat DB","creat table Smspg success");
        } catch (SQLException e) {
            Log.e("Oncreat DB","erreur>>>"+e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public boolean insertSms(Smspp smspp){
        try {
            tableSmspp = getDao(Smspp.class);
            tableSmspp.create(smspp);
            return true;
        } catch (SQLException e) {
            Log.e("insert faild",e.getMessage());
        }
        return false;
    }
    public List<Smspp> getSmsppByReceiverNumber(String receiverNumber){
        try {
            tableSmspp = getDao(Smspp.class);
            return tableSmspp.queryForEq("receiverNumber",receiverNumber);
        } catch (SQLException e) {
            Log.e("getting Smspp faild",e.getMessage());
        }
        return null;
    }

    public boolean insertSmsGroupe(Smspg smspg){
        try {
            tableSmspg = getDao(Smspg.class);
            tableSmspg.create(smspg);
            return true;
        } catch (SQLException e) {
            Log.e("insert faild",e.getMessage());
        }
        return false;
    }

    public boolean deleteGroupe(int idGroupe){
        try {
            tableGroupe = getDao(Groupe.class);
            tableGroupe.deleteById(idGroupe);
            Log.e("delete","ok"+idGroupe);
            return true;
        } catch (SQLException e) {
            Log.e("delete faild",e.getMessage());
        }
        return false;
    }

    public List<Smspg> getSmspgByIdgroupe(int idGroupe){
        try {
            tableSmspg = getDao(Smspg.class);
            return tableSmspg.queryForEq("idGroupe",idGroupe);
        } catch (SQLException e) {
            Log.e("getting Smspg faild",e.getMessage());
        }
        return null;
    }
    public boolean insertContact(Contact contact){
        try {
            tableContact = getDao(Contact.class);
            tableContact.create(contact);
            return true;
        } catch (SQLException e) {
            Log.e("insert Contact faild", e.getMessage());
        }
        return false;
    }
    public Contact getContactById(int id){
        try {
            tableContact = getDao(Contact.class);
            return tableContact.queryForEq("id",id).get(0);
        } catch (SQLException e) {
            Log.e("getting Contact faild",e.getMessage());
        }
        return null;
    }
    public Integer getIdContact(Contact contact){
        try {
            tableContact = getDao(Contact.class);
            return tableContact.extractId(contact);
        } catch (SQLException e) {
            Log.e("getting Contact id",e.getMessage());
        }
        return null;
    }
    public boolean isContactExist(Contact contact){
        try {
            tableContact = getDao(Contact.class);
            return tableContact.idExists(this.getIdContact(contact));
        } catch (SQLException e) {
            Log.e("is ID Contact exist",e.getMessage());
        }
        return false;
    }

    public List<Contact> getContact(){
        try {
            tableContact = getDao(Contact.class);
            return tableContact.queryForAll();
        } catch (SQLException e) {
            Log.e("getting Contact faild",e.getMessage());
        }
        return null;
    }
    public boolean insertGroupe(Groupe groupe){
        try {
            tableGroupe = getDao(Groupe.class);
            tableGroupe.create(groupe);
            return true;
        } catch (SQLException e) {
            Log.e("insert Groupe faild",e.getMessage());
        }
        return false;
    }
    public List<Groupe> getGroupes(){
        try {
            tableGroupe = getDao(Groupe.class);
            return tableGroupe.queryForAll();
        } catch (SQLException e) {
            Log.e("getting Groupe faild",e.getMessage());
        }
        return null;
    }
    public Integer getIdGroupe(Groupe groupe){
        try {
            tableGroupe = getDao(Groupe.class);
            return tableGroupe.extractId(groupe);
        } catch (SQLException e) {
            Log.e("getting groupe id",e.getMessage());
        }
        return null;
    }
    public int getIdGroupeByname(String name){
        try {
            tableGroupe = getDao(Groupe.class);
            return tableGroupe.queryForEq("nom",name).get(0).getId();
        } catch (SQLException e) {
            Log.e("getting Groupe ID faild",e.getMessage());
        }
        return 0;
    }
    public boolean isGroupeExist(String name){
        try {
            tableGroupe = getDao(Groupe.class);
            return tableGroupe.queryForEq("nom",name).size()==0?false:true;
        } catch (SQLException e) {
            Log.e("is ID groupe exist",e.getMessage());
        }
        return false;
    }
    public boolean insertDiffusion(Diffusion diffusion){
        try {
            tableDiffusion = getDao(Diffusion.class);
            tableDiffusion.create(diffusion);
            return true;
        } catch (SQLException e) {
            Log.e("insert diffusion faild",e.getMessage());
        }
        return false;
    }
    public List<Diffusion> getDiffusion(){
        try {
            tableDiffusion = getDao(Diffusion.class);
            return tableDiffusion.queryForAll();
        } catch (SQLException e) {
            Log.e("getting diffusion faild",e.getMessage());
        }
        return null;
    }
    public List<Diffusion> getDiffusionByGroupeId(int groupeId){
        try {
            tableDiffusion = getDao(Diffusion.class);
            return tableDiffusion.queryForEq("idGroupe",groupeId);
        } catch (SQLException e) {
            Log.e("getting Diffusion faild",e.getMessage());
        }
        return null;
    }
}
