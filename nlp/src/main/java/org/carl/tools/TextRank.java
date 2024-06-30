package org.carl.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import com.hankcs.hanlp.HanLP;

public class TextRank {
    public static String generateSummaryByTextRank(String content) {
        List<String> sentenceList = splitSentence(content);
        int sentenceCount = sentenceList.size();

        double[] scores = new double[sentenceCount];
        Arrays.fill(scores, 1.0);
        for (int i = 0; i < 10; i++) {
            double[] tempScores = Arrays.copyOf(scores, scores.length);
            for (int j = 0; j < sentenceCount; j++) {
                double score = 0.0;
                for (int k = 0; k < sentenceCount; k++) {
                    if (k != j) {
                        score += similarity(sentenceList.get(j), sentenceList.get(k));
                    }
                }
                tempScores[j] = 0.15 + 0.85 * score;
            }
            scores = tempScores;
        }

        int summarySize = Math.max(sentenceCount / 10, 1);
        PriorityQueue<Sentence> queue = new PriorityQueue<>(summarySize);
        for (int i = 0; i < sentenceCount; i++) {
            queue.offer(new Sentence(scores[i], i));
        }
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < summarySize; i++) {
            indexList.add(Objects.requireNonNull(queue.poll()).index);
        }
        Collections.sort(indexList);
        StringBuilder summary = new StringBuilder();
        for (Integer index : indexList) {
            summary.append(sentenceList.get(index)).append("。");
        }
        return summary.toString();
    }

    private static List<String> splitSentence(String text) {
        List<String> sentenceList = new ArrayList<>();
        String[] sentences = text.split("[\\n。？！；]");
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.length() > 0) {
                sentenceList.add(sentence);
            }
        }
        return sentenceList;
    }

    private static double similarity(String sentence1, String sentence2) {
        List<String> words1 = HanLP.segment(sentence1).stream().map(term -> term.word)
            .collect(Collectors.toList());
        List<String> words2 = HanLP.segment(sentence2).stream().map(term -> term.word)
            .collect(Collectors.toList());
        int intersection = CollectionUtils.intersection(words1, words2).size();
        return intersection / Math.sqrt(words1.size() * words2.size());
    }

    private static class Sentence implements Comparable<Sentence> {
        double score;
        int index;

        public Sentence(double score, int index) {
            this.score = score;
            this.index = index;
        }

        @Override
        public int compareTo(Sentence o) {
            return Double.compare(o.score, score);
        }
    }
}