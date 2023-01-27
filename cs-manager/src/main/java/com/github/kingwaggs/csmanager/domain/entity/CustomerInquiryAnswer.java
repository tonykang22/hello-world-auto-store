package com.github.kingwaggs.csmanager.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInquiryAnswer {

    @Column(name = "answer_content")
    private String answerContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "answered_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime answeredAt;

}
