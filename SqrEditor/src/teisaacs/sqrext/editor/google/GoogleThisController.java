package teisaacs.sqrext.editor.google;


import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
//import oracle.ide.extension.RegisteredByExtension; 11g
import oracle.ide.editor.Editor;
import oracle.ide.view.View;

import teisaacs.sqrext.editor.SqrEditor;


/**
 * Controller for action esdksample.GoogleThis.
 */
//@RegisteredByExtension("oracle.ide.extsamples.codeinteraction") 11g
public final class GoogleThisController implements Controller {
    public boolean update(IdeAction action, Context context) {
        action.setEnabled( hasSelectedText( context.getView() ) );
        return true;
    }

    public boolean handleEvent(IdeAction action, Context context) {
    return false;
    }
    
    private boolean hasSelectedText( View view ) {
        if ( !(view instanceof Editor )) return false;
        
        String selectedText = ((CodeEditor)view).getSelectedText(); 
        return selectedText != null && selectedText.length() != 0;
 
    }
}
