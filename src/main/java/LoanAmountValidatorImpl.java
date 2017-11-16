/**
 * Created by mario on 09-Nov-17.
 */
public class LoanAmountValidatorImpl implements LoanAmountValidator
{
    /**
     * Validates the requested amount of money according to the three following requirement:
     * Borrowers should be able to request a loan of:
     * 1) any £100 increment
     * 2) between £1000
     * 3) and £15000 inclusive.
     * */
    @Override
    public boolean validateLoanAmount(int amount)
    {
        return (amount >= 100) && (amount <= 15000) && (amount % 100 == 0);
    }
}
