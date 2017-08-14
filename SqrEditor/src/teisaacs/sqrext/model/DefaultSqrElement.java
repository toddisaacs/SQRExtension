package teisaacs.sqrext.model;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.Attributes;

public class DefaultSqrElement  {

    private Vector children = new Vector();
    protected static ImageIcon icon;
    private String Name;
    private int startLine;
    private String type;
    private boolean local;


    public Attributes getAttributes() {
        return null;
    }

    public Object getData() {
        return this;
    }

    public String getShortLabel() {
        return "Default Procedure";
    }

    public String getLongLabel() {
        return "Default Procedure ";
    }

    public Icon getIcon() {
        return icon;
    }

    public String getToolTipText() {
        return "tool tip test";
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
    
    public void addChild(SqrElement e) {
        children.add(e);
    }

    public Iterator getChildren() {
        return children.iterator();
    }

    public boolean mayHaveChildren() {
        return !(children.isEmpty());
    }
}
