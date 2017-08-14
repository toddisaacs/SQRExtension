package teisaacs.sqrext.editor.openInclude;

import oracle.ide.Ide;
import oracle.ide.controller.Command;

public class OpenIncludeCommand extends Command {
  public OpenIncludeCommand() {
    super(actionId());
  }

  
  public static int actionId() {
      final Integer cmdId = Ide.findOrCreateCmdID("teisaacs.sqrext.editor.openInclude.OpenIncludeCommand");
      if (cmdId == null)
          throw new IllegalStateException("teisaacs.sqrext.editor.openInclude.OpenIncludeCommand not found.");
      return cmdId;
  }
  
  //****************************************************************************
  // Command implementation
  //****************************************************************************

  /**
   * Command Processor callback used to execute this command.
   * @return status 
   */
  public int doit() {
    return 0;
  }
  
  
}
