package teisaacs.sqrext.editor;

import oracle.javatools.editor.folding.AbstractCodeFoldingPlugin;
import oracle.javatools.editor.folding.CodeFoldingProvider;
import oracle.ide.Context;

public class SqrCodeFoldingPlugin extends AbstractCodeFoldingPlugin{
private Context context;
  public SqrCodeFoldingPlugin(Context con) {
  context = con;
  }

  protected CodeFoldingProvider createProvider() {
    SqrCodeFoldingProvider provider = new SqrCodeFoldingProvider(context);
    return provider;
  }
}
