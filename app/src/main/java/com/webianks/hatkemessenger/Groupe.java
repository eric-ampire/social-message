package com.webianks.hatkemessenger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Groupe {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private String nom;
  @DatabaseField
  private int nbContact;

  public Groupe() {
  }

  public Groupe(String nom, int nbContact) {
    this.nom = nom;
    this.nbContact = nbContact;
  }

  @Override
  public String toString() {
    return "Groupe{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", nbContact=" + nbContact +
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

  public int getNbContact() {
    return nbContact;
  }

  public void setNbContact(int nbContact) {
    this.nbContact = nbContact;
  }
}
