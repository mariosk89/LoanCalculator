import model.Lender;
import model.Quote;
import org.junit.*;

import java.io.File;
import java.util.*;

public class CompoundInterestQuoteCalculatorTest
{
    private static QuoteCalculator quoteCalculator;

    @BeforeClass
    public static void initialize()
    {
        quoteCalculator = new CompoundInterestQuoteCalculator();
    }

    @Test
    public void emptyQuoteOnHighLoanRequestTest()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("market.csv").getFile());
        List<Lender> lenders = LenderFileExtractor.extractLenders(file);

        Optional<Quote> quote = quoteCalculator.calculateQuoteRate(1200,lenders, 36, 12);

        Assert.assertFalse("Expected an empty quote as the requested amount can't be served",quote.isPresent());
    }

    @Test
    public void calculateAmountsPerRateFromAllLendersTest()
    {
        //Testing that the amount is covered
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("A",7.0, 300));
        lenders.add(new Lender("B",8.0, 300));
        lenders.add(new Lender("C",8.0, 300));
        lenders.add(new Lender("D",9.0, 100));

        Map<Double,Integer> amountsPerRate = quoteCalculator.calculateAmountsPerRate(1000, lenders);

        Assert.assertEquals("Wrong size of returned amounts per rate map",3, amountsPerRate.size());
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(7.0));
        Assert.assertEquals("Wrong amount for this rate",300.0, amountsPerRate.get(7.0), 0.0);
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(8.0));
        Assert.assertEquals("Wrong amount for this rate",600.0, amountsPerRate.get(8.0), 0.0);
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(9.0));
        Assert.assertEquals("Wrong amount for this rate",100.0, amountsPerRate.get(9.0), 0.0);
    }

    @Test
    public void calculateAmountsPerRateFromAllLendersPartialTest()
    {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("A",7.0, 300));
        lenders.add(new Lender("B",8.0, 600));
        lenders.add(new Lender("C",8.0, 300));

        Map<Double,Integer> amountsPerRate = quoteCalculator.calculateAmountsPerRate(1000, lenders);

        Assert.assertEquals("Wrong size of returned amounts per rate map",2, amountsPerRate.size());
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(7.0));
        Assert.assertEquals("Wrong amount for this rate",300.0, amountsPerRate.get(7.0), 0.0);
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(8.0));
        Assert.assertEquals("Wrong amount for this rate",700.0, amountsPerRate.get(8.0), 0.0);
        Assert.assertEquals("Wrong number of remaining funds for Lender C", 200.0, lenders.get(2).getAvailable(), 0.0);

    }

    @Test
    public void calculateAmountsPerRateGetLowerRateTest()
    {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("A",7.0, 300));
        lenders.add(new Lender("B",8.0, 600));
        lenders.add(new Lender("C",9.0, 100));
        lenders.add(new Lender("C",10.0, 100));

        Map<Double,Integer> amountsPerRate = quoteCalculator.calculateAmountsPerRate(1000, lenders);

        Assert.assertEquals("Wrong size of returned amounts per rate map",3, amountsPerRate.size());
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(7.0));
        Assert.assertEquals("Wrong amount for this rate",300.0, amountsPerRate.get(7.0), 0.0);
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(8.0));
        Assert.assertEquals("Wrong amount for this rate",600.0, amountsPerRate.get(8.0), 0.0);
        Assert.assertNotNull("Missing rate entry from expected map", amountsPerRate.get(9.0));
        Assert.assertEquals("Wrong amount for this rate",100.0, amountsPerRate.get(9.0), 0.0);
    }

}
