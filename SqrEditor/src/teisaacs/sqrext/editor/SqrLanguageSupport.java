package teisaacs.sqrext.editor;

import oracle.javatools.editor.language.AbstractLanguageSupport;
import oracle.javatools.editor.language.DocumentRenderer;


public class SqrLanguageSupport extends AbstractLanguageSupport {
    public SqrLanguageSupport() {
    }


    protected DocumentRenderer createDocumentRenderer() {
        return new SqrDocumentRenderer(this);
    }
}
