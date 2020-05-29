package nino.explorers.dao.couchdb;

import model.Expedition;
import model.enums.ExpeditionStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CouchExpeditionDao implements dao.ExpeditionDao, InitializingBean {
    @Value("${dbServerURL}")
    private String dbServerURL;

    private RestTemplateBuilder restTemplateBuilder;

    private static final Logger logger = LoggerFactory.getLogger( CouchExpeditionDao.class );

    public CouchExpeditionDao(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public void afterPropertiesSet() {
        restTemplateBuilder = restTemplateBuilder
                .rootUri( dbServerURL )
                .basicAuthentication( "admin", "admin" );
        checkDB();
    }

    private void checkDB() {
        ResponseEntity<String> entity = restTemplateBuilder
                .errorHandler( new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(@NotNull ClientHttpResponse response) throws IOException {
                        return response.getStatusCode() == HttpStatus.NOT_FOUND;
                    }

                    @Override
                    public void handleError(@NotNull ClientHttpResponse response) {
                        logger.warn( "Expedition table missing" );
                        createDb();
                    }
                } ).build().getForEntity( "/expeditions", String.class );
        if ( entity.getStatusCode() == HttpStatus.OK ) {
            logger.info( "Expedition table exists" );
        }
        if ( entity.getStatusCode().isError() && entity.getStatusCode() != HttpStatus.NOT_FOUND ) {
            logger.error( "Unknown error in table check" );
            logger.error( entity.toString() );
        }

    }

    private void createDb() {
        restTemplateBuilder.build()
                .put( "/expeditions", null );
        logger.info( "Expedition table created" );
    }

    @NotNull
    @Override
    public UUID createOrUpdateExpedition(@NotNull Expedition expedition) {
        if ( expedition.getId() == null ) {
            ResponseEntity<CouchResult> response = restTemplateBuilder.build()
                    .postForEntity( "/expeditions", expedition, CouchResult.class );
            if (response.getStatusCode().isError() || response.getBody() == null) {
                logger.error( "Unknown error in table check" );
                logger.error( response.toString() );
            } else {
                logger.info( "Successfully created " + response );
                return UUID.fromString( response.getBody().getId() );
            }
        } else {
            // TODO _id id problem
        }

        return expedition.getId();
    }

    @Override
    public boolean deleteExpedition(@NotNull UUID uuid) {
        return false;
    }

    @Nullable
    @Override
    public Expedition getExpedition(@NotNull UUID uuid) {
        return null;
    }

    @NotNull
    @Override
    public List<Expedition> listExpeditions() {
        return null;
    }

    @NotNull
    @Override
    public List<Expedition> listExpeditions(@NotNull ExpeditionStatus expeditionStatus) {
        return null;
    }
}
