package by.levitsky.spring.dictionaryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "english_word")
public class EnglishWord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "count", nullable = false)
    private Integer count=0;

    @Column(name = "frequency", nullable = false)
    private Double frequency = 0.;
}
