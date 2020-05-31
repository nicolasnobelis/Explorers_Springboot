package nino.explorers.dao.couchdb;

import model.Expedition;
import model.enums.Country;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.springframework.util.Assert.notNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "/application.properties")
class CouchExpeditionDaoTest {
    @Autowired
    ApplicationContext context;

    @Test
    void createOrUpdateExpedition() {
        CouchExpeditionDao couchExpeditionDao = context.getBean( "couchExpeditionDao", CouchExpeditionDao.class );
        Expedition expedition = new Expedition();
        expedition.setName( "Test Expedition" );
        expedition.setCountry( Country.SPAIN );

        UUID storedException = couchExpeditionDao.createOrUpdateExpedition( expedition );
        notNull(storedException, "no id");
    }

    @Test
    void deleteExpedition() {
    }

    @Test
    void getExpedition() {
    }

    @Test
    void listExpeditions() {
    }

    @Test
    void testListExpeditions() {
    }
}