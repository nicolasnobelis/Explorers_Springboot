package nino.explorers.dao.couchdb;

import com.fasterxml.jackson.annotation.JsonInclude;
import model.Expedition;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExpeditionDocument {
    private UUID _id;
    private String _rev;
    private LocalDateTime creationDate;
    private Expedition expedition;

    public UUID get_id() {
        return _id;
    }

    public void set_id(UUID _id) {
        this._id = _id;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Expedition getExpedition() {
        return expedition;
    }

    public void setExpedition(Expedition expedition) {
        this.expedition = expedition;
    }
}
