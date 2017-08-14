package teisaacs.sqrext.editor.blockComment;

import oracle.ide.Context;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.IdeAction;

import teisaacs.sqrext.editor.google.GoogleThisCommand;

public class BlockCommentContextMenuListener implements ContextMenuListener {
  public BlockCommentContextMenuListener() {
  }

  public void menuWillShow(ContextMenu contextMenu) {
  IdeAction action = IdeAction.get(BlockCommentCommand.actionId(),"teisaacs.sqrext.editor.blockComment.BlockCommentCommand", "Comment Selected Block");
   action.addController(new BlockCommentController());
    contextMenu.add(contextMenu.createMenuItem(
      IdeAction.find(BlockCommentCommand.actionId())
    ));
  }

  public void menuWillHide(ContextMenu contextMenu) {
  }

  public boolean handleDefaultAction(Context context) {
    return false;
  }
}
