package com.github.quaoz;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class Main {
    public static void main(String[] args) {
        System.out.println(FuzzySearch.weightedRatio("The quick brown fox jimps ofver the small lazy dog", "the quick brown fox jumps over the small lazy dog"));
        System.out.println(FuzzySearch.ratio("The quick brown fox jimps ofver the small lazy dog", "the quick brown fox jumps over the small lazy dog"));
        System.out.println(FuzzySearch.weightedRatio("fetgerd gofic", "feathered gothic"));
        System.out.println(FuzzySearch.ratio("fetgerd gofic", "feathered gothic"));
    }
}