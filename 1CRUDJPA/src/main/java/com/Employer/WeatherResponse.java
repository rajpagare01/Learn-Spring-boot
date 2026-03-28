package com.Employer;
import lombok.Data;

import java.util.ArrayList;
@Data
public class WeatherResponse{

    private Current current;

     @Data
    public class Current{

        public int temperature;


        public ArrayList<String> weather_descriptions;

        public int feelslike;

    }

}

