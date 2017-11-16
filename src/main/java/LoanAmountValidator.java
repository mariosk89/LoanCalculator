/**
 * Validator interface for validating if the requested amount of money meets the criteria for loans offered by our system
 */
public interface LoanAmountValidator
{
    /**
     * Validates the requested amount against the system's criteria for loans
     *
     * @param amount the requested amount of money
     * */
    boolean validateLoanAmount(int amount);
}
