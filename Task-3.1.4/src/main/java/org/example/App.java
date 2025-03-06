package org.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class App
{
    static RestTemplate restTemplate = new RestTemplate();
    static String url = "http://94.198.50.185:7081/api/users";

    public static void main( String[] args )
    {
        String sessionId = getAllUsers();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);

        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte)30);

        createUser(newUser, headers);

        newUser.setName("Thomas");
        newUser.setLastName("Shelby");

        updateUser(newUser, headers);
        deleteUser(3L, headers);
    }

    static public String getAllUsers(){
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String sessionId = response.getHeaders().getFirst("Set-Cookie").split(";")[0];
        System.out.println(sessionId);

        return sessionId;
    }

    static public void createUser(User user, HttpHeaders headers){
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());
    }

    static public void updateUser(User user, HttpHeaders headers){
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        System.out.println(response.getBody());
    }

    static public void deleteUser(long id, HttpHeaders headers){
        HttpEntity<User> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, request, String.class);
        System.out.println(response.getBody());
    }
}
