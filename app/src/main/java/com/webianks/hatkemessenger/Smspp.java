package com.webianks.hatkemessenger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Smspp {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private String sms;
  @DatabaseField
  private boolean status;
  @DatabaseField
  private String senderNumber;
  @DatabaseField
  private String receiverNumber;

  public Smspp(String sms, boolean status, String senderNumber, String receiverNumber) {
    this.sms = sms;
    this.status = status;
    this.senderNumber = senderNumber;
    this.receiverNumber = receiverNumber;
  }
  public Smspp(){}
  @Override
  public String toString() {
    return "Smspp{" +
            "id=" + id +
            ", sms='" + sms + '\'' +
            ", status=" + status +
            ", senderNumber='" + senderNumber + '\'' +
            ", receiverNumber='" + receiverNumber + '\'' +
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

  public String getSenderNumber() {
    return senderNumber;
  }

  public void setSenderNumber(String senderNumber) {
    this.senderNumber = senderNumber;
  }

  public String getReceiverNumber() {
    return receiverNumber;
  }

  public void setReceiverNumber(String receiverNumber) {
    this.receiverNumber = receiverNumber;
  }
}
