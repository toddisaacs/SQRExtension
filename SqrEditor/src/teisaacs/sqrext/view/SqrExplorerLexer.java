package teisaacs.sqrext.view;

import oracle.javatools.buffer.LineMap;
import oracle.javatools.buffer.ReadTextBuffer;
import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.parser.AbstractLexer;
import oracle.javatools.parser.LexerToken;
import oracle.javatools.parser.util.KeywordTable;

import teisaacs.sqrext.editor.SqrLexerToken;
import teisaacs.sqrext.editor.SqrTokens;

public class SqrExplorerLexer extends AbstractLexer {

    /**
     * The last token found from <code>lex()</code< operation.
     */
    private int lastToken;

    /**
     * The starting offset for the last token found
     */
    private int startOffset;

    /**
     * The ending offset of the last token found.
     */
    private int endOffset;

    /**
     * Whether to use the last token that was found.
     */
    private boolean useLastToken;

    /**
     * Whether to skip comments or not.
     */
    private boolean skipComments;

    public static final int KW_BEGIN_SQR_PROCEDURE = 500;
    public static final int KW_END_SQR_PROCEDURE = KW_BEGIN_SQR_PROCEDURE + 1;

    private int state = 0;
    private static final int STATE_IN_PROCEDURE = 1;
    private static final int STATE_IN_SQL = 2;

   
    /**
     * The keyword table for our python keywords.
     */
    private static KeywordTable keywordTable;
    
    
    public SqrExplorerLexer() {
        lastToken = TK_NOT_FOUND;
        startOffset = -1;
        endOffset = -1;
        useLastToken = false;
        skipComments = true;
        setTextBuffer(null);
        setPosition(0);
    }
    



    public LexerToken createLexerToken() {
        return new SqrLexerToken();
    }

    public int lex(LexerToken lexerToken) {
        if (useLastToken) {
            useLastToken = false;
            return fillLexerToken(lexerToken);
        }

        // Initialize token value - set this to EOF so that if an exception
        // is thrown on the first character access, we know it's an EOF and
        // not a part of a token.
        lastToken = TK_EOF;
        try {
            while (true) {

                char c = textBuffer.getChar(currentPos++);

                switch (c) {
                case ' ': // SPC
                case '\t': // TAB
                case '\f': // FF
                case '\n': // LF ?
                case '\r': // CR ?
                    continue;
                }

                // Record the start offset for the token
                startOffset = currentPos - 1;

                switch (c) {
                case '!':
                    lastToken = TK_NOT_FOUND;
                    this.skipLineComment();
                    break;
                case 'b':
                case 'B':
                case 'e':
                case 'E':
                    lastToken =  checkKeyword(c);
                    //return TK_EOF;
                    break;
                default: lastToken = TK_NOT_FOUND;
                }
                 break;
            }
        } catch (IndexOutOfBoundsException e) {
            currentPos = textBuffer.getLength();

            if (lastToken == TK_EOF) {
                startOffset = currentPos;
            }
        }

        endOffset = currentPos;
        useLastToken = false;

        return fillLexerToken(lexerToken);
    }


    /**
     * Utility routine to fill in the <code>LexerToken</code> structure
     * with the current token information we have.  Returns the
     * current token.
     * @param lexedToken the client-specified <code>LexerToken</code>
     * @return the last token found
     */
    private int fillLexerToken(LexerToken lexedToken) {
       
       
       SqrLexerToken t = (SqrLexerToken)lexedToken;

        t.setToken(lastToken);
        t.setStartOffset(startOffset);
        t.setEndOffset(endOffset);
    
        return lastToken;
    }

    public void backup() {
    }

    private int checkKeyword(char c) throws IndexOutOfBoundsException {
        lastToken = TK_NOT_FOUND;
        int hash = KeywordTable.computeInsensitivePartialHash(c, 0);
      
        while (true) {
                   c = textBuffer.getChar(currentPos);
                                      
                   if (!isSqrIdentifierChar(c)) {
                       // Not a valid sqr identifier
                      break;
                   }
                   hash = KeywordTable.computeInsensitivePartialHash(c, hash);
                   currentPos++;
               }
        
        // Now check the keyword table to see if this is a keyword
        int keyword = 
            keywordTable.lookupKeyword(textBuffer, startOffset, currentPos, 
                                       hash);

        //if (keyword != TK_NOT_FOUND) {
            lastToken = keyword;
        //}

        if (keyword == KW_BEGIN_SQR_PROCEDURE) {
            state = STATE_IN_PROCEDURE;
            return lastToken;
        }

        if (keyword == KW_END_SQR_PROCEDURE) {
            state = 0;
            return lastToken;
        }
    
        return lastToken;
    }


    /**
     * Utility routine which scans through the text buffer to find the
     * end of the single-line comment.  Sets the current position on
     * the end of line character.
     */
    private void skipLineComment() {
        char c;
        while (true) {
            c = textBuffer.getChar(currentPos);
            switch (c) {
            case '\r':
            case '\n':
                return;
            }
            currentPos++;
        }
    }
    
    private static void initialize() {
        keywordTable = new KeywordTable(1);
        keywordTable.setCaseSensitivity(false);
        
        keywordTable.addKeyword("begin-procedure", KW_BEGIN_SQR_PROCEDURE);
        keywordTable.addKeyword("end-procedure", KW_END_SQR_PROCEDURE);
    }
    
    static {
        initialize();
    }
    
    private static final boolean isSqrIdentifierChar(char c) {
        return (c == '_' || 
                (c <= 127 && (Character.isLetter(c) || Character.isDigit(c))) || 
                c == '-');
    }
}
