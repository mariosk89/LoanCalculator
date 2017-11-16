import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoanAmountValidatorImplTests
{
    private static LoanAmountValidator loanAmountValidator;

    @BeforeClass
    public static void initialize()
    {
        loanAmountValidator = new LoanAmountValidatorImpl();
    }

    @Test
    public void testNoSmallerThanAThousand()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(100);
        Assert.assertTrue("Expected 100 to be treated as a valid loan amount",valid);
    }

    @Test
    public void testSmallerThanAThousand()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(10);
        Assert.assertFalse("Expected 10 to be treated as a invalid loan amount", valid);
    }

    @Test
    public void testNoGreaterThanFifteenThousand()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(15000);
        Assert.assertTrue("Expected 15000 to be treated as a valid loan amount",valid);
    }

    @Test
    public void testGreaterThanFifteenThousand()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(15100);
        Assert.assertFalse("Expected 15100 to be treated as a invalid loan amount",valid);
    }

    @Test
    public void testDivisibleByAHundred()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(1900);
        Assert.assertTrue("Expected 1900 to be treated as a valid loan amount",valid);
    }

    @Test
    public void testNotDivisibleByAHundred()
    {
        boolean valid = loanAmountValidator.validateLoanAmount(250);
        Assert.assertFalse("Expected 250 to be treated as a invalid loan amount",valid);
    }
}
