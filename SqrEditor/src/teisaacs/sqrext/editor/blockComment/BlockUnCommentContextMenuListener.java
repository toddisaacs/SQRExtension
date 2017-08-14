package teisaacs.sqrext.editor.blockComment;

import oracle.ide.Context;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.IdeAction;

import teisaacs.sqrext.editor.blockComment.BlockUnCommentCommand;
import teisaacs.sqrext.editor.google.GoogleThisCommand;

public class BlockUnCommentContextMenuListener implements ContextMenuListener {

  public void menuWillShow(ContextMenu contextMenu) {
  IdeAction action = IdeAction.get(BlockUnCommentCommand.actionId(),"teisaacs.sqrext.editor.blockComment.BlockUnCommentCommand", "Un-Comment Selected Block");
   action.addController(new BlockCommentController());
    contextMenu.add(contextMenu.createMenuItem(
      IdeAction.find(BlockUnCommentCommand.actionId())
    ));
  }

  public void menuWillHide(ContextMenu contextMenu) {
  }

  public boolean handleDefaultAction(Context context) {
    return false;
  }
}
