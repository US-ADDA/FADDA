package com.fadda.recursivetypes.parsers;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TreeLexer extends Lexer {
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, ID = 7, INT = 8, DOUBLE = 9,
            WS = 10;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\f;\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\7\b&\n\b\f" +
                    "\b\16\b)\13\b\3\t\6\t,\n\t\r\t\16\t-\3\n\3\n\3\n\5\n\63\n\n\3\13\6\13" +
                    "\66\n\13\r\13\16\13\67\3\13\3\13\2\2\f\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21" +
                    "\n\23\13\25\f\3\2\5\b\2\60\60==C]__aac|\t\2\60\60\62;==C]__aac|\5\2\13" +
                    "\f\17\17\"\"\2>\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3" +
                    "\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2" +
                    "\3\27\3\2\2\2\5\31\3\2\2\2\7\33\3\2\2\2\t\35\3\2\2\2\13\37\3\2\2\2\r!" +
                    "\3\2\2\2\17#\3\2\2\2\21+\3\2\2\2\23/\3\2\2\2\25\65\3\2\2\2\27\30\7start\2" +
                    "\2\30\4\3\2\2\2\31\32\7*\2\2\32\6\3\2\2\2\33\34\7.\2\2\34\b\3\2\2\2\35" +
                    "\36\7+\2\2\36\n\3\2\2\2\37 \7-\2\2 \f\3\2\2\2!\"\7/\2\2\"\16\3\2\2\2#" +
                    "\'\t\2\2\2$&\t\3\2\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\20\3\2" +
                    "\2\2)\'\3\2\2\2*,\4\62;\2+*\3\2\2\2,-\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\22" +
                    "\3\2\2\2/\60\5\21\t\2\60\62\7\60\2\2\61\63\5\21\t\2\62\61\3\2\2\2\62\63" +
                    "\3\2\2\2\63\24\3\2\2\2\64\66\t\4\2\2\65\64\3\2\2\2\66\67\3\2\2\2\67\65" +
                    "\3\2\2\2\678\3\2\2\289\3\2\2\29:\b\13\2\2:\26\3\2\2\2\7\2\'-\62\67\3\b" +
                    "\2\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public TreeLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "ID", "INT", "DOUBLE",
                "WS"
        };
    }

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'_'", "'('", "','", "')'", "'+'", "'-'"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, "ID", "INT", "DOUBLE", "WS"
        };
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "Tree.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
}
