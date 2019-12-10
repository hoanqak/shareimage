package com.tight.coupling.loose.coupling;

public class ExportPDF implements IExport
{

    @Override
    public void processExport(Object data)
    {
        System.out.println("Export PDF: " + data);
    }
}
