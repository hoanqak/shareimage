package com.tight.coupling.loose.coupling;

public class ExportExcel implements IExport
{
    @Override
    public void processExport(Object data)
    {
        System.out.println("Export Excel: " + data);
    }
}
