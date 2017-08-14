package teisaacs.sqrext.editor;

import oracle.javatools.parser.AbstractLexer;
import oracle.javatools.parser.LexerToken;
import oracle.javatools.parser.util.KeywordTable;


public class SqrLexer extends AbstractLexer implements SqrTokens {

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

    /**
     * The keyword table for our python keywords.
     */
    private static KeywordTable keywordTable;


    
    public SqrLexer() {
        {
            lastToken = TK_NOT_FOUND;
            startOffset = -1;
            endOffset = -1;
            useLastToken = false;
            skipComments = true;
            setTextBuffer(null);
            setPosition(0);
        }
    }

   
    /**
     * Scans the text buffer at the current position and returns the token that
     * was found. The token and offset information is also stored in the 
     * <code>lexedToken</code> instance passed into the call.
     * 
     * @param lexedToken the instance passed in where token info is stored
     * @return the token that was found, same as calling 
     *    <code>lexedToken.getToken()</code> (for convenience)
     */
    public int lex(LexerToken lexedToken) {
        // Check whether the user called backup() after the last lex() operation
        if (useLastToken) {
            useLastToken = false;
            return fillLexerToken(lexedToken);
        }

        // Initialize token value - set this to EOF so that if an exception
        // is thrown on the first character access, we know it's an EOF and
        // not a part of a token.
        lastToken = EOF;

        // We don't do range-checking here at all, so we must catch
        // the IndexOutOfBoundsException that the text buffer may throw
        // (see the ReadTextBuffer interface for more details.)
        try {
            // Use a while loop here so that we can skip over white-space
            // and possibly comments.
            while (true) {
                // Store the starting offset of the current token
                char c = textBuffer.getChar(currentPos++);
               // if (Character.isLowerCase(c)){
                //    c = Character.toUpperCase(c);
               // }
                
        
                
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

                // Big switch statement to map token type.  Note, in case of
                // tokens that consist of more than one character, make sure
                // that the lastToken is kept up to date.  For example, if the
                // first character is '+', store TK_PLUS in case it happens to
                // be the last character in the buffer (and checking for the
                // next character throws an exception.)
                switch (c) {
                    // Handle single-character operators
               // case '=':
               // case '+':
               // case ',':
                case ')':
                case '(':
                case ':':
               // case '*':
                case '{':
                case '}':
                    lastToken = OPERATORS;
                    break;

                    // Some missing here...

                    // Multi character operators


                case '<':
                    lastToken = LESS;
                    c = textBuffer.getChar(currentPos);
                    switch (c) {
                    case '=':
                        lastToken = EQLESS;
                        currentPos++;
                        break;
                    case '>':
                        lastToken = LESSGREATER;
                        currentPos++;
                        break;
                    }
                    break;

                case '>':
                    lastToken = GREATER;
                    c = textBuffer.getChar(currentPos);
                    switch (c) {
                    case '=':
                        lastToken = EQGREATER;
                        currentPos++;
                        break;
                    }
                    break;
                
             case '!':
                    if ( textBuffer.getChar( currentPos ) == '=' )
                    {
                      lastToken = NOTEQUAL;
                      currentPos++;
                        continue;
                    }
                    if (textBuffer.getChar( currentPos ) == '!') 
                    {
                        lastToken = EOF;
                        currentPos++;
                        continue;
                    }
                   
                   
                   lastToken = SQR_COMMENT;
                    skipLineComment();
                  // if (skipComments) {
                        //lastToken = EOF;
                    //    continue;
                  //  }
                    break;
            
                 
                    // String literals?
                     case '\'':
                     case '\"':
                       // Triple quoted string?
                       if ( textBuffer.getChar( currentPos ) == c && textBuffer.getChar( currentPos + 1 ) == c  )   
                       {
                         lastToken = STRING_LITERAL;
                         currentPos++;
                         //skipTripleQuotedStringLiteral( c );
                       }
                       else
                       {
                         lastToken = STRING_LITERAL;
                         skipStringLiteral( c );
                       }
                       break;

                    // Int literals
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    lastToken = INT_LITERAL;
                    skipDecimalDigits();

                    c = textBuffer.getChar(currentPos);
                    switch (c) {
                    case '.':
                        currentPos++;
                        lastToken = FLOAT_LITERAL;
                        skipFloatLiteral();
                        break;
                    }
                    break;

                default:
                    lastToken = otherstuff(c);
                } // switch

                // break out of while loop unless we explicitly continue;ed
                break;
            } // while
        } // try
        catch (IndexOutOfBoundsException e) {
            currentPos = textBuffer.getLength();

            if (lastToken == EOF) {
                startOffset = currentPos;
            }
        }

        endOffset = currentPos;
        useLastToken = false;

        return fillLexerToken(lexedToken);
    } // lex()

    /**
     * Sets whether the lexer should generate tokens for comments
     * 
     */
    public void setSkipComments(final boolean skipComments) {
        this.skipComments = skipComments;
    }

    /**
     * Utility routine which scans through the text buffer to find the
     * end of the string literal.  Sets the current position on the
     * character after the trailing ("), or on the end of line
     * character.
     */
    private void skipStringLiteral( final char delimiter )
    {
      char c;
      while ( true )
      {
        c = textBuffer.getChar( currentPos++ );

        if ( c == delimiter )
        {
          return;
        }
        
        switch ( c )
        {
          case '\\':
            // Skip whitespace and newline characters
            while ( true )
            {
              c = textBuffer.getChar( currentPos++ );
              if ( !Character.isWhitespace( c ) )
              {
                break;    // while
              }
            }
            break;
          case '\r':
          case '\n':
            currentPos--;
            return;
        }
      }
    }


    /**
     * For identifiers and keywords and other stuff not directly handled in the
     * lex() method...
     */
    private int otherstuff(char c) throws IndexOutOfBoundsException {

        if (!isSqrIdentifierStart(c)) {
            lastToken = EOF;
            return lastToken;
        }
        

        lastToken = IDENTIFIER;
        int hash = KeywordTable.computeInsensitivePartialHash(c, 0);

        // scan until we find a character that is not part of an identifier
        // In python, an identifier is:
        // ( ( a..z | A..Z ) | _ ) ( a..z | A..Z | 0..9 | _ )*
        while (true) {
            c = textBuffer.getChar(currentPos);
            //if (Character.isLowerCase(c)){
             //   c = Character.toUpperCase(c);
           // }
            
            if (!isSqrIdentifierChar(c)) {
                // Not a valid python identifier
               break;
            }
            hash = KeywordTable.computeInsensitivePartialHash(c, hash);
            currentPos++;
        }
    

        // Now check the keyword table to see if this is a keyword
        int keyword = 
            keywordTable.lookupKeyword(textBuffer, startOffset, currentPos, 
                                       hash);

        if (keyword != TK_NOT_FOUND) {
            lastToken = keyword;
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


    /**
     * Utility routine which scans through the text buffer to find
     * the end of an float literal.  Sets the current position
     * following the last digit.  This routine expects to start
     * at the (.) or at the (e) exponent.  These are the rules from the
     * Python language spec:
     * <ul>
     *   <li>  Digits . opt-Digits opt-ExponentPart  </li>
     *   <li>  . Digits opt-ExponentPart </li>
     *   <li>  Digits ExponentPart </li>
     *   <li>  Digits opt-ExponentPart  </li>
     * </ul>
     */
    private void skipFloatLiteral() {
        // For case 1, the main lex() method eats the initial digits and
        // the dot.  For case 2, lex() eats the dot and one digit.  For
        // case 3, lex() eats the digits, but leaves the 'e'.
        char c = textBuffer.getChar(currentPos);

        // For case 1 and 2, we are expecting some possible digits.  If
        // we hit them, then eat them.
        while (isDecimalDigit(c)) {
            currentPos++;
            c = textBuffer.getChar(currentPos);
        }

        // Next, we are expecting an optional exponent
        switch (c) {
        case 'e':
        case 'E':
            currentPos++;
            // Eat the 'e', and check if the next character is a
            // sign prefix.
            c = textBuffer.getChar(currentPos);
            if ((c == '+') || (c == '-')) {
                currentPos++;
                c = textBuffer.getChar(currentPos);
            }

            // Eat the rest of the digits if any
            while (isDecimalDigit(c)) {
                currentPos++;
                c = textBuffer.getChar(currentPos);
            }

            // Fall through
        }
    }


    

    /**
     * Private utility routine to skip a sequence of decimal digits.
     * Sets the current position following the last decimal digit.
     */
    private void skipDecimalDigits() {
        char c;
        while (true) {
            c = textBuffer.getChar(currentPos);
            if (!isDecimalDigit(c)) {
                return;
            }
            currentPos++;
        }
    }

    /**
     * Utility routine to check whether the given digit is a decimal
     * digit.
     * @param digit the digit to check
     */
    public static boolean isDecimalDigit(char digit) {
        switch (digit) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return true;
        }
        return false;
    }

    /**
     * Utility routine to fill in the <code>LexerToken</code> structure
     * with the current token information we have.  Returns the
     * current token.
     * @param lexedToken the client-specified <code>LexerToken</code>
     * @return the last token found
     */
    private int fillLexerToken(LexerToken lexedToken) {
        AbstractLexer.DefaultLexerToken outToken = 
            (AbstractLexer.DefaultLexerToken)lexedToken;

        outToken.setToken(lastToken);
        outToken.setStartOffset(startOffset);
        outToken.setEndOffset(endOffset);

        return lastToken;
    }


    private static final boolean isSqrIdentifierChar(char c) {
        return (c == '_' || 
                (c <= 127 && (Character.isLetter(c) || Character.isDigit(c))) || 
                c == '-');
    }

    private static final boolean isSqrIdentifierStart(char c) {
        return (c == '_' || (c <= 127));
    }


    private static void initialize() {
        keywordTable = new KeywordTable(164);
        keywordTable.setCaseSensitivity(false);


        
        keywordTable.addKeyword("begin-procedure", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("end-procedure", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("begin-heading", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("end-heading", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("begin-program", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("end-program", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-DOCUMENT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-DOCUMENT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-EXECUTE", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-EXECUTE", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-FOOTINGT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-FOOTING", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-REPORT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-REPORT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-SQL", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-SQL", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-SELECT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-SELECT", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("BEGIN-SETUP", KW_SQR_PROCEDURE);
        keywordTable.addKeyword("END-SETUP", KW_SQR_PROCEDURE);
        
        
        
        keywordTable.addKeyword("ADD", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ALTER-COLOR-MAP", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ALTER-CONNECTION", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ALTER-LOCALE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ALTER-PRINTER", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ALTER-REPORT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ARRAY-ADD", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ARRAY-DIVIDE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ARRAY-MULTIPLY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ARRAY-SUBTRACT", KW_SQR_FUNCTION);
              
        keywordTable.addKeyword("BREAK", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("CALL", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CALL_SYSTEM", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CLEAR-ARRAY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CLOSE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("COLUMNS", KW_SQR_FUNCTION);
        keywordTable.addKeyword("COMMIT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CONCAT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CONNECT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CREATE-ARRAY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("CREATE-COLOR-PALETTE", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("DATE-TIME", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-CHART", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-COLOR-MAP", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-CONNECTION", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-IMAGE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-LAYOUT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-PRINTER", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-PROCEDURE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLRE-REPORT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DECLARE-TOC", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DATE-VARIABLE", KW_SQR_FUNCTION);        
        keywordTable.addKeyword("DISPLAY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DIVIDE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DO", KW_SQR_FUNCTION);
        keywordTable.addKeyword("DOLLAR-SYMBOL", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("ELSE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ENCODE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("END-DECLARE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("END-EVALUATE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("END-IF", KW_SQR_FUNCTION);
        keywordTable.addKeyword("END-WHILE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("EVALUATE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("EXECUTE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("EXIT-SELECT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("EXTRACT", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("FIND", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("GET", KW_SQR_FUNCTION);
        keywordTable.addKeyword("GET-COLOR", KW_SQR_FUNCTION);
        keywordTable.addKeyword("GOTO", KW_SQR_FUNCTION);
        keywordTable.addKeyword("GRAPHIC", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("HORZ-LINE", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("IF", KW_SQR_FUNCTION);
        keywordTable.addKeyword("INPUT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("INTO", KW_SQR_FUNCTION);
        keywordTable.addKeyword("LAST-PAGE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("LET", KW_SQR_FUNCTION);
        keywordTable.addKeyword("LOAD-LOOKUP", KW_SQR_FUNCTION);
        keywordTable.addKeyword("LOOKUP", KW_SQR_FUNCTION);
        keywordTable.addKeyword("LOWERCASE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("MAXLEN", KW_SQR_FUNCTION);
        keywordTable.addKeyword("MBTOSBS", KW_SQR_FUNCTION);
        keywordTable.addKeyword("MONEY-SYMBOL", KW_SQR_FUNCTION);
        keywordTable.addKeyword("MOVE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("MULTIPLY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NAME", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NEW-PAGE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NEW-REPORT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NEXT-COLUMN", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NEXT-LISTING", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NO-FORMFEED", KW_SQR_FUNCTION);
        keywordTable.addKeyword("NOLINE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ON-ERROR", KW_SQR_FUNCTION);
        keywordTable.addKeyword("OPEN", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PAGE-NUMBER", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PAGE-SIZE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("POSITION", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT-BAR-CODE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT-CHART", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT-DIRECT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT-IMAGE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINTER", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINTER-DEINIT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINTER-INIT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PRINT…CODE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PROCEDURE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("PUT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("READ", KW_SQR_FUNCTION);
        keywordTable.addKeyword("ROLLBACK", KW_SQR_FUNCTION);
        keywordTable.addKeyword("SBTOMBS", KW_SQR_FUNCTION);
        keywordTable.addKeyword("SIZE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("SHOW", KW_SQR_FUNCTION);
        keywordTable.addKeyword("STOP", KW_SQR_FUNCTION);
        keywordTable.addKeyword("STRING", KW_SQR_FUNCTION);
        keywordTable.addKeyword("SUBTRACT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("SYSTEM", KW_SQR_FUNCTION);
        keywordTable.addKeyword("TO", KW_SQR_FUNCTION);
        keywordTable.addKeyword("TOC-ENTRY", KW_SQR_FUNCTION);
        keywordTable.addKeyword("TYPE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("UNSTRING", KW_SQR_FUNCTION);
        keywordTable.addKeyword("UPPERCASE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("USE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("USE-COLUMN", KW_SQR_FUNCTION);
        keywordTable.addKeyword("USE-PRINTER-TYPE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("USE-PROCEDURE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("USE-REPORT", KW_SQR_FUNCTION);
        keywordTable.addKeyword("VERT-LINE", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("WHILE", KW_SQR_FUNCTION);
        keywordTable.addKeyword("WRITE", KW_SQR_FUNCTION);
        
        keywordTable.addKeyword("local", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("name", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("size", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("field", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("BOX", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("FIELD", KW_SQR_FUNCTION_ARG);
        keywordTable.addKeyword("FONT", KW_SQR_FUNCTION_ARG);
        
        
        keywordTable.addKeyword("ASK", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUG", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEFINE", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#ELSE", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#END-IF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#ENDIF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#IF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#IFNDEF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#INCLUDE", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#IFDEF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGA", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGB", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGC", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGD", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGE", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGG", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGH", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGI", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGJ", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGK", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGL", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGM", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGN", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGO", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGP", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGQ", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGR", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGS", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGT", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGU", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGV", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGW", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGX", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGY", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("#DEBUGZ", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGA", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGB", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGC", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGD", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGE", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGF", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGG", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGH", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGI", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGJ", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGK", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGL", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGM", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGN", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGO", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGP", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGQ", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGR", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGS", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGT", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGU", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGV", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGW", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGX", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGY", KW_SQR_PREPROCESS);
        keywordTable.addKeyword("DEBUGZ", KW_SQR_PREPROCESS);

    }

    static {
        initialize();
    }


    public void backup() {
    }


}
