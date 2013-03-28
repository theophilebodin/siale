// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.List;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.domain.DroitDataOnDemand;
import nc.mairie.siale.domain.DroitIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DroitIntegrationTest_Roo_IntegrationTest {
    
    declare @type: DroitIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: DroitIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: DroitIntegrationTest: @Transactional;
    
    @Autowired
    private DroitDataOnDemand DroitIntegrationTest.dod;
    
    @Test
    public void DroitIntegrationTest.testCountDroits() {
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", dod.getRandomDroit());
        long count = Droit.countDroits();
        Assert.assertTrue("Counter for 'Droit' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void DroitIntegrationTest.testFindDroit() {
        Droit obj = dod.getRandomDroit();
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Droit' failed to provide an identifier", id);
        obj = Droit.findDroit(id);
        Assert.assertNotNull("Find method for 'Droit' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Droit' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void DroitIntegrationTest.testFindAllDroits() {
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", dod.getRandomDroit());
        long count = Droit.countDroits();
        Assert.assertTrue("Too expensive to perform a find all test for 'Droit', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Droit> result = Droit.findAllDroits();
        Assert.assertNotNull("Find all method for 'Droit' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Droit' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void DroitIntegrationTest.testFindDroitEntries() {
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", dod.getRandomDroit());
        long count = Droit.countDroits();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Droit> result = Droit.findDroitEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Droit' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Droit' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void DroitIntegrationTest.testFlush() {
        Droit obj = dod.getRandomDroit();
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Droit' failed to provide an identifier", id);
        obj = Droit.findDroit(id);
        Assert.assertNotNull("Find method for 'Droit' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDroit(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Droit' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DroitIntegrationTest.testMergeUpdate() {
        Droit obj = dod.getRandomDroit();
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Droit' failed to provide an identifier", id);
        obj = Droit.findDroit(id);
        boolean modified =  dod.modifyDroit(obj);
        Integer currentVersion = obj.getVersion();
        Droit merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Droit' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DroitIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", dod.getRandomDroit());
        Droit obj = dod.getNewTransientDroit(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Droit' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Droit' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Droit' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void DroitIntegrationTest.testRemove() {
        Droit obj = dod.getRandomDroit();
        Assert.assertNotNull("Data on demand for 'Droit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Droit' failed to provide an identifier", id);
        obj = Droit.findDroit(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Droit' with identifier '" + id + "'", Droit.findDroit(id));
    }
    
}
