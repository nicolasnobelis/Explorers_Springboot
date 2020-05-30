package nino.explorers.dao.couchdb;

import model.Expedition;
import model.enums.ExpeditionStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class CouchExpeditionDao implements dao.ExpeditionDao {
    private static final Logger logger = LoggerFactory.getLogger( CouchExpeditionDao.class );
    private final CouchClient couchClient;

    public CouchExpeditionDao(CouchClient couchClient) {
        this.couchClient = couchClient;
    }

    @NotNull
    @Override
    public UUID createOrUpdateExpedition(@NotNull Expedition expedition) {
        if ( expedition.getId() == null ) {
            expedition.setId( UUID.randomUUID() );
            return couchClient.putExpedition( expedition );
        } else {
            return couchClient.updateExpedition( expedition );
        }
        // todo UNIT TESTS !!!
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
