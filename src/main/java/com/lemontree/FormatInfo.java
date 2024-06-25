package com.lemontree;

import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class FormatInfo implements Transferable {
    private final String htmlContent;
    private final DataFlavor DEF_DF = new DataFlavor("text/html; charset=unicode; class=java.lang.String", "text/html");
    private final String text;


    public FormatInfo(String PicPath, String Text){
        this.text = Text;
        htmlContent = String.format("<html><body><img src=\"%s\"><br>%s</body></html>", PicPath, Text);
    }


    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DEF_DF, DataFlavor.stringFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DEF_DF.equals(flavor) || flavor.equals(DataFlavor.stringFlavor);
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (DEF_DF.equals(flavor)) return htmlContent;
        if (flavor.equals(DataFlavor.stringFlavor)) return text.replace("<br>", "\n");
        throw new UnsupportedFlavorException(flavor);
    }
}
