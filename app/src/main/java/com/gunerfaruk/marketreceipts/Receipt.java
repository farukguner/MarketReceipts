package com.gunerfaruk.marketreceipts;

import java.util.Date;

public class Receipt {
    public String ImageName;
    public String ReceiptDate;
    public boolean Status;

    public Receipt(String imageName, Date receiptDate){
        ImageName = imageName;
        ReceiptDate = receiptDate.toString();
        Status = false;
    }

    public Receipt(){

    }
}
