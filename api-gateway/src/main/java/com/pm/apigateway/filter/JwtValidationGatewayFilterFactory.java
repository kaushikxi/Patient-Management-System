package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// A filter class is a custom class that allows us to intercept http request, apply custom logic, and
// then decide whether to process the request or to cancel the request

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl){ // we're using an env variable for authServiceUrl because it depends on the environment we're working in
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {

        return(exchange, chain) -> {// exchange is an object that is passed to us by spring gateway that holds all properties for current request, chain variable is a variable that manages the chain of filters that exists in the filter chain
            String token =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION); // we're getting the authorization header from the request

            if(token == null || !token.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete(); // this returns early from our filter and return the response to the client
            }

            return webClient.get()
                    .uri("/validate")// performing GET request to the uri /validate using webClient
                    .header(HttpHeaders.AUTHORIZATION,token)// we are taking the authorization header from the initial request and we are setting it on this request to the validation endpoint
                    .retrieve()// and we retrieve the response
                    .toBodilessEntity()//we tell the webClient that there is no body in the response
                    .then(chain.filter(exchange)); // if everything goes well, then this method gets called; chain.filter -> our filter finished processing the request, all was successful and that we are happy for the request to continue down the chain

        };
    }


}
