package teisaacs.sqrext.editor;

import oracle.javatools.parser.LexerToken;

public class SqrLexerToken implements LexerToken {
    
    private int token;
    private int startOffset;
    private int endOffset;
    private String name;

    public SqrLexerToken() {
    }

    public int getToken() {
        return token;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
    
    public void setToken(int t) {
        token = t;
    }
    
    public void setEndOffset(int offset) {
         endOffset = offset;
    }
    
    public void setStartOffset(int offset) {
        startOffset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
