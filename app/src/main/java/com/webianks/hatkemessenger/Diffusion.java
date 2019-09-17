package com.webianks.hatkemessenger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Diffusion {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private int idContact;
  @DatabaseField
  private int idGroupe;

  public Diffusion() {
  }

  public Diffusion(int idContact, int idGroupe) {
    this.idContact = idContact;
    this.idGroupe = idGroupe;
  }

  @Override
  public String toString() {
    return "Diffusion{" +
            "id=" + id +
            ", idContact=" + idContact +
            ", idGroupe=" + idGroupe +
            '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIdContact() {
    return idContact;
  }

  public void setIdContact(int idContact) {
    this.idContact = idContact;
  }

  public int getIdGroupe() {
    return idGroupe;
  }

  public void setIdGroupe(int idGroupe) {
    this.idGroupe = idGroupe;
  }
}
