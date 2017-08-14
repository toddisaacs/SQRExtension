package teisaacs.sqrext.model;

import java.util.Iterator;

import javax.swing.Icon;

import javax.swing.ImageIcon;

import oracle.ide.model.Attributes;
import oracle.ide.model.Element;

import teisaacs.sqrext.SqrEditorAddin;

public class SqrHeadElement implements SqrElement {

    private static ImageIcon icon;
    private String Name;
    private int startLine;
    private String type;
    private boolean local;
    private String toolText;
    public SqrHeadElement() {

    }

    static {
        String path = "/teisaacs/sqrext/resources/images/headProc.JPG";
        java.net.URL imgURL = SqrElement.class.getResource(path);
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        } else {
            //System.err.println("Couldn't find file: " + path);
            oracle.ide.log.LogManager.getLogManager().showLog();
            oracle.ide.log.LogManager.getLogManager().getMsgPage().log("Couldn't find file: " + 
                                                                       path + 
                                                                       "\n");
        }

    }

    public boolean mayHaveChildren() {
        return false;
    }

    public Iterator getChildren() {
        return null;
    }

    public Attributes getAttributes() {
        return null;
    }

    public Object getData() {
        return this;
    }

    public String getShortLabel() {
        return "SQR Heading";
    }

    public String getLongLabel() {
        return "SQR Heading";
    }

    public Icon getIcon() {
        return icon;
    }



    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getToolTipText() {
        return toolText;
    }

    public void setToolTipText(String text) {
        toolText = text;
    }
}
