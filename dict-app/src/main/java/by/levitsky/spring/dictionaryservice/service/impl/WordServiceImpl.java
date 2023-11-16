package by.levitsky.spring.dictionaryservice.service.impl;

import by.levitsky.spring.dictionaryservice.model.EnglishWord;
import by.levitsky.spring.dictionaryservice.repository.EnglishRepository;
import by.levitsky.spring.dictionaryservice.service.FileService;
import by.levitsky.spring.dictionaryservice.service.WordService;
import by.levitsky.spring.dictionaryservice.service.TextParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    EnglishRepository englishRepository;

    @Autowired
    TextParserService parserService = new TextParserServiceImpl();

    @Autowired
    FileService fileService;

    private Map<String, Integer> wordMap = new HashMap<>();

    @Override
    public void addWord(EnglishWord word) {
        if (!englishRepository.existsByWord(word.getWord().toLowerCase())) {
            wordMap.put(word.getWord().toLowerCase(), 0);
            word.setWord(word.getWord().toLowerCase());
            word.setCount(0);
            englishRepository.save(word);
        }
    }

    @Override
    public Iterable<EnglishWord> getAllWords() {
        return englishRepository.findAll();
    }


    @Override
    public boolean updateWord(long id, EnglishWord newWord) {
        if(!englishRepository.existsById(id)){
            return false;
        }

        int count=englishRepository.findById(id).get().getCount();
        double freq=englishRepository.findById(id).get().getFrequency();
        englishRepository.deleteById(id);
        EnglishWord updWord=new EnglishWord();

        if(englishRepository.existsByWord(newWord.getWord())){
            EnglishWord word=englishRepository.getByWord(newWord.getWord());
            updWord.setCount(word.getCount()+count);
            updWord.setFrequency(word.getFrequency()+freq);
            englishRepository.deleteByWord(newWord.getWord());
        }else{
            updWord.setCount(count);
            updWord.setFrequency(freq);
        }
        updWord.setWord(newWord.getWord());
        englishRepository.save(updWord);
        return true;
    }

    @Override
    public Iterable<EnglishWord> getWordByRegex(String regex) {
        Iterable<EnglishWord> words = englishRepository.findAll();
        List<EnglishWord> result = new ArrayList<>();
        for (EnglishWord word : words) {
            if (word.getWord().startsWith(regex)) {
                result.add(word);
            }
        }

        return result;
    }

    @Override
    public Optional<EnglishWord> getWordById(long id) {
        return englishRepository.findById(id);
    }

    @Override
    public Iterable<EnglishWord> getSortedByAlphabet() {
        return englishRepository.findAll(Sort.by(Sort.Direction.ASC, "word","frequency"));
    }

    @Override
    public Iterable<EnglishWord> getSortedByFrequency() {
        return englishRepository.findAll(Sort.by(Sort.Direction.ASC, "frequency", "word"));
    }

    @Override
    public Iterable<EnglishWord> getSortedInAlphReverse() {
        return englishRepository.findAll(Sort.by(Sort.Direction.DESC, "word", "frequency"));
    }

    @Override
    public Iterable<EnglishWord> getSortedByFreqReverse() {
        return englishRepository.findAll(Sort.by(Sort.Direction.DESC, "frequency", "word"));
    }

    @Override
    public void parseWords(String path) {
        wordMap = parserService.parseText(new HashMap<>(), path);
        Map<String, Integer> oldMap = getMap();

        for (Map.Entry<String, Integer> entry : oldMap.entrySet()) {
            if (wordMap.containsKey(entry.getKey())) {
                int count = wordMap.get(entry.getKey());
                wordMap.put(entry.getKey(), entry.getValue() + count);
            } else {
                wordMap.put(entry.getKey(), entry.getValue());
            }
        }

        int total = 0;
        for (int val : wordMap.values()) {
            total += val;
        }
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            if (englishRepository.existsByWord(entry.getKey())) {
                englishRepository.deleteByWord(entry.getKey());
            }
            double newFrequency = (entry.getValue() * 1.0) / total;
            EnglishWord word = new EnglishWord();
            word.setFrequency(newFrequency);
            word.setCount(entry.getValue());
            word.setWord(entry.getKey());
            englishRepository.save(word);
        }
    }

    @Override
    public void deleteWordById(long id) {
        if (englishRepository.existsById(id)) {
            String word = englishRepository.findById(id).get().getWord();
            wordMap.remove(word);
            englishRepository.deleteById(id);
            //recalculateFrequencies(getMap());
        }
    }

    public Map<String, Integer> getMap() {
        Iterable<EnglishWord> words = getSortedByFreqReverse();
        Map<String, Integer> wMap = new HashMap<>();
        for (EnglishWord word : words) {
            wMap.put(word.getWord(), word.getCount());
        }

        return wMap;
    }



    public void recalculateFrequencies(Map<String, Integer> map) {
        int total = 0;
        for (int val : map.values()) {
            total += val;
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            englishRepository.deleteByWord(entry.getKey());
            double newFrequency = (entry.getValue() * 1.0) / total;
            EnglishWord word = new EnglishWord();
            word.setFrequency(newFrequency);
            word.setCount(entry.getValue());
            word.setWord(entry.getKey());
            englishRepository.save(word);
        }
    }

    public void correctWords(){
        Iterable<EnglishWord> words=englishRepository.findAll();
        for(EnglishWord word:words){
            if(word.getWord().contains("[]")){
                EnglishWord newWord=new EnglishWord();
                newWord.setWord(word.getWord().replaceAll("”…",""));
                newWord.setCount(word.getCount());
                updateWord(word.getId(),newWord);
            }
        }
    }

}
