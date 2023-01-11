package com.timeservice.oauth2democlient.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


/**
 * This controller retrieves the time via a REST interface.
 */
@Controller
public class TimeController {

  /**
   * Gateway method to access service functionality.
   */
  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public ModelAndView getTime() {
    ModelAndView m = new ModelAndView("getTime");

    return m;
  }


  /**
   * Method to access the time service with the appropriate credentials and retrieve the time.
   *
   * @param code authorisation code obtained from the authorisation server
   */

  @RequestMapping(value = "/showTime", method = RequestMethod.GET)
  public ModelAndView showTime(@RequestParam("code") String code) throws JsonProcessingException {

    System.out.println("Authorization Code------" + code);
    String credentials = "timeservice-client:password-for-timeservice-client";
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Authorization", "Basic " + encodedCredentials);


    String accessTokenUrl = "http://localhost:8080/oauth/token";
    accessTokenUrl += "?code=" + code;
    accessTokenUrl += "&grant_type=authorization_code";
    accessTokenUrl += "&redirect_uri=http://localhost:8090/showTime";

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> request = new HttpEntity<String>(headers);
    ResponseEntity<String> response = restTemplate
        .exchange(accessTokenUrl, HttpMethod.POST, request, String.class);

    System.out.println("Access Token Response ---------" + response.getBody());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(response.getBody());
    String token = node.path("access_token").asText();

    String url = "http://localhost:8084/api/time";

    HttpHeaders headers1 = new HttpHeaders();
    headers1.add("Authorization", "Bearer" + token);
    HttpEntity<String> entity = new HttpEntity<>(headers1);

    ResponseEntity<String> r = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    System.out.println("Time is " + r.getBody());

    ModelAndView m = new ModelAndView("displayTime");
    m.addObject("time", r.getBody());


    return m;














  }
}
