import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class FormatInfo implements Transferable {
    private final String htmlContent;
    private final DataFlavor DEF_DF = new DataFlavor("text/html; charset=unicode; class=java.lang.String", "text/html");


    public FormatInfo(String PicPath, String Text){
        htmlContent = String.format("<html><body><img src=\"%s\"><br>%s</body></html>", PicPath, Text);
    }


    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DEF_DF};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DEF_DF.equals(flavor);
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (DEF_DF.equals(flavor)) return htmlContent;
        if (flavor.equals(DataFlavor.stringFlavor)) return "Create by LemonTree";
        throw new UnsupportedFlavorException(flavor);
    }


}
