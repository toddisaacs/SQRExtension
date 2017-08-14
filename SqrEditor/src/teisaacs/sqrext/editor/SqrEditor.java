package teisaacs.sqrext.editor;

import java.awt.Component;


import oracle.ide.ceditor.CodeEditor;
import oracle.ide.ceditor.find.FindableEditor;
import oracle.ide.editor.Editor;
import oracle.ide.model.UpdateMessage;

import oracle.javatools.editor.BasicEditorPane;

public class SqrEditor extends Editor implements FindableEditor {

  //****************************************************************************
  // Editor implementation
  //****************************************************************************
  public void open() {
  }

  public Component getGUI() {
    return null;
  }

  public void update(Object object, UpdateMessage updateMessage) {
  }
  
  //****************************************************************************
  // FindableEditor implementation
  //****************************************************************************
  public BasicEditorPane getFocusedEditorPane() {
    return null;
  }
  
  
  
}
