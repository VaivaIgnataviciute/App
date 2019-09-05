package com.example.app;


import android.net.nsd.NsdServiceInfo;

public class PrinterNew {
    String printerName;
    String printerModel;
    NsdServiceInfo printerService;
    int printerState;


    public PrinterNew() {

    }


    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerName = printerModel;
    }

    public int getPrinterState() {
        return this.printerState;
    }

    public void setPrinterState(int printerState) {
        this.printerState = printerState;
    }

    public void setPrinterService(NsdServiceInfo service) {
        this.printerService = service;
    }

    public NsdServiceInfo getPrinterService() {
        return this.printerService;
    }

}


