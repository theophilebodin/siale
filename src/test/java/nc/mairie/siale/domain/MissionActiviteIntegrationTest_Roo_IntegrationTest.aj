// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.List;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.MissionActiviteDataOnDemand;
import nc.mairie.siale.domain.MissionActiviteIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MissionActiviteIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MissionActiviteIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MissionActiviteIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: MissionActiviteIntegrationTest: @Transactional;
    
    @Autowired
    private MissionActiviteDataOnDemand MissionActiviteIntegrationTest.dod;
    
    @Test
    public void MissionActiviteIntegrationTest.testCountMissionActivites() {
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", dod.getRandomMissionActivite());
        long count = MissionActivite.countMissionActivites();
        Assert.assertTrue("Counter for 'MissionActivite' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testFindMissionActivite() {
        MissionActivite obj = dod.getRandomMissionActivite();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to provide an identifier", id);
        obj = MissionActivite.findMissionActivite(id);
        Assert.assertNotNull("Find method for 'MissionActivite' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MissionActivite' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testFindAllMissionActivites() {
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", dod.getRandomMissionActivite());
        long count = MissionActivite.countMissionActivites();
        Assert.assertTrue("Too expensive to perform a find all test for 'MissionActivite', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MissionActivite> result = MissionActivite.findAllMissionActivites();
        Assert.assertNotNull("Find all method for 'MissionActivite' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MissionActivite' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testFindMissionActiviteEntries() {
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", dod.getRandomMissionActivite());
        long count = MissionActivite.countMissionActivites();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MissionActivite> result = MissionActivite.findMissionActiviteEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'MissionActivite' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MissionActivite' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testFlush() {
        MissionActivite obj = dod.getRandomMissionActivite();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to provide an identifier", id);
        obj = MissionActivite.findMissionActivite(id);
        Assert.assertNotNull("Find method for 'MissionActivite' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMissionActivite(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'MissionActivite' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testMergeUpdate() {
        MissionActivite obj = dod.getRandomMissionActivite();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to provide an identifier", id);
        obj = MissionActivite.findMissionActivite(id);
        boolean modified =  dod.modifyMissionActivite(obj);
        Integer currentVersion = obj.getVersion();
        MissionActivite merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MissionActivite' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", dod.getRandomMissionActivite());
        MissionActivite obj = dod.getNewTransientMissionActivite(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MissionActivite' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'MissionActivite' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void MissionActiviteIntegrationTest.testRemove() {
        MissionActivite obj = dod.getRandomMissionActivite();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MissionActivite' failed to provide an identifier", id);
        obj = MissionActivite.findMissionActivite(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'MissionActivite' with identifier '" + id + "'", MissionActivite.findMissionActivite(id));
    }
    
}
