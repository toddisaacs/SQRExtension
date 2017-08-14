package teisaacs.sqrext.editor;

import oracle.ide.Context;

import javax.swing.JPopupMenu;

import oracle.ide.model.Node;

import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.BasicDocument;
import oracle.javatools.editor.BasicEditorPane;
import oracle.javatools.editor.folding.CodeFoldingModel;
import oracle.javatools.editor.folding.CodeFoldingProvider;
import oracle.javatools.editor.folding.DefaultCodeFoldingModel;

public class SqrCodeFoldingProvider implements CodeFoldingProvider {
  Context context;
  public SqrCodeFoldingProvider(Context context) {
  this.context = context;
  }

  public CodeFoldingModel createModel(BasicEditorPane basicEditorPane) {
    CodeFoldingModel foldModel = null;
     Node node = context.getNode();
    TextBuffer tb = null;
     if (node instanceof SqrNode) {
       SqrNode  sqrNode = (SqrNode)node;
       tb = sqrNode.acquireTextBuffer();
       foldModel = new DefaultCodeFoldingModel(new BasicDocument(tb));
     }

    return foldModel;
  }

  public void insertUpdate(CodeFoldingModel codeFoldingModel, int i, int i1) {
  }

  public void removeUpdate(CodeFoldingModel codeFoldingModel, int i, int i1) {
  }

  public JPopupMenu getPopupMenu() {
    return null;
  }
}
