package teisaacs.sqrext.editor.blockComment;

import javax.swing.text.BadLocationException;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Command;
import oracle.ide.log.LogManager;

import oracle.javatools.buffer.LineMap;
import oracle.javatools.editor.BasicDocument;
import oracle.javatools.editor.BasicEditorPane;

import oracle.javatools.editor.EditDescriptor;

import teisaacs.sqrext.SqrEditorAddin;
import teisaacs.sqrext.editor.SqrEditor;

public class BlockUnCommentCommand extends Command {

  private EditDescriptor commentEditDescriptor = 
    new EditDescriptor("teisaacs.sqrext.editor.blockComment_UNCOMMENT");
    
  public BlockUnCommentCommand() {
    super(actionId());
  }

  /**
   * Command Processor callback used to execute this command.
   * 
   * @return
   */
  public int doit() {
    removeComment();
    return OK;
  }

  /**
   * Returns the id of the action this command is associated with.
   * 
   * @return the id of the action this command is associated with.
   * @throws IllegalStateException if the action this command is associated
   * with is not registered.
   */
  public static int actionId() {
    final Integer cmdId = 
      Ide.findOrCreateCmdID("teisaacs.sqrext.editor.blockComment.BlockUnCommentCommand");
    if (cmdId == null)
      throw new IllegalStateException("Action teisaacs.sqrext.editor.blockComment.BlockUnCommentCommand not found.");
    return cmdId;
  }
  
  private boolean removeComment() {
    boolean ret = false;
    Context context = this.context;
    BasicEditorPane editor = SqrEditorAddin.getCurrentEditorPane(context);
    final BasicDocument document = (BasicDocument)editor.getDocument();

    if (editor != null) {
      try {
        String selectionText = editor.getSelectedText();
        if (selectionText.trim().length() > 0) {
          //insert comment character at start of line
          editor.beginEdit(commentEditDescriptor);
          try {
            //for each line insert comment
            int ss = editor.getSelectionStart();
            int se = editor.getSelectionEnd();

            LineMap lineMap = document.getTextBuffer().getLineMap();
            int startLineNum = lineMap.getLineFromOffset(ss);
            int startOfLineOffset = editor.getLineStartOffset(startLineNum);
            
            int lineStart = startOfLineOffset;

            while (lineStart < se) {
              //start with comment
             
              int lineNumber = lineMap.getLineFromOffset(lineStart);

              String linetext = 
                document.getText(lineStart, editor.getLineEndOffset(lineNumber) - 
                                 lineStart);
              if (document.getTextBuffer().getChar(lineStart) == '!') {
                document.remove(lineStart, 1);
              }
              //set next line start
              lineStart = editor.getLineEndOffset(lineNumber);
              //LogManager.getLogManager().getMsgPage().log("lineStart " + lineStart + " - line number "  + lineNumber + "\n");
            }


          } catch (BadLocationException e) {
            System.err.println("Exception occurred in completion: " + 
                               e); //NORES
            e.printStackTrace();
          } finally {
            editor.endEdit();
          }

          ret = true;
        } else
          ret = false;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return ret;
  }
}
