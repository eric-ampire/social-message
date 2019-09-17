package com.webianks.hatkemessenger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Smspg {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private String sms;
  @DatabaseField
  private boolean status;
  @DatabaseField
  private int idGroupe;

  public Smspg(String sms, boolean status, int idGroupe) {
    this.sms = sms;
    this.status = status;
    this.idGroupe = idGroupe;
  }

  public Smspg() {
  }

  @Override
  public String toString() {
    return "Smspg{" +
            "id=" + id +
            ", sms='" + sms + '\'' +
            ", status=" + status +
            ", idGroupe=" + idGroupe +
            '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSms() {
    return sms;
  }

  public void setSms(String sms) {
    this.sms = sms;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public int getIdGroupe() {
    return idGroupe;
  }

  public void setIdGroupe(int idGroupe) {
    this.idGroupe = idGroupe;
  }
}
