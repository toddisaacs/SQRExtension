package teisaacs.sqrext.editor.insight;

import java.awt.MediaTracker;

import javax.swing.Icon;

import javax.swing.ImageIcon;

import oracle.javatools.editor.insight.ListDataItem;

import teisaacs.sqrext.SqrEditorAddin;

/**
 * Inner class from Simple code insight implementation example from Keimpe
 *
 * @author Keimpe Bronkhorst
 */
  public class SqrListDataItem extends ListDataItem {
    private String str;
    public static final Icon SQR_FILE_ICON;
    
    static 
    {
      ImageIcon i = new ImageIcon(
        SqrEditorAddin.class.getResource( "batman.gif" )
      );
      if ( i.getImageLoadStatus() == MediaTracker.COMPLETE )
      {
        SQR_FILE_ICON = i;
      }
      else
      {
        SQR_FILE_ICON = null;
      }
    }

    public SqrListDataItem(String str) {
      this.str = str;
    }

    public Icon getIcon() {
      //return SQR_FILE_ICON;
      return null;
    }

    public String getDisplayText() {
      // This is used for the display in the popup list
      return str;
    }

    public String getName() {
      // This is inserted in the editor buffer for the completion, although
      // you have control over this in the 'complete' method
      return str;
    }
  }