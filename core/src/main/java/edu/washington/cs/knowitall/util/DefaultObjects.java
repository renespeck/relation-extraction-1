package edu.washington.cs.knowitall.util;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import edu.washington.cs.knowitall.extractor.chunking.SentenceExtractor;
import edu.washington.cs.knowitall.extractor.chunking.mapper.BracketsRemover;
import edu.washington.cs.knowitall.extractor.chunking.mapper.SentenceEndFilter;
import edu.washington.cs.knowitall.extractor.chunking.mapper.SentenceLengthFilter;
import edu.washington.cs.knowitall.extractor.chunking.mapper.SentenceStartFilter;
import edu.washington.cs.knowitall.nlp.ChunkedSentenceReader;
import edu.washington.cs.knowitall.nlp.morphology.Morphy;

public class DefaultObjects {

    public static final String tokenizerModelFile = "de-token.bin";
    public static final String taggerModelFile = "de-pos-maxent.bin";
    public static final String sentDetectorModelFile = "de-sent.bin";
    public static final String confFunctionModelFile = "reverb-conf-maxent.gz";
    public static final String morphologyLexiconFile = "morphy-export-20110722.xml";
    public static final String smallMorphologyLexiconFile = "morphy-export-20110722.small.xml";

    /**
     * Default singleton objects
     */
    private static BracketsRemover BRACKETS_REMOVER;
    private static SentenceStartFilter SENTENCE_START_FILTER;
    private static SentenceEndFilter SENTENCE_END_FILTER;
    private static Morphy MORPHY = null;

    public static InputStream getResourceAsStream(String resource)
        throws IOException {
        InputStream in = DefaultObjects.class.getClassLoader().getResourceAsStream(resource);
        if (in == null) {
            throw new IOException("Couldn't load resource: " + resource);
        } else {
            return in;
        }
    }

    public static Morphy getMorphy(boolean test) throws IOException {
        if (MORPHY == null) {
            String f = (test) ? smallMorphologyLexiconFile : morphologyLexiconFile;
            InputStream in = getResourceAsStream(f);
            if (in == null) {
                throw new IOException("Couldn't load resource: " + f);
            }
            MORPHY = new Morphy(in, test);
        }
        return MORPHY;
    }

    public static Tokenizer getDefaultTokenizer() throws IOException {
        return new TokenizerME(new TokenizerModel(
            getResourceAsStream(tokenizerModelFile)));
    }

    public static POSTagger getDefaultPosTagger() throws IOException {
        return new POSTaggerME(new POSModel(
            getResourceAsStream(taggerModelFile)));
    }

    public static SentenceDetector getDefaultSentenceDetector()
        throws IOException {
        return new SentenceDetectorME(new SentenceModel(
            getResourceAsStream(sentDetectorModelFile)));
    }

    public static void addDefaultSentenceFilters(SentenceExtractor extractor) {
        if (BRACKETS_REMOVER == null) {
            BRACKETS_REMOVER = new BracketsRemover();
        }
        if (SENTENCE_END_FILTER == null) {
            SENTENCE_END_FILTER = new SentenceEndFilter();
        }
        if (SENTENCE_START_FILTER == null) {
            SENTENCE_START_FILTER = new SentenceStartFilter();
        }
        extractor.addMapper(BRACKETS_REMOVER);
        extractor.addMapper(SENTENCE_END_FILTER);
        extractor.addMapper(SENTENCE_START_FILTER);
        extractor.addMapper(SentenceLengthFilter.minFilter(4));
    }

    public static SentenceExtractor getDefaultSentenceExtractor()
        throws IOException {
        SentenceExtractor extractor = new SentenceExtractor();
        addDefaultSentenceFilters(extractor);
        return extractor;
    }

    public static ChunkedSentenceReader getDefaultSentenceReader(Reader in)
        throws IOException {
        ChunkedSentenceReader reader = new ChunkedSentenceReader(in, getDefaultSentenceExtractor());
        return reader;
    }

}
