package de.hpi.normalization;

import de.hpi.nlp.extraction.chunking.ChunkedArgumentExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedBinaryExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedRelationExtraction;

/**
 * A class for normalizing {@link ChunkedBinaryExtraction} objects. This class uses {@link
 * ArgumentNormalizer} to normalize arg1 and arg2, and {@link VerbalRelationNormalizer} to normalize
 * rel.
 *
 * @author afader
 */
public class BinaryExtractionNormalizer {

    private ArgumentNormalizer argNormalizer;
    private VerbalRelationNormalizer relNormalizer;

    /**
     * Constructs a new normalizer object.
     */
    public BinaryExtractionNormalizer() {
        this.argNormalizer = new ArgumentNormalizer();
        this.relNormalizer = new VerbalRelationNormalizer(false, false, false);
    }

    /**
     * Normalizes the given argument
     * @param arg the argument to normalize
     * @return the normalized argument
     */
    public NormalizedArgumentField normalizeArgument(ChunkedArgumentExtraction arg) {
        return argNormalizer.normalizeField(arg);
    }

    /**
     * Normalizes the given relation phrase
     * @param rel the relation phrase to normalize
     * @return the normalized phrase
     */
    public NormalizedField normalizeRelation(ChunkedRelationExtraction rel) {
        return relNormalizer.normalizeField(rel);
    }

    /**
     * Normalizes the given extraction
     * @param extr the extraction to normalize
     * @return the normalized extraction
     */
    public NormalizedBinaryExtraction normalize(ChunkedBinaryExtraction extr) {
        NormalizedArgumentField arg1Norm = normalizeArgument(extr.getArgument1());
        NormalizedArgumentField arg2Norm = normalizeArgument(extr.getArgument2());
        NormalizedField relNorm = normalizeRelation(extr.getRelation());
        return new NormalizedBinaryExtraction(extr, arg1Norm, relNorm, arg2Norm);
    }

}
