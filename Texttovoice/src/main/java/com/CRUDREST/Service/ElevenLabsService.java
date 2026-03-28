package com.CRUDREST.Service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElevenLabsService {

    @Value("${elevenlabs.api.key}")
    private  String API_KEY;
    @Value("${elevenlabs.voice.id}")
    private String voiceId;



    public String getModels() {

        String url = "https://api.elevenlabs.io/v1/models";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("xi-api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        return response.getBody();
    }



    public byte[] generateSpeech(String text) {

        String url = "https://api.elevenlabs.io/v1/text-to-speech/" + voiceId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("xi-api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
    {
        "text": "%s",
        "model_id": "eleven_flash_v2_5"
    }
    """.formatted(text);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                byte[].class
        );

        return response.getBody();
    }
}