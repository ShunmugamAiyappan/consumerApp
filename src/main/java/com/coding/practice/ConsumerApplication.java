package com.coding.practice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ConsumerApplication {

  public static void main(String[] args) {
    ConsumerApplication consumerApplication = new ConsumerApplication();
    URL consumingUrl;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    int sum = 0;
    try {
      consumingUrl = new URL(resourceBundle.getString("producer.url"));
      HttpURLConnection connection = (HttpURLConnection) consumingUrl.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/json");

      if (connection.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
      }

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      StringBuffer content = new StringBuffer();
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line);
      }

      ObjectMapper mapper = new ObjectMapper();
      List<Map> jsonOutput = mapper.readValue(content.toString(), new TypeReference<List<Map>>() {
      });
      bufferedReader.close();
      for (int i = 0; i < jsonOutput.size(); i++) {
        List numbers = (List) (jsonOutput.get(i)).get("numbers");
        sum += consumerApplication.listTotal(numbers);
      }
      System.out.println("Total: " + sum);

    } catch (MalformedURLException e) {
      System.out.println("Mal formed url: " + e);
    } catch (IOException ex) {
      System.out.println("Exception calling get request: " + ex);
    }
  }

  int listTotal(List<Integer> list) {
    int sum = 0;
    for (int i : list) {
      sum += i;
    }
    return sum;
  }
}