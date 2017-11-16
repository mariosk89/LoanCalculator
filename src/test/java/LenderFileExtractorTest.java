import model.Lender;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LenderFileExtractorTest
{
    @Test
    public void extractLendersTest()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("market.csv").getFile());

        List<Lender> lenders = LenderFileExtractor.extractLenders(file);

        Assert.assertEquals("Wrong number of lenders extracted",2,lenders.size());
        Assert.assertEquals("Wrong model.Lender item extracted", new Lender("Bob",0.075,640), lenders.get(0));
        Assert.assertEquals("Wrong model.Lender item extracted", new Lender("Jane", 0.069, 480), lenders.get(1));
    }

    @Test
    public void extractLendersWithInvalidValuesTest()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("marketInvalidValues.csv").getFile());

        List<Lender> lenders = LenderFileExtractor.extractLenders(file);

        Assert.assertEquals("Wrong number of lenders extracted",1,lenders.size());
        Assert.assertEquals("Wrong model.Lender item extracted", new Lender("Jane", 0.069, 480), lenders.get(0));
    }

    @Test
    public void extractLendersWithMissingValuesTest()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("marketMissingValues.csv").getFile());

        List<Lender> lenders = LenderFileExtractor.extractLenders(file);

        Assert.assertEquals("Wrong number of lenders extracted",1,lenders.size());
        Assert.assertEquals("Wrong model.Lender item extracted", new Lender("Jane", 0.069, 480), lenders.get(0));
    }

    @Test
    public void extractLendersFromEmptyFile()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("empty.csv").getFile());

        List<Lender> lenders = LenderFileExtractor.extractLenders(file);

        Assert.assertTrue("Expected an empty Lenders list", lenders.isEmpty());
    }
}
