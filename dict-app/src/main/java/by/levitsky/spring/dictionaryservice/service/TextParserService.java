package by.levitsky.spring.dictionaryservice.service;

import java.util.Map;

public interface TextParserService {
    Map<String, Integer> parseText(Map<String, Integer> wordsFreq, String path);
}
