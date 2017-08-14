package teisaacs.sqrext.editor.insight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ListModel;
import javax.swing.text.BadLocationException;
import oracle.javatools.buffer.LineMap;
import oracle.javatools.editor.BasicDocument;
import oracle.javatools.editor.BasicEditorPane;
import oracle.javatools.editor.EditDescriptor;
import oracle.javatools.editor.insight.AbstractInsight;
import oracle.javatools.editor.insight.InsightData;
import oracle.javatools.editor.insight.InsightProvider;
import oracle.javatools.editor.insight.InsightView;
import oracle.javatools.editor.insight.ListDataItem;
import oracle.javatools.editor.insight.ListInsightData;
import oracle.javatools.editor.insight.ListInsightView;

/**
 * Basic SQR code insight provider that will serve as both a regular and smart
 * Insight.  This code is based on an example implementation sent to me
 * by Keimpe Bronkhorst. 
 * 
 * @author Todd Isaacs
 */
public class SqrInsightProvider extends AbstractInsight implements InsightProvider, 
                                                                   ListInsightData {

  private BasicEditorPane editor;
  private int commandStart;

  //***************************************************************************
  // InsightProvider - Implementation
  //***************************************************************************

  /**
   * @return this InsightProvider
   */
  protected InsightProvider createInsightProvider() {
    return this;
  }


  /**
   * @return this InsightProvider
   */
  protected InsightProvider createSmartInsightProvider() {
    return this;
  }


  /**
   * Basic SQR insight will not be triggered by a special character so this will
   * just return false.  SQR insight will be triggered by CTRL-SPACE key stroke.
   * 
   * @param c   
   * @return False in this impementation so no special chracter trigger.
   */
  public boolean isInsightTriggerChar(char c) {
    return false;
  }


  //****************************************************************************
  // InsightData
  //****************************************************************************

  /**
   * Called when insight is invoked for each occurance such as ctrl-space.  
   * 
   * @param basicEditorPane
   * @return The initial insight data
   */
  public InsightData getInsightData(BasicEditorPane basicEditorPane) {
    editor = basicEditorPane;

    //set the command start position
    int indx = getCaretPosition();
    int commandLength = 0;
    commandStart = getCaretPosition();
    while (indx > getLineStart()) {
      indx = indx - 1;
      if (getTextBuffer().getChar(indx) != ' ') {
        commandStart = indx;
        commandLength++;
      }
    }

    return this;
  }


  /**
   * Called when insight is displayed for each time the caret is moved.  This is
   * a good place to filter the data returned based on what has been typed so.
   * We have to be able to handle backspaces and typed chraters.
   * 
   * @param basicEditorPane
   * @param insightData
   * @return Updated insight data based on offset and caret position.
   */
  public InsightData updateInsightData(BasicEditorPane basicEditorPane, 
                                       InsightData insightData) {
    return insightData;
  }

  /**
   * @return Returns a basic JDeveloper ListInsightView
   */
  public InsightView getInsightView() {
    return new ListInsightView();
  }

  // Used for undo stack
  private EditDescriptor editDescriptor;
  private List dataModel = new ArrayList();

  // Setup Datamodel avialable for insight
  {
    editDescriptor = new EditDescriptor("SQRCodeInsightExtension");
    
    //setup list
    dataModel.addAll( Arrays.asList(fileFunc));
    dataModel.addAll(Arrays.asList(numericFunc));
    dataModel.addAll(Arrays.asList(miscFunc));
    dataModel.addAll(Arrays.asList(sqrCommands));
    dataModel.addAll(Arrays.asList(reservedVars));
    
    //sort
    Collections.sort(dataModel);
  }
  
  
  /**
   * Gets the datamodel based on what has been typed and the caret position. 
   * 
   * I decided to just return the correct data here instead of using 
   * updateInsightData().  
   * 
   * @return Data model based on caret position
   */
  public Object[] getData() {
    //figure out what has been typed
    String typedChars = getStringAtCurPosition();

    // Here you make the data you want to show
    ArrayList filteredModel = new ArrayList();
    Iterator iterator = dataModel.iterator();

    if (typedChars.length() > 0) {
      //loop through dataModel and filter the contents to match
      while (iterator.hasNext()) {
        String item = (String)iterator.next();
        if (item.toLowerCase().startsWith(typedChars.toString().toLowerCase())) {
          filteredModel.add(new SqrListDataItem(item));
        }
      }
      return filteredModel.toArray();

    } else {
      //nothing typed return whole model
      if (getCaretPosition() < commandStart) {
        return filteredModel.toArray();
      } else {
        while (iterator.hasNext()) {
          filteredModel.add(new SqrListDataItem((String)iterator.next()));
        }
        return filteredModel.toArray();
      }
    }
  }


  /**
   * This method does not seam to be called by the JDev API??.  The 
   * getMatchingDataCount() method is called each key stroke. However it  
   * appears that updateInsightData() is called.  I'm not sure when this is 
   * callled.
   * 
   * @return
   */
  public Object[] getMatchingData() {
    return getData();
  }

  /**
   * 
   * @inheritDoc
   */
  public int getMatchingDataCount() {
    return getData().length;
  }

  /**
   * 
   * @inheritDoc
   */
  public Object getDefault() {
    return null;
  }

  /**
   * Called when an item is selected from the displayed list.
   * @param selectedObject
   * @return true
   */
  public boolean complete(Object selectedObject) {
    // Get the name from the data item
    // Here you can see that you have control over what is inserted
    // in the editor buffer
    final SqrListDataItem item = (SqrListDataItem)selectedObject;
    String itemName = item.getName();
    String typedChars = getStringAtCurPosition();
    final BasicDocument document = (BasicDocument)editor.getDocument();
    final int caretOffset = editor.getCaretPosition();

    // You might need to adjust the caretOffset if the code is embedded
    // in other text, such as happens with jsp files
    // for adjusting the output location
    final int offsetAdjustment = typedChars.length() * -1;

    // All right, that is what we want to replace - do it in a single
    // undoable transaction.  This will acquire a write lock since we
    // are about to read from the document.
    editor.beginEdit(editDescriptor);

    // Now, remove the partial text the user typed, and insert the
    // selected one.
    try {
      document.remove(commandStart, typedChars.length());
      //determine case to output since SQR is case insensitive, use typedChars
      if (typedChars.length() >= 1) {
        String lCaseCmpr = typedChars.toString().toLowerCase();
        String uCaseCmpr = typedChars.toString().toUpperCase();

        if (typedChars.toString().equals(lCaseCmpr)) {
          //lower case
          itemName = item.getName().toLowerCase();
        } else {
          if (typedChars.toString().equals(uCaseCmpr)) {
            //upper case 
            itemName = item.getName().toUpperCase();
          } else {
            //Mixed case, length must be greater than 1 to hit this
            String strtChar = itemName.substring(0, 1).toUpperCase();
            itemName = 
                strtChar + itemName.substring(1, itemName.length()).toLowerCase();
          }
        }
      }
      document.insertString(caretOffset + offsetAdjustment, itemName, null);
    } catch (BadLocationException e) {
        System.err.println("Exception occurred in completion: " + e); //NORES
        e.printStackTrace();
    } finally {
      editor.endEdit();
    }

    // Return false, indicating that Insight should not retrigger
    return false;
  }


  /**
   * used to perform a partial complete when there are multiple matches and the
   * user hits TAB and the user had typed some data.  If no data just return the 
   * same thing as getData().
   * @inheritDoc 
   */
  public void partialComplete() {
    
  }


  /**
   * Returns the datamodel for the list.  
   * @return
   */
  public ListModel getListModel() {
    //return new ReadOnlyListModel(getData());  11G API
    return new DefaultComboBoxModel(getData());
  }


  /**
   * Helper to find the string of characters typed from the current caret 
   * position.  
   * 
   * @return String of characters typed from the current position.
   */
  private String getStringAtCurPosition() {
    if (getCaretPosition() < commandStart) {
      return "";
    } else {
      return getTextBuffer().getString(commandStart, 
                                       getCaretPosition() - commandStart);
    }
  }


  /**
   * Helper method to get the line number the caret is currently on.
   * 
   * @return current line of caret.
   */
  private int getLineStartNumber() {
    int offSet = getCaretPosition();
    LineMap lineMap = getTextBuffer().getLineMap();
    return lineMap.getLineFromOffset(offSet);
  }


  /**
   * Helper method to find the offset of the current line starting position.
   * 
   * @return current line start offset.
   */
  private int getLineStart() {
    return getTextBuffer().getLineMap().getLineStartOffset(getLineStartNumber());
  }



  //****************************************************************************
  //Insight Data -- move to somthing easier to support??
  //****************************************************************************

  //File-Related Functions
  private static final String[] fileFunc = { "delete", "rename", "exists" };

  //Numeric Functions
  private static final String[] numericFunc = 
  { "abs", "e10", "round", "acos", "exp", "sign", "asin", "floor", "sin", 
    "atan", "log", "sinh", "ceil", "log10", "sqrt", "cos", "mod", "tan", 
    "cosh", "power", "tanh", "deg", "rad", "trunc" };

  //Miscellaneous Functions;
  private static final String[] miscFunc = 
  { "instr", "rtrim", "ascii", "instrb", "setproperty", "asciiic", "isblank", 
    "strtodate", "isnull", "substr", "cond", "length", "substrb", "dateadd", 
    "lengthb", "to_char", "datediff", "lower", "to_multi_byte", "datenow", 
    "lpad", "to_number", "datetostr", "ltrim", "to_single_byte", "edit", "nvl", 
    "translate", "getenv", "range", "upper", "getcapability", "roman", 
    "wrapdepth", "getproperty", "rpad" };

  //commands
  private static final String[] sqrCommands = 
  { "ADD", "ARRAY-ADD", "ALTER-COLOR-MAP", "ARRAY-DIVIDE", "ALTER-CONNECTION", 
    "ARRAY-MULTIPLY", "ALTER-LOCALE", "ARRAY-SUBTRACT", "ALTER-PRINTER", "ASK", 
    "ALTER-REPORT", "BEGIN-DOCUMENT", "BEGIN-REPORT", "BEGIN-EXECUTE", 
    "BEGIN-SELECT", "BEGIN-FOOTING", "BEGIN-SETUP", "BEGIN-HEADING", 
    "BEGIN-SQL", "BEGIN-PROCEDURE", "BREAK", "BEGIN-PROGRAM", "CALL", "COMMIT", 
    "CALL SYSTEM", "CONCAT", "CLEAR-ARRAY", "CONNECT", "CLOSE", "CREATE-ARRAY", 
    "COLUMNS", "CREATE-COLOR-PALETTE", "DATE-TIME", "DECLARE-PROCEDURE", 
    "#DEBUG", "DECLARE-REPORT", "DECLARE-CHART", "DECLARE-TOC", 
    "DECLARE-COLOR-MAP", "DECLARE-VARIABLE", "DECLARE-CONNECTION", "#DEFINE", 
    "DECLARE-IMAGE", "DISPLAY", "DECLARE-LAYOUT", "DIVIDE", "DECLARE PRINTER", 
    "DO", "DECLARE-PRINTER", "DOLLAR-SYMBOL", "DECLARE PROCEDURE", "#ELSE", 
    "END-PROGRAM", "ELSE", "END-REPORT", "ENCODE", "END-SELECT", "END-DECLARE", 
    "END-SETUP", "END-DOCUMENT", "END-SQL", "END-EVALUATE", "END-WHILE", 
    "END-FOOTING", "EVALUATE", "END-HEADING", "EXECUTE", "#END-IF", "#ENDIF", 
    "EXIT-SELECT", "END-IF", "EXTRACT", "END-PROCEDURE", "FIND", "GET", 
    "GRAPHIC FONT", "GET-COLOR", "GRAPHIC HORZ-LINE", "GOTO", 
    "GRAPHIC VERT-LINE", "GRAPHIC BOX", "#IF", "#IFNDEF", "IF", "#INCLUDE", 
    "#IFDEF", "INPUT", "LAST-PAGE", "LOOKUP", "LET", "LOWERCASE", 
    "LOAD-LOOKUP", "MBTOSBS", "MOVE", "MONEY-SYMBOL", "MULTIPLY", "NEW-PAGE", 
    "NEXT-LISTING", "NEW-REPORT", "NO-FORMFEED", "NEXT-COLUMN", "OPEN", 
    "PAGE-NUMBER", "PRINT CODE", "PAGE-SIZE", "PRINT-DIRECT", "POSITION", 
    "PRINT-IMAGE", "PRINT", "PRINTER-DEINIT", "PRINT-BAR-CODE", "PRINTER-INIT", 
    "PRINT-CHART", "PUT", "READ", "ROLLBACK", "SBTOMBS", "SET-MEMBERS", 
    "SECURITY", "SHOW", "SET-COLOR", "STOP", "SET-GENERATIONS", "STRING", 
    "SET-LEVELS", "SUBTRACT", "TOC-ENTRY", "UNSTRING", "USE-PRINTER-TYPE", 
    "UPPERCASE", "USE-PROCEDURE", "USE", "USE-REPORT", "USE-COLUMN", "WHILE", 
    "WRITE" };

  //reserved variables
  private static final String[] reservedVars = 
  { "#current-column", "{sqr-encoding}", "$current-date", "$sqr-hostname", 
    "#current-line", "{sqr-hostname}", "#end-file", "$sqr-locale", 
    "#page-count", "#sqr-max-columns", "#return-status", "#sqr-max-lines", 
    "#sql-count", "#sqr-pid", "$sql-error", "$sqr-platform", "#sql-status", 
    "{sqr-platform}", "$sqr-database", "$sqr-program", "{sqr-database}", 
    "$sqr-report", "$sqr-dbcs", "$sqr-ver", "{sqr-dbcs}", "$username", 
    "$sqr-encoding" };

  //html procedures
  private static final String[] htmlProcedures = 
  { "html_br", "html_img", "html_p", "html_center", "html_nobr", "html_p_end", 
    "html_center_end", "html_nobr_end", "html_set_body_attributes", "html_hr", 
    "html_on", "html_set_head_tags", "html_h1", "html_h3", "html_h5", 
    "html_h1_end", "html_h3_end", "html_h5_end", "html_h2", "html_h4", 
    "html_h6", "html_h2_end", "html_h4_end", "html_h6_end", "html_blink", 
    "html_kbd", "html_sub", "html_blink_end", "html_kbd_end", "html_sub_end", 
    "html_cite", "html_samp", "html_sup", "html_cite_end", "html_samp_end", 
    "html_sup_end", "html_code", "html_strike", "html_code_end", 
    "html_strike_end", "html_a", "html_a_end", "html_dd", "html_dt", "html_ol", 
    "html_dd_end", "html_dt_end", "html_ol_end", "html_dir", "html_li", 
    "html_ul", "html_dir_end", "html_li_end", "html_ul_end", "html_dl", 
    "html_menu", "html_dl_end", "html_menu_end", "html_caption", "html_td", 
    "html_tr", "html_caption_end", "html_td_end", "html_tr_end", "html_table", 
    "html_th", "html_table_end", "html_th_end" };
}
