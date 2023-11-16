package by.levitsky.spring.dictionaryservice.service.impl;

import by.levitsky.spring.dictionaryservice.service.TextParserService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TextParserServiceImpl implements TextParserService {

    @Override
    public Map<String, Integer> parseText(Map<String, Integer> wordsFreq, String path) {
        Pattern pattern = Pattern.compile("[^\\w]");
        List<String> words = new ArrayList<>();

        try (Scanner sc = new Scanner(new FileReader(path))) {
            while (sc.hasNextLine()) {
                words.addAll(Arrays.asList(pattern.split(sc.nextLine())));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String word : words) {
            String w = word.replaceAll("\\d", "");
            if (w.trim().length() != 0) {
                if (!wordsFreq.containsKey(w.toLowerCase()) || wordsFreq.get(w.toLowerCase()) == 0) {
                    wordsFreq.put(w.toLowerCase(), 1);
                } else {
                    int frequency = wordsFreq.get(w.toLowerCase());
                    wordsFreq.put(w.toLowerCase(), frequency + 1);
                }
            }
        }
        return wordsFreq;
    }
}
