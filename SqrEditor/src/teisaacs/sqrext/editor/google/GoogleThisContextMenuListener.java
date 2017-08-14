package teisaacs.sqrext.editor.google;


import javax.swing.JMenuItem;

import oracle.ide.Context;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.IdeAction;

public final class GoogleThisContextMenuListener implements ContextMenuListener {
  private JMenuItem menuItem;
  private int nCmdID;

  public void menuWillShow(ContextMenu contextMenu) {
    //contextMenu.add(contextMenu.createMenuItem(IdeAction.find(GoogleThisCommand.actionId())));
    IdeAction action = IdeAction.get(GoogleThisCommand.actionId(),"teisaacs.sqrext.editor.google.GoogleThisCommand", "google Selected Text");
    action.addController(new GoogleThisController());
     contextMenu.add(contextMenu.createMenuItem(action));
  }

  public void menuWillHide(ContextMenu contextMenu) {
  }

  public boolean handleDefaultAction(Context context) {
    return false;
  }
}
