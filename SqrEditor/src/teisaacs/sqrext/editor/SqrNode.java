package teisaacs.sqrext.editor;

import java.util.Vector;

import javax.swing.Icon;

import oracle.ide.model.DeployableTextNode;
import oracle.ide.net.URLFileSystem;

import oracle.javatools.buffer.LineMap;

import oracle.javatools.buffer.ReadTextBuffer;
import oracle.javatools.buffer.TextBuffer;

import teisaacs.sqrext.SqrEditorAddin;
import teisaacs.sqrext.model.SqrBeginProgram;
import teisaacs.sqrext.model.SqrHeadElement;
import teisaacs.sqrext.model.SqrIncludeElement;
import teisaacs.sqrext.model.SqrProcedureElement;
import teisaacs.sqrext.model.SqrSQLElement;
import teisaacs.sqrext.model.SqrSetupElement;
import teisaacs.sqrext.view.SqrExplorerLexer;


public class SqrNode extends DeployableTextNode {

    private LineMap lineMap = null;
    private SqrExplorerLexer lexer = null;
    private int offSet = 0;
    private ReadTextBuffer rtb = null;

    public SqrNode() {

        super();
        lexer = new SqrExplorerLexer();

    }

    public Icon getIcon() {
        if (SqrEditorAddin.SQR_FILE_ICON != null) {
            return SqrEditorAddin.SQR_FILE_ICON;
        } else {
            return super.getIcon();
        }
    }

    public String getSourceFile() {
        return URLFileSystem.getFileName(getURL());
    }

    public String getName() {
        return "SQR";
    }


    public Vector getElements() {
        Vector v = new Vector();
        
        //lexer.setTextBuffer(this.acquireTextBuffer());
        rtb = this.acquireTextBuffer();
        TextBuffer tb = this.acquireTextBuffer();
        lineMap = tb.getLineMap();

        offSet = -1;
        char c;

        try {
            while (true) {
                offSet++;
                c = rtb.getChar(offSet);
                
                switch (c) {
                case '!':
                    offSet++;
                    skipLine();
                    break;
                case '#':
                    c = rtb.getChar(++offSet);
                    switch(c) {
                    case 'i':
                    case 'I':
                        c = rtb.getChar(++offSet);
                        switch(c) {
                        case 'n':
                        case 'N':
                            c = rtb.getChar(++offSet);
                            switch(c) {
                            case 'c':
                            case 'C':
                                c = rtb.getChar(++offSet);
                                switch(c) {
                                case 'l':
                                case 'L':
                                    c = rtb.getChar(++offSet);
                                    switch(c) {
                                    case 'u':
                                    case 'U':
                                        c = rtb.getChar(++offSet);
                                        switch(c) {
                                        case 'd':
                                        case 'D':
                                            c = rtb.getChar(++offSet);
                                            switch(c) {
                                            case 'e':
                                            case 'E':
                                                //found #include
                                                 String includeFileName = parseProcedureName();
                                                 SqrIncludeElement item = new SqrIncludeElement();
                                                item.setName(includeFileName);
                                                item.setToolTipText(includeFileName);
                                                //item.setStartLine(1);
                                                item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                                v.add(item);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }        
                    }
                case 'b':
                case 'B':
                    offSet++;
                    c = rtb.getChar(offSet);
                    switch (c) {
                    case 'e':
                    case 'E':
                        c = rtb.getChar(++offSet);
                        switch (c) {
                        case 'g':
                        case 'G':
                            c = rtb.getChar(++offSet);
                            switch (c) {
                            case 'i':
                            case 'I':
                                c = rtb.getChar(++offSet);
                                switch (c) {
                                case 'n':
                                case 'N':
                                    c = rtb.getChar(++offSet);
                                    switch (c) {
                                    case '-':
                                        //Found begin-
                                        //LogMessage("Found BEGIN-" );
                                        String test = parseProcedureType();
                                        //LogMessage("'" + test + "'" );
                                       
                                        if (test.toLowerCase().equals("procedure")) {
                                            //get name
                                            String procName = parseProcedureName();
                                            
                                            SqrProcedureElement item = 
                                                new SqrProcedureElement();
                                            item.setName(procName);
                                            item.setToolTipText(procName);
                                            //item.setStartLine(1);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("report")) {
                                            SqrBeginProgram item = 
                                                new SqrBeginProgram();
                                            item.setName(test);
                                            item.setToolTipText(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("program")) {
                                            SqrBeginProgram item = 
                                                new SqrBeginProgram();
                                            item.setName(test);
                                            item.setToolTipText(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("setup")) {
                                            SqrSetupElement item = 
                                                new SqrSetupElement();
                                            item.setName(test);
                                            item.setToolTipText(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("heading")) {
                                            SqrHeadElement item = 
                                                new SqrHeadElement();
                                            item.setName(test);
                                            item.setToolTipText(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("sql")) {
                                            int startOffset = offSet;
                                            SqrSQLElement item = 
                                                new SqrSQLElement();
                                            item.setName(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        } else if (test.toLowerCase().equals("select")) {
                                            SqrSQLElement item = 
                                                new SqrSQLElement();
                                            item.setName(test);
                                            item.setStartLine(lineMap.getLineFromOffset(offSet) + 1);
                                            v.add(item);
                                        }
                                        
                                        //LogMessage("Found : begin-" + test );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                } //End Main Switch    
            } // End While   
        } catch (IndexOutOfBoundsException e) {
            //just run past end of buffer
            
        }
        
        return v;
    }

   

    private String parseProcedureType() {
        //we are at the '-' in string 'begin-xxxxxx'
        char c;
        StringBuffer sb = new StringBuffer("");
        offSet++;

        try {
            while (true) {

                c = rtb.getChar(offSet);

                if (c == '\r' || c == '\n' || c == ' ') {
                    return sb.toString();
                } else {
                    if (c != '\'') {
                        sb.append(c); 
                    }
                    offSet++;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //just run past end of buffer
            offSet--;
            return sb.toString();
        }
    }

    
    private String parseProcedureName() {
        //we are right after the 'begin-xxxxxx '
        char c;
        StringBuffer sb = new StringBuffer("");

        //increment to next character
        offSet++;

        boolean inName = false;

    
        try {
            while (true) {

                c = rtb.getChar(offSet);

                if (c == '\r' || c == '\n' || c == '(') {
                    return sb.toString();
                } 
                else {
                    if (inName) {
                        //in an identifier leave when space found
                        if (c == ' ') {
                            return sb.toString();
                        } 
                        else {
                            if (c != '\'') {
                              sb.append(c);
                            }
                            offSet++;
                        }

                    } 
                    else {
                        //not in identifier
                        if (c == ' ') {
                           //we want first non-space
                           offSet++;
                        } else {
                            inName = true;
                            if (c != '\'') sb.append(c);
                            offSet++;
                        }
                    }

                }
            }
        } catch (IndexOutOfBoundsException e) {
            //just run past end of buffer
            offSet--;
            return sb.toString() + "...";
        }
    }

    /**
     * Utility routine which scans through the text buffer to find the
     * end of the single-line comment.  Sets the current position on
     * the end of line character.
     */
    private void skipLine() {
        char c;
        while (true) {
            c = rtb.getChar(offSet);
            switch (c) {
            case '\r':
            case '\n':
                return;
            }
            offSet++;
        }
    }


    private static final void LogMessage(String msg) {
        oracle.ide.log.LogManager.getLogManager().showLog();
        oracle.ide.log.LogManager.getLogManager().getMsgPage().log(msg + "\n");
    }

}
