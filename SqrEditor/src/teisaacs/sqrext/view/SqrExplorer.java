package teisaacs.sqrext.view;

import java.awt.Component;

import java.awt.event.MouseEvent;

import java.util.Enumeration;
import java.util.EventObject;

import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import oracle.ide.Context;
import oracle.ide.ceditor.CodeEditor;
import oracle.ide.ceditor.find.FindableEditor;
import oracle.ide.controller.Controller;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.explorer.Explorer;
import oracle.ide.explorer.TreeExplorer;

import oracle.ide.model.Element;

import oracle.ide.model.Observer;
import oracle.ide.model.UpdateMessage;
import oracle.ide.view.View;

import oracle.javatools.editor.BasicEditorPane;

import teisaacs.sqrext.editor.SqrEditor;
import teisaacs.sqrext.editor.SqrNode;
import teisaacs.sqrext.model.DefaultSqrElement;
import teisaacs.sqrext.model.SqrElement;
import teisaacs.sqrext.model.SqrHeadElement;
import teisaacs.sqrext.model.SqrProcedureElement;

public class SqrExplorer extends Explorer implements TreeSelectionListener, 
                                                     Observer {

  private JTree sqrTree = null;
  private SqrNode sqrNode = null;
  private Context context = null;
  private JScrollPane scrollPane = null;

  private DefaultTreeModel treeModel = null;
  private DefaultMutableTreeNode currentlySelectedNode = null;
  private BasicEditorPane currentEditor = null;
  private DefaultMutableTreeNode root = null;

  public SqrExplorer() {
    super();
  }

  public Component getGUI() {
    //LogMessage("getGUI()");
    return scrollPane;
  }


  public Context getContext(EventObject event) {
    this.context = super.getContext(event);
    if (sqrNode != null) {
      this.context.setNode(sqrNode);
      //register this node as an observer
      sqrNode.attach(this);
    }
    return this.context;
  }


  /**
   * the Context to use as initialization data.
   * @param context
   */
  public void setContext(Context context) {
    this.context = context;
    Element element = context.getElement();
    if (element instanceof SqrNode) {
      sqrNode = (SqrNode)element;
      if (sqrTree == null && sqrNode != null) {
        buildTree();
        scrollPane = new JScrollPane(sqrTree);
      }
    }
  }

  private static final void LogMessage(String msg) {
    oracle.ide.log.LogManager.getLogManager().showLog();
    oracle.ide.log.LogManager.getLogManager().getMsgPage().log(msg + "\n");
  }


  private void buildTree() {

    root = new DefaultMutableTreeNode(sqrNode.getName());

    updateTreeModel();
    sqrTree = new JTree(new DefaultTreeModel(root));
    sqrTree.addTreeSelectionListener(this);
    sqrTree.setToolTipText("");
    sqrTree.setCellRenderer(new SqrExplorerCellRenderer());
    sqrTree.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            //tree_mousePressed(e);
          }

          public void mouseReleased(MouseEvent e) {
            //tree_mouseReleased(e);
          }

          public void mouseClicked(MouseEvent e) {
            tree_mouseClicked(e);
          }
        });

  }

  private void updateTreeModel() {
    Vector v = sqrNode.getElements();

    root.removeAllChildren();

    boolean isMain = true;
    Enumeration e;
    e = v.elements();
    DefaultMutableTreeNode lastLeaf = null;


    DefaultMutableTreeNode includes = new DefaultMutableTreeNode("Includes");

    boolean hasIncludes = false;
    while (e.hasMoreElements()) {
      SqrElement item = (SqrElement)e.nextElement();
      DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(item);

      if (item instanceof teisaacs.sqrext.model.SqrProcedureElement || 
          item instanceof teisaacs.sqrext.model.SqrHeadElement || 
          item instanceof teisaacs.sqrext.model.SqrBeginProgram ||
          item instanceof teisaacs.sqrext.model.SqrSetupElement) {
        lastLeaf = leaf;
        root.add(leaf);

        //LogMessage("add main leaf");
      } else {
        //LogMessage("not head or procedure");
        if (item instanceof teisaacs.sqrext.model.SqrIncludeElement) {
          if (!hasIncludes) {
            root.add(includes);
            hasIncludes = true;
            includes.add(leaf);
          } else {
            includes.add(leaf);
          }
        } else {
          if (lastLeaf == null) {
            root.add(leaf);
            //LogMessage("add leaf");
          } else {
            lastLeaf.add(leaf);
            //LogMessage("add child");

          }
        }
      }
    }
  }


  void tree_mouseClicked(MouseEvent e) {
    int nbClick = e.getClickCount();
    if (nbClick == 2) // On the second one please
    {
      Object whoIsIt = e.getSource();

      BasicEditorPane editorPane = null;
      if (context != null) {

        EditorManager em = EditorManager.getEditorManager();
        Editor ed = em.getCurrentEditor();

        View view = context.getView();

        if (ed instanceof CodeEditor) {
          CodeEditor cEd = (CodeEditor)ed;
          SqrElement item = (SqrElement)currentlySelectedNode.getUserObject();
          cEd.gotoLine(item.getStartLine(), 0, false);
        }

      }
    }
  }

  public void valueChanged(TreeSelectionEvent e) {


    TreePath treePath = e.getNewLeadSelectionPath();
    if (treePath != null) {
      DefaultMutableTreeNode dmtn = 
        (DefaultMutableTreeNode)treePath.getLastPathComponent();
      currentlySelectedNode = dmtn;
    }
  }

  public void update(Object object, UpdateMessage updateMessage) {
    this.updateTreeModel();
    sqrTree.updateUI();
  }
}
