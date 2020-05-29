package nino.explorers;

import model.Expedition;
import model.enums.ExpeditionStatus;
import nino.explorers.dao.couchdb.CouchExpeditionDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest")
public class ExpeditionService implements rest.ExpeditionService {
    private final CouchExpeditionDao couchExpeditionDao;

    public ExpeditionService(CouchExpeditionDao couchExpeditionDao) {
        this.couchExpeditionDao = couchExpeditionDao;
    }

    @Override
    public void abortExpedition(@NotNull UUID uuid, int i) {

    }

    @Override
    public void assignExplorer(@NotNull UUID uuid, @NotNull UUID uuid1) {

    }

    @Override
    public void assignShip(@NotNull UUID uuid, @NotNull UUID uuid1) {

    }

    @NotNull
    @Override
    @RequestMapping(path = "/expedition/createOrUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UUID createOrUpdateExpedition(@NotNull @RequestBody Expedition expedition) {
        return couchExpeditionDao.createOrUpdateExpedition( expedition );
    }

    @Override
    public void deleteExpedition(@NotNull UUID uuid) {

    }

    @NotNull
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

    @Override
    public void startExpedition(@NotNull UUID uuid) {

    }
}
