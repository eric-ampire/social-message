package com.webianks.hatkemessenger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Comparator;

@DatabaseTable
public class Contact implements Comparable<Contact> {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private String nom;
  @DatabaseField
  private String number;
  @DatabaseField
  private String email;
  @DatabaseField
  private boolean par_sms;
  @DatabaseField
  private boolean par_mail;
  @DatabaseField
  private int idGroupe;

  public Contact() {
  }

  public Contact(String nom, String number, int idGroupe) {
    this.nom = nom;
    this.number = number;
    this.idGroupe = idGroupe;
  }


  public Contact(String nom, String number, String email, int idGroupe) {
    this.nom = nom;
    this.number = number;
    this.email = email;
    this.idGroupe = idGroupe;
  }

    public Contact(int id, String nom, String number, String email, boolean par_sms, boolean par_mail, int idGroupe) {
        this.id = id;
        this.nom = nom;
        this.number = number;
        this.email = email;
        this.par_sms = par_sms;
        this.par_mail = par_mail;
        this.idGroupe = idGroupe;
    }

    @Override
  public String toString() {
    return "Contact{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", number='" + number + '\'' +
            ", email='" + email + '\'' +
            ", idGroupe=" + idGroupe +
            '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public int getIdGroupe() {
    return idGroupe;
  }

  public void setIdGroupe(int idGroupe) {
    this.idGroupe = idGroupe;
  }

    public boolean isPar_sms() {
        return par_sms;
    }

    public void setPar_sms(boolean par_sms) {
        this.par_sms = par_sms;
    }

    public boolean isPar_mail() {
        return par_mail;
    }

    public void setPar_mail(boolean par_mail) {
        this.par_mail = par_mail;
    }

    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int compareTo(Contact o) {
    return this.getNom().compareTo(o.getNom());
  }
}
