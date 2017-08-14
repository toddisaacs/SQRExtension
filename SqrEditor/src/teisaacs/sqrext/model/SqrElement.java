package teisaacs.sqrext.model;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.Element;

public interface SqrElement extends Element {
    
    public abstract Icon getIcon();
    public abstract String getName();
    public abstract int getStartLine();
    public abstract boolean isLocal();
}
