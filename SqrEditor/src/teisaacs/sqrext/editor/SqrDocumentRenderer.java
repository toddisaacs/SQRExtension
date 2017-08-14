package teisaacs.sqrext.editor;

import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.language.BlockRenderer;
import oracle.javatools.editor.language.LanguageSupport;
import oracle.javatools.editor.language.LexerDocumentRenderer;
import oracle.javatools.parser.Lexer;

public class SqrDocumentRenderer  extends LexerDocumentRenderer
    implements SqrTokens
    {

    public SqrDocumentRenderer( LanguageSupport support )
    {
      super( support );
    }
    
    protected BlockRenderer createBlockRenderer() {
        TextBuffer textBuffer = getTextBuffer();
        return new SqrBlockRenderer( textBuffer );
    }

    protected Lexer createLexer() {
        return new SqrLexer();
    }

    public boolean isMultiLineToken(int i) {
        return false;
    }
}
