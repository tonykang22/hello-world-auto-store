package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productanalyzer.domain.dto.response.PapagoTextResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PapagoTextTranslatorTest {

    @InjectMocks
    private PapagoTextTranslator papagoTextTranslator;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private static String text;
    private static String source;
    private static String target;


    @BeforeAll
    static void init() {
        text = "word";
        source = "en";
        target = "ko";
    }

    @Test
    @DisplayName("번역에 성공 시")
    void translateSuccessfully() throws JsonProcessingException {
        // given
        String translatedText = "단어";

        PapagoTextResponseDto papagoTextResponse = mock(PapagoTextResponseDto.class);
        PapagoTextResponseDto.Message message = mock(PapagoTextResponseDto.Message.class);
        PapagoTextResponseDto.Result result = mock(PapagoTextResponseDto.Result.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(translatedText, HttpStatus.OK);
        String body = responseEntity.getBody();

        when(objectMapper.writeValueAsString(any()))
                .thenReturn(translatedText);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(responseEntity);
        when(objectMapper.readValue(eq(responseEntity.getBody()), (Class<PapagoTextResponseDto>) any()))
                .thenReturn(papagoTextResponse);

        when(papagoTextResponse.getMessage()).thenReturn(message);
        when(message.getResult()).thenReturn(result);
        when(result.getTranslatedText()).thenReturn(translatedText);

        // when
        String response = papagoTextTranslator.translateText(text, source, target);

        // then
        assertEquals("단어", response);
    }

    @Test
    @DisplayName("번역에 성공 시 - 4회 실패 후 성공")
    void translateSuccessfullyAfterFourError() throws JsonProcessingException {
        // given
        String translatedText = "단어";

        PapagoTextResponseDto papagoTextResponse = mock(PapagoTextResponseDto.class);
        PapagoTextResponseDto.Message message = mock(PapagoTextResponseDto.Message.class);
        PapagoTextResponseDto.Result result = mock(PapagoTextResponseDto.Result.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(translatedText, HttpStatus.OK);
        String body = responseEntity.getBody();

        when(objectMapper.writeValueAsString(any())).thenThrow(IllegalStateException.class)
                .thenThrow(IllegalStateException.class)
                .thenThrow(IllegalStateException.class)
                .thenThrow(IllegalStateException.class)
                .thenReturn(translatedText);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(responseEntity);
        when(objectMapper.readValue(eq(responseEntity.getBody()), (Class<PapagoTextResponseDto>) any()))
                .thenReturn(papagoTextResponse);

        when(papagoTextResponse.getMessage()).thenReturn(message);
        when(message.getResult()).thenReturn(result);
        when(result.getTranslatedText()).thenReturn(translatedText);

        // when
        String response = papagoTextTranslator.translateText(text, source, target);

        // then
        assertEquals("단어", response);
    }

    @Test
    @DisplayName("번역에 실패 시")
    void translateWithFailure() throws JsonProcessingException {
        // given
        when(objectMapper.writeValueAsString(any())).thenThrow(IllegalStateException.class);

        // when
        String response = papagoTextTranslator.translateText(text, source, target);

        // then
        assertNull(response);
    }
}