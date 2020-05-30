package nino.explorers.dao.couchdb;

import model.Expedition;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CouchClient implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger( CouchClient.class );

    @Value("${dbServerURL}")
    private String dbServerURL;

    private RestTemplateBuilder restTemplateBuilder;

    private Map<UUID, String> revisionCache = new HashMap<>();

    public CouchClient(RestTemplateBuilder restTemplateBuilder) {
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

    public UUID putExpedition(Expedition expedition) {
        ExpeditionDocument document = new ExpeditionDocument();
        document.set_id( expedition.getId() );
        document.setCreationDate( LocalDateTime.now() );
        document.setExpedition( expedition );
        ResponseEntity<CouchResult> response = restTemplateBuilder.build()
                .postForEntity( "/expeditions", document, CouchResult.class );
        if ( response.getStatusCode().isError() || response.getBody() == null ) {
            logger.error( "Unknown error in put document" );
            logger.error( response.toString() );
            throw new RestClientException( "Unknown error in put document" );
        } else {
            logger.info( "Successfully created " + response );
            revisionCache.put( document.get_id(), response.getBody().getRev() );
            return UUID.fromString( response.getBody().getId() );
        }
    }

    public UUID updateExpedition(Expedition expedition) {
        if ( expedition.getId() == null ) {
            throw new NullPointerException( "Need an expedition id !" );
        }
        String revision = revisionCache.get( expedition.getId() );
        if ( revision == null ) {
            ExpeditionDocument document = getExpedition( expedition );
            revision = document.get_rev();
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath( "/expeditions/" )
                .pathSegment( expedition.getId().toString() )
                .queryParam( "rev", revision );
        ResponseEntity<CouchResult> response = restTemplateBuilder.build()
                .exchange( uriBuilder.build().toUriString(), HttpMethod.PUT, new HttpEntity<>( expedition ), CouchResult.class );
        if ( response.getStatusCode().isError() || response.getBody() == null ) {
            logger.error( "Unknown error in update document" );
            logger.error( response.toString() );
            throw new RestClientException( "Unknown error in update document" );
        } else {
            logger.info( "Successfully updated " + response );
            revisionCache.put( expedition.getId(), response.getBody().getRev() );
            return UUID.fromString( response.getBody().getId() );
        }
    }

    private ExpeditionDocument getExpedition(@NotNull Expedition expedition) {
        if ( expedition.getId() == null ) {
            throw new NullPointerException( "Need an expedition id !" );
        }
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath( "/expeditions/" )
                .pathSegment( expedition.getId().toString() );
        ResponseEntity<ExpeditionDocument> response = restTemplateBuilder.build()
                .getForEntity( uriBuilder.build().toUriString(), ExpeditionDocument.class );
        if ( response.getStatusCode().isError() || response.getBody() == null ) {
            logger.error( "Unknown error in get document" );
            logger.error( response.toString() );
            throw new RestClientException( "Unknown error in get document" );
        } else {
            logger.info( "Successfully get " + response );
            revisionCache.put( expedition.getId(), response.getBody().get_rev() );
            return response.getBody();
        }
    }
}
