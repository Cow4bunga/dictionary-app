package by.levitsky.spring.dictionaryservice.repository;

import by.levitsky.spring.dictionaryservice.model.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EnglishRepository extends JpaRepository<EnglishWord,Long> {
    boolean existsByWord(String word);
    EnglishWord getByWord(String word);
    @Transactional
    void deleteByWord(String word);
}
