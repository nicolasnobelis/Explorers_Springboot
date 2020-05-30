package nino.explorers;

import nino.explorers.dao.couchdb.CouchClient;
import nino.explorers.dao.couchdb.CouchExpeditionDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpeditionsBackendServer {
    @Bean
    public CouchClient couchClient(RestTemplateBuilder restTemplateBuilder) {
        return new CouchClient( restTemplateBuilder );
    }

    @Bean
    public CouchExpeditionDao couchExpeditionDao(CouchClient couchClient) {
        return new CouchExpeditionDao( couchClient );
    }

    public static void main(String[] args) {
        SpringApplication.run( ExpeditionsBackendServer.class, args );
    }
}
