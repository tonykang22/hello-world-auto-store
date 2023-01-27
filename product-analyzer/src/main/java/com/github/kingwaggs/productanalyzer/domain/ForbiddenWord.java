package com.github.kingwaggs.productanalyzer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forbidden_word")
@Getter @Setter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@DynamicUpdate
@AllArgsConstructor @NoArgsConstructor
public class ForbiddenWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "word", nullable = false)
    private String word;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "registration_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime registrationDate;

    public static ForbiddenWord create(String word) {
        return ForbiddenWord.builder()
                .word(word)
                .registrationDate(LocalDateTime.now())
                .build();
    }

}
