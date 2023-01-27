package com.github.kingwaggs.csmanager.controller;

import com.github.kingwaggs.csmanager.domain.CommonResponse;
import com.github.kingwaggs.csmanager.domain.dto.InquiryAnswer;
import com.github.kingwaggs.csmanager.domain.result.CsManagerResultDto;
import com.github.kingwaggs.csmanager.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/${module.name}")
@RequiredArgsConstructor
public class CsManagerController {

    private static final String SUCCESS_BODY_RESPONSE_MESSAGE = "Request sent successfully.";

    private final InquiryService inquiryService;

    @GetMapping("/inquiries")
    public ResponseEntity<CommonResponse> getInquiries() {
        CsManagerResultDto resultDto = inquiryService.getInquiries();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, resultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/inquiries/{inquiryId}/answer")
    public ResponseEntity<CommonResponse> postInquiryAnswer(@PathVariable String inquiryId,
                                                            @RequestBody InquiryAnswer inquiryAnswer) {
        inquiryService.answerInquiry(inquiryId, inquiryAnswer.getContent());
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, SUCCESS_BODY_RESPONSE_MESSAGE);
        return ResponseEntity.ok(commonResponse);

    }

}
