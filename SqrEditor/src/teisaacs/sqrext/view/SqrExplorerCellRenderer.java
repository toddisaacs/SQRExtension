package teisaacs.sqrext.view;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import teisaacs.sqrext.model.SqrElement;


public class SqrExplorerCellRenderer extends DefaultTreeCellRenderer {
    public SqrExplorerCellRenderer() {
        super();
    }
    
    
    /**
     * Configures the renderer based on the passed in components.
     * sets the Icon and tool tip text with the values from the Element
     * @see oracle.ide.model.Element;
     */
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus)                                            
    {                                                  
         super.getTreeCellRendererComponent(tree,
                                            value,
                                            sel,
                                            expanded,
                                            leaf,
                                            row,
                                            hasFocus);
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) value;
            int depth = tn.getDepth();
        
         setToolTipText("testing this method");
           /* if (depth == 0 || depth == 1) {
                if (tn.getUserObject() instanceof SqrElement) {
                    SqrElement e = (SqrElement) tn.getUserObject();
                    setText(e.getName());
                    
                    setIcon(e.getIcon());
                }
            }*/
            if (tn.getUserObject() instanceof SqrElement) {
                SqrElement e = (SqrElement) tn.getUserObject();
                setText(e.getName());
                
                setIcon(e.getIcon());
            }
                                        
         return this;
     }
   
}
