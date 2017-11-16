import model.Lender;
import model.Quote;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * The Application class, coordinating the business logic.
 * Using Constructor-Dependency-Injection the class offers additional flexibility and it's easier to test as it
 * can accept various implementations of components that implement the business logic.
 */
public class Application
{
    private final String filePath;
    private final String loanAmountRequest;
    private final LoanAmountValidator loanAmountValidator;
    private final QuoteCalculator quoteCalculator;

    public Application(String filePath, String loanAmountRequest, LoanAmountValidator loanAmountValidator, QuoteCalculator quoteCalculator)
    {
        this.filePath = filePath;
        this.loanAmountRequest = loanAmountRequest;
        this.loanAmountValidator = loanAmountValidator;
        this.quoteCalculator = quoteCalculator;
    }

    public void start()
    {
        File csvFile = new File(filePath);

        int loanAmount = 0;
        boolean validLoanAmount = false;

        try
        {
            loanAmount = Integer.parseInt(loanAmountRequest);
            validLoanAmount = loanAmountValidator.validateLoanAmount(loanAmount);
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Invalid value for 'loan_amount'. Expecting any £100 increment between £1000 and £15000 inclusive.");
        }

        if(validLoanAmount && csvFile.exists())
        {
            List<Lender> lenders = LenderFileExtractor.extractLenders(csvFile);

            Optional<Quote> quote = quoteCalculator.calculateQuoteRate(loanAmount, lenders, 36, 12);

            if(quote.isPresent())
            {
                System.out.println(quote.get().toString());
            }
            else
            {
                System.out.println("It's not possible to provide a quote at this time.");
            }
        }
        else
        {
            System.err.println("Could not locate specified file: '"+csvFile.getName()+"'.");
        }
    }
}
