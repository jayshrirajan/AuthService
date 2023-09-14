package com.msys.authservice.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msys.authservice.config.WebClientConfig;
import com.msys.authservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service

public class KeyCloakUtilService {

    @Autowired
    private WebClientConfig webClientConfig;

    @Value("${services.authService.baseUrl}")
    private String baseUrl;

    @Value("${services.authService.keycloakBaseUrl}")
    private String keycloakBaseUrl;

    @Value("${services.authService.realmsName}")
    private String realmsName;

    @Value("${keycloak.admin.username}")
    private String userName;

    @Value("${keycloak.admin.password}")
    private String password;

    @Value("${keycloak.admin.client_id}")
    private String clientId;

    @Value("${keycloak.admin.grant_type}")
    private String grantType;

    @Value("${keycloak.admin.tokenUrl}")
    private String tokenUrl;


    public Mono<String> getAccessToken() {
        try {
            log.info("start get access token"+baseUrl+tokenUrl);
            return webClientConfig.webClientAuthService().post().uri(baseUrl + tokenUrl).body(
                            BodyInserters.fromFormData("username", userName)
                                    .with("password", password)
                                    .with("grant_type", grantType)
                                    .with("client_id", clientId)
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(res -> {
                        try {
                            JsonParser parser = new ObjectMapper().getFactory().createParser(res);
                            while (!parser.isClosed()) {
                                JsonToken token = parser.nextToken();
                                if (JsonToken.FIELD_NAME.equals(token) && "access_token".equals(parser.getCurrentName())) {
                                    parser.nextToken();
                                    log.info("end get access token");
                                    return parser.getValueAsString();
                                }
                            }
                            throw new RuntimeException();

                        } catch (JsonParseException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        catch (Exception e)
        {
            log.error("Error in get access token - {}"+e);
            return Mono.error(e);
        }
    }


    public String getIdByUserName(String username) throws Exception
    {
        try {
            log.info("getIdByUserName"+keycloakBaseUrl + realmsName + "/users");
            Flux<UserDto> response = webClientConfig.webClientAuthService().get().uri(u -> u.path(keycloakBaseUrl + realmsName + "/users")
                            .queryParam("username", username)
                            .queryParam("exact", true).build())
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(this.getAccessToken().block()))
                    .retrieve().bodyToFlux(UserDto.class);
            return response.map(resp -> resp.getId()).blockFirst();

        }
        catch (Exception e)
        {
            log.info("error::: "+e);
            log.error("getIdByUserName - username {}, error: {}",username,e);
            throw new RuntimeException(e.getMessage());
        }
    }
}


