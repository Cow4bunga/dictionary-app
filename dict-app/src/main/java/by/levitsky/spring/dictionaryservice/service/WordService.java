package by.levitsky.spring.dictionaryservice.service;

import by.levitsky.spring.dictionaryservice.model.EnglishWord;

import java.util.Optional;

public interface WordService {
    void addWord(EnglishWord word);
    public Iterable<EnglishWord> getAllWords();

    boolean updateWord(long id, EnglishWord newWord);

    Iterable<EnglishWord> getWordByRegex(String regex);
    Optional<EnglishWord> getWordById(long id);

    Iterable<EnglishWord> getSortedByAlphabet();
    Iterable<EnglishWord> getSortedByFrequency();

    Iterable<EnglishWord> getSortedInAlphReverse();
    Iterable<EnglishWord> getSortedByFreqReverse();

    void parseWords(String path);

    void deleteWordById(long id);

}
