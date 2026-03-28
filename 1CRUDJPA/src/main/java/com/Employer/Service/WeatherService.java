package com.Employer.Service;

import com.Employer.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
private static final String apikey = "b8ce071ddda0e82bdd175d9cf65bd17a";
private static final String API = "https://api.weatherstack.com/forecast?access_key=API_KEY&query=CITY";

@Autowired
private RestTemplate restTemplate;


public WeatherResponse getWeather(String city)
{
    String finalapi = API.replace("CITY" , city).replace("API_KEY" , apikey );
    ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalapi, HttpMethod.GET, null, WeatherResponse.class);
    WeatherResponse body= response.getBody();
    return body;




}

}
