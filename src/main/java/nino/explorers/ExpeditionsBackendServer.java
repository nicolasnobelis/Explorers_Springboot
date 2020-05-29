package nino.explorers;

import nino.explorers.dao.couchdb.CouchExpeditionDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpeditionsBackendServer {
    @Bean
    public CouchExpeditionDao couchExpeditionDao(RestTemplateBuilder restTemplateBuilder) {
        return new CouchExpeditionDao(restTemplateBuilder);
    }
    public static void main(String[] args) {
        SpringApplication.run( ExpeditionsBackendServer.class, args );
    }
}
