package com.github.kingwaggs.csmanager.domain.result;

import com.github.kingwaggs.csmanager.domain.dto.InquiryMessage;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
public class InquiryResultDto implements CsManagerResultDto {

    private List<InquiryMessage> inquiries;
    private LocalDateTime dateTime;

    @Override
    public List<InquiryMessage> getResult() {
        return this.inquiries;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

}
