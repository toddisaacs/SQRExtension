package teisaacs.sqrext.editor.blockComment;

import oracle.ide.Context;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.editor.Editor;
import oracle.ide.view.View;

import teisaacs.sqrext.editor.SqrNode;

public class BlockCommentController implements Controller {

  /**
   * Controller interface callback. Called if this controller is registered with
   * the menu item or button IdeAction (action.addController).  The controller 
   * can defer the processing of the event to other controllers up the chain or
   * if no controller handles the event the command processor will process the 
   * command.
   * 
   * @param ideAction
   * @param context
   * @return true if this controll will handle the event.  false to allow other 
   * controllers to handle this event.  If no controllers handle this event,
   * then the action will be processed by the command processor and the doit()
   * method will be invoked on the command.
   */
  public boolean handleEvent(IdeAction ideAction, Context context) {
    return false;
  }

  /**
   * Controller interface callback. Called if this controller is registered with
   * the menu item or button via the IdeAction (action.addController). This 
   * gives this controller the oportunity to update the IdeAction enabled status
   * and return true if it was updated to true.  Otherwise return false to
   * delegate to controllers up the chain.
   * 
   * @param ideAction
   * @param context
   * @return true if this controller updated the ation's status or false if this 
   * controller has not updated the status.
   */
  public boolean update(IdeAction ideAction, Context context) {
    ideAction.setEnabled(hasSelectedText(context.getView()));
    return true;
  }

  /**
   * determines if there is selected text and an SqrNode.
   * 
   * @param context
   * @return true if text is selected and we have an SqrNode otherwise false.
   */
  private boolean hasSelectedText(View view) {
    
    if (!(view instanceof Editor)) //|| 
       // !(context.getNode() instanceof SqrNode))
      return false;

    String selectedText = ((CodeEditor)view).getSelectedText();
    return selectedText != null && selectedText.length() != 0;

  }
}
