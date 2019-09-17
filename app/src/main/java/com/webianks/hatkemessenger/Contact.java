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
  private int idGroupe;

  public Contact() {
  }

  public Contact(String nom, String number, int idGroupe) {
    this.nom = nom;
    this.number = number;
    this.idGroupe = idGroupe;
  }

  @Override
  public String toString() {
    return "Contact{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", number='" + number + '\'' +
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

  @Override
  public int compareTo(Contact o) {
    return this.getNom().compareTo(o.getNom());
  }
}
