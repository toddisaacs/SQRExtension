package teisaacs.sqrext;

import java.awt.MediaTracker;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;

import oracle.ide.AddinManager;
import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.IdeMainWindow;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.ceditor.CodeEditorAddin;
import oracle.ide.ceditor.EditorPluginsFactory;
import oracle.ide.ceditor.find.FindableEditor;
import oracle.ide.config.ClientSetting;
import oracle.ide.config.IdeSettings;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.controller.MenuManager;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorAddin;
import oracle.ide.editor.EditorManager;
import oracle.ide.explorer.ExplorerManager;
import oracle.ide.log.LogManager;
import oracle.ide.model.DeployableTextNode;
import oracle.ide.model.Element;
import oracle.ide.model.Node;
import oracle.ide.model.Recognizer;
import oracle.ide.panels.Navigable;
import oracle.ide.util.MenuSpec;
import oracle.ide.view.View;

import oracle.javatools.buffer.LineMap;
import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.BasicDocument;
import oracle.javatools.editor.BasicEditorPane;
import oracle.javatools.editor.EditDescriptor;

import teisaacs.sqrext.editor.SqrLanguageModule;
import teisaacs.sqrext.editor.SqrNode;
import teisaacs.sqrext.editor.blockComment.BlockCommentContextMenuListener;
import teisaacs.sqrext.editor.blockComment.BlockUnCommentContextMenuListener;
import teisaacs.sqrext.editor.google.GoogleThisCommand;
import teisaacs.sqrext.editor.google.GoogleThisContextMenuListener;
import teisaacs.sqrext.editor.google.GoogleThisController;
import teisaacs.sqrext.editor.insight.SqrInsightProvider;
import teisaacs.sqrext.model.SqrElement;
import teisaacs.sqrext.model.SqrEnvData;
import teisaacs.sqrext.view.SqrEnvDataPanel;
import teisaacs.sqrext.view.SqrExplorer;
import oracle.ide.AddinManager;

/*
 * SQR Editor Addin is the main extension class.  Extends A
 *
 * TODO Check to see if it is better to override subclassof EditorAddin or
 * CodeEditorAddin, advantages, etc.. ??
 */
public class SqrEditorAddin extends EditorAddin implements Controller, 
                                                           EditorPluginsFactory {
  public static final String SQR_EXTENSION = "sqr";
  public static final String SQR_EXTENSION2 = "sqc";
  public static final Icon SQR_FILE_ICON;

  private SqrNode sqrNode;
  
  

  // Extension ID
  public static final String EXTENSION_ID = "teisaacs.sqrext.SqrAddin";
  public static final int SQR_ENV_ADDIN_CMD_ID = 
    Ide.findOrCreateCmdID("teisaacs.sqrext.SQR_ENV_ADDIN_CMD_ID");
  public static final int RCLICK_GOTO_SOURCE_CMD_ID = 
    Ide.findOrCreateCmdID("teisaacs.sqrext.RCLICK_GOTO_SOURCE_CMD_ID");

  private SqrInsightProvider sqrInsight = new SqrInsightProvider();
  
  private String goToSourceProc = "";
  
  static String[] includedProcedures;

  static {
    ImageIcon i = 
      new ImageIcon(SqrEditorAddin.class.getResource("batman.gif"));
    if (i.getImageLoadStatus() == MediaTracker.COMPLETE) {
      SQR_FILE_ICON = i;
    } else {
      SQR_FILE_ICON = null;
    }
    
    //parse directory and build stored procedure array
     SqrEnvData opts = (SqrEnvData) Ide.getSettings().getData(SqrEnvData.KEY);
     
     String dir1 = opts.getDir1();
     
     if (dir1 != null) {
       FileInputStream fio;
      try {
        File dir = new File(dir1);

        FilenameFilter sqcFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".sqc");
                }
            };
            
        String[] children = dir.list(sqcFilter);
        
        for (int j=0; j < children.length; j++) {
          //parse file here
        }
        
        fio = new FileInputStream(new File(dir1));
        int c;
        while (fio.read() != -1) {
          
        }
      } catch (FileNotFoundException e) {
        // TODO
      } catch (IOException e) {
        // TODO
      }
    }
    
  }
  
  
  

  //****************************************************************************
  // EditorAddin
  //****************************************************************************
  /**
   * Gets the fully qualified class name of the Editor being registered. 
   * @return this class
   */
  public Class getEditorClass() {
    return teisaacs.sqrext.SqrEditorAddin.class;
  }

  /**
   * Gets the menu specification of this Addin. This specification may be used 
   * to add a menu item to the main menu bar and/or to any context menu popped 
   * up in a NavigatorWindow. 
   * 
   * @return MenuSpec
   */
  public MenuSpec getMenuSpecification() {
    return null;
  }
  
  /**
   * Description copied from interface: EditorAddin which was copied from Addin
   * 
   * Invoked by the AddinManager after the instance of the Addin is instantiated. 
   * When invoked, The Addin should register and menu items, and actions 
   * required for use during this classes lifecycle. Addin authors should take 
   * care to ensure that any extraneous initialization is not preformed on this 
   * method, and any startup code that can be delayed until a later time is 
   * delayed, as the Addin's are synchronously initialized during the startup 
   * of the IDE, and each Addin has the potential to negatively impact the 
   * startup time of the product. 
   * 
   * @return void
   */
   public void initialize() {

     new SqrLanguageModule();

     Recognizer.mapExtensionToClass(SQR_EXTENSION, SqrNode.class);
     Recognizer.mapExtensionToClass(SQR_EXTENSION2, SqrNode.class);

    //Structure View 
     final ExplorerManager explorerMgr = ExplorerManager.getExplorerManager();
     if (explorerMgr != null) {
       explorerMgr.register(SqrNode.class, SqrExplorer.class, null);
     }

    //create and register settings
     ClientSetting clientSetting = ClientSetting.findOrCreate(EXTENSION_ID);
     if (clientSetting.getData(SqrEnvData.KEY) == null) {
       clientSetting.putData(SqrEnvData.KEY, new SqrEnvData());
     }
     Navigable sqrIdeSettingsPanel = 
       new Navigable(SqrEnvDataPanel.TITLE, SqrEnvDataPanel.class);
     IdeSettings.registerUI(sqrIdeSettingsPanel);

     // Insert Simple MenuItem in the Tools Menu
     JMenu mainMenu = MenuManager.getJMenu(IdeMainWindow.MENU_TOOLS);
     mainMenu.add(createSqrEnvMenuItem());

     //Go To Source menu
     JMenuItem rclickMi1 = 
       createGoToSourceMenuItem(this, RCLICK_GOTO_SOURCE_CMD_ID, "Go to Source", 
                                new Integer('s'), null);
     ctxMenuListener ctxMenuLstnr1 = 
       new ctxMenuListener(rclickMi1, RCLICK_GOTO_SOURCE_CMD_ID);
     EditorManager.getEditorManager().getContextMenu().addContextMenuListener(ctxMenuLstnr1);

     //R-click comment
     EditorManager.getEditorManager().getContextMenu().addContextMenuListener(new BlockCommentContextMenuListener());
     
     //R-click uncomment
     EditorManager.getEditorManager().getContextMenu().addContextMenuListener(new BlockUnCommentContextMenuListener());
     
     //Example Google this command from ESDK
     EditorManager.getEditorManager().getContextMenu().addContextMenuListener(new GoogleThisContextMenuListener());

  }

  private JMenuItem createSqrEnvMenuItem() {
    final String label = "SQR Command";
    final String data = null;
    final char mnemonic = 'q';
    Integer mnemonicInt = new Integer((int)mnemonic);
    Icon menuIcon = null;

    IdeAction action = 
      IdeAction.get(SqrEditorAddin.SQR_ENV_ADDIN_CMD_ID, null, label, null, 
                    mnemonicInt, menuIcon, data, true);
    action.putValue(IdeAction.STATE, Boolean.TRUE);
    action.setState(true);
    action.addController(new configMenuController());
    // This is done by the Create method above!
    // action.putValue(IdeAction.USER_DATA, data);
    MenuManager menuMgr = Ide.getMenubar();
    JMenuItem mi = menuMgr.createMenuItem(action);

    return mi;
  }

  private static JMenuItem createGoToSourceMenuItem(Controller ctrlr, 
                                                    int cmdID, 
                                                    String menuLabel, 
                                                    Integer mnemonic, 
                                                    String iconName) {
    Icon mi = 
      ((iconName == null) ? null : new ImageIcon(ctrlr.getClass().getResource(iconName)));
    IdeAction actionTM = // int cmdId,
      //    String cmdClass,
      //    String name,
      //    KeyStroke accelerator,
      //    Integer mnemonic,
      //    Icon icon ,
      //    Object data,
      //    boolean enabled
      IdeAction.get(cmdID, null, menuLabel, null, mnemonic, mi, null, true);
    actionTM.addController(ctrlr);
    JMenuItem menuItem = Ide.getMenubar().createMenuItem(actionTM);
    //Ide.getToolbar().
    return menuItem;
  }


  private static final void logMessage(String msg) {
    oracle.ide.log.LogManager.getLogManager().showLog();
    oracle.ide.log.LogManager.getLogManager().getMsgPage().log(msg + "\n");
  }


  /**
   * convience method for commands to get the editor pane.
   * 
   * @param context
   * @return current editor pane for the active document.
   */
  public static BasicEditorPane getCurrentEditorPane(Context context) {

    BasicEditorPane editorPane = null;
    if (context != null) {
      View view = context.getView();
      FindableEditor findableEditor = null;
      if (view instanceof FindableEditor) {
        // If the currens at view in editor itself, then check
        // if it supports the Find package.
        findableEditor = (FindableEditor)view;
      }
      // If the current view supports the find interface, get the
      // editor pane that has focus.
      if (findableEditor != null) {
        editorPane = findableEditor.getFocusedEditorPane();
      }
    }
    return editorPane;
  }


  private boolean shouldMenuBeEnabled(Context context) {
    BasicEditorPane editor = getCurrentEditorPane(context);

    boolean bEnable = (editor.getSelectionStart() < editor.getSelectionEnd());
    //verify we are on a procedure.  There should be a 'do' to the left of this//
    Element element = context.getElement();
    SqrNode node = (SqrNode)element;
    TextBuffer tb = node.acquireTextBuffer();
    LineMap lMap = tb.getLineMap();

    int line = lMap.getLineFromOffset(editor.getSelectionStart());
    int start = editor.getLineStartOffset(line);
    int end = editor.getLineEndOffset(line);

    goToSourceProc = "";


    try {
      String thisLine = editor.getText(start, end - start);
      //logMessage(thisLine);
      int doidx = thisLine.toLowerCase().indexOf("do ");
      boolean atStart = false;
      boolean hasProcedure = false;

      if (doidx != -1) {
        //make sure this is at beginning and nothing in between
        int i = start;
        atStart = true;
        while (i < doidx + start) {
          if (tb.getChar(i) != ' ' && tb.getChar(i) != '!') {
            //logMessage("'do' not at the beginning: " + tb.getChar(i));
            atStart = false;
            break;
          } else {
            //logMessage("Found '" + tb.getChar(i) + "'");
          }
          i++;
        }
        //logMessage("'do' at the beginning?: " + atStart + ", doidx: " + doidx);


        //continue...
        //make sure there is something after the '...do '
        String remainder = thisLine.substring(doidx + 2).trim();
        //logMessage("remainder (after 'do'): " + remainder  );
        if (remainder.length() > 0) {
          //potential procedure found - parse procedure name and validate 
          hasProcedure = true;
          int idx_space = remainder.indexOf(" ");
          int idx_paren = remainder.indexOf("(");
          int indx = 0;
          StringBuffer sb = new StringBuffer("");

          while (indx < remainder.length()) {
            //logMessage("try " + indx + ",  length " + remainder.length());
            char c = remainder.charAt(indx);
            if (c == ' ' || c == '(') {
              break;
            } else {
              //logMessage("Character \t'" + c + "'   " + indx);
              sb.append(c);
            }

            ++indx;
          }
          //logMessage("GO TO: " + sb);
          goToSourceProc = sb.toString();

        } else {
          //no procedure name
          hasProcedure = false;
          //logMessage("No Procedure");
        }
      }

      if (atStart && hasProcedure) {
        return true;
      } else {
        return false;
      }

    } catch (BadLocationException e) {
      // TODO
      logMessage("BadlocationException " + e);
      return false;
    }
  }



  //****************************************************************************
  // Controller implementation
  //****************************************************************************
  public boolean handleEvent(IdeAction ideAction, Context context) {
    boolean bHandled = false;
    int cmdId = ideAction.getCommandId();

    if (cmdId == RCLICK_GOTO_SOURCE_CMD_ID) {
      if (!goToSourceProc.equals("")) {
        //logMessage("Go To: " + goToSourceProc);
        Node node = context.getNode();
        if (node instanceof SqrNode) {
          //logMessage("SqrNode Found: ");
          SqrNode sqrNode = (SqrNode)node;
          Vector structureModel = sqrNode.getElements();

          Enumeration e = structureModel.elements();

          while (e.hasMoreElements()) {
            SqrElement item = (SqrElement)e.nextElement();
            //logMessage("Item : " + item.getName());
            if (item.getName().toLowerCase().equals(goToSourceProc.toLowerCase())) {
              //logMessage("Item found: " + item.getName());
              EditorManager em = EditorManager.getEditorManager();
              Editor ed = em.getCurrentEditor();

              if (ed instanceof CodeEditor) {
                CodeEditor cEd = (CodeEditor)ed;
                cEd.gotoLine(item.getStartLine(), 0, false);
                bHandled = true;
              }
            }
          }
        }

      }
    }
    String msg = null;

    if (msg != null) {
      Ide.getStatusBar().setText(msg);
    }
    return bHandled;
  }
  
  
  public boolean update(IdeAction ideAction, Context context) {
    int cmdId = ideAction.getCommandId();
    boolean flag = false;
    if (cmdId == RCLICK_GOTO_SOURCE_CMD_ID)

    {
      if (isMenuAvailable(context)) {
        ideAction.setEnabled(shouldMenuBeEnabled(context));
      }
      flag = true;
    }
    return flag;


  }


  /**
   * Indicates if the menu item is avialable.  Used by the update() method.
   * @param context
   * @return true if this menu item should be shown.
  
  public static boolean isRClickCommentMenuAvailable(Context context) {
    boolean bAvail = false;
    Element el = context.getElement();
    if (el != null) {
      if (el instanceof DeployableTextNode)
        bAvail = true;
    }
    return bAvail;
  } */

  public static boolean isMenuAvailable(Context context) {
    boolean bAvail = false;
    Element el = context.getElement();
    if (el != null) {
      if (el instanceof DeployableTextNode)
        bAvail = true;
    }
    return bAvail;
  }
  
  
  //****************************************************************************
  // EditorPluginsFactory
  //****************************************************************************

  /**
   * Create plugins that should be instaled when a node for this editor is 
   * opened.  
   * @param context
   * @return List of plugins
   */
  public List createPlugins(Context context) {
    List pluginsList = new ArrayList();
    oracle.ide.model.Node ideNode = context.getNode();
    if (ideNode instanceof SqrNode) {
      // Register extensions as a plugin for SQR files
      pluginsList.add(sqrInsight);
    }
    return pluginsList;
  }


  class configMenuController implements Controller {

    public boolean handleEvent(IdeAction action, Context context) {
      int cmdId = action.getCommandId();
      if (cmdId == SqrEditorAddin.SQR_ENV_ADDIN_CMD_ID) {
        // get the Text Editor options
        SqrEnvData opts = 
          (SqrEnvData)Ide.getSettings().getData(SqrEnvData.KEY);
        //logMessage("Config Data = " + opts.getEnv());
        return true;
      }
      return false;
    }


    public boolean update(IdeAction action, Context context) {
      int cmdId = action.getCommandId();
      if (cmdId == SqrEditorAddin.SQR_ENV_ADDIN_CMD_ID) {
        action.setEnabled(true);
        return true;
      }
      return false;
    }
  }

  private static final class ctxMenuListener implements ContextMenuListener {
    private JMenuItem menuItem;
    private int nCmdID;

    ctxMenuListener(JMenuItem ctxMenuItem, int nCmdID) {
      this.menuItem = ctxMenuItem;
      this.nCmdID = nCmdID;
    }

    public void menuWillShow(ContextMenu popup) {
      //always show for SQRs
      Context context = (popup == null) ? null : popup.getContext();
      if (context == null)
        return;
      if (popup == EditorManager.getEditorManager().getContextMenu()) {
        if (nCmdID == RCLICK_GOTO_SOURCE_CMD_ID) {
          popup.add(this.menuItem);
        }
      }

      return;
    }

    public void menuWillHide(ContextMenu popup) {
    }

    public boolean handleDefaultAction(Context context) {
      return false;
    }
  }
}
