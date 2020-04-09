package com.lixq.epark.voice.service;

import com.winnerlook.model.VoiceResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VoiceServiceTest {
    @Autowired
    private VoiceService voiceService;


    @Test
    void bindWithAx() {
        VoiceResponseResult result = voiceService.bindWithAx("13020248427", "15210352037");
        assertEquals(result.getResult(), "000000");
    }

    @Test
    void unBindWithAx() {
        VoiceResponseResult result = voiceService.unBindWithAx("13020248427", "15210352037");
        assertEquals(result.getResult(), "000000");
    }
}