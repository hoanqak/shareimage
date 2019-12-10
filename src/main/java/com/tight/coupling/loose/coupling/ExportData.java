package com.tight.coupling.loose.coupling;

import javax.annotation.PostConstruct;

public class ExportData
{
    private IExport iExport;
    public ExportData(IExport iExport){
        this.iExport = iExport;
    }

    public void exportProcess(Object data){
        this.iExport.processExport(data);
    }

    @PostConstruct
    private void constructor(){
        System.out.println("Start");
    }

    public static void main(String[] args)
    {
        IExport iExport = new ExportExcel();
        IExport iExportPDF = new ExportPDF();
        ExportData exportData = new ExportData(iExport);
        exportData.exportProcess("export");
        new ExportData(iExportPDF).exportProcess("export");
    }
}
