/**
 * Launcher class for our application
 */
public class ApplicationLauncher
{
    public static void main(String args[])
    {
        if(args.length == 2)
        {
            Application app = new Application(args[0], args[1], new LoanAmountValidatorImpl(), new CompoundInterestQuoteCalculator());
            app.start();
        }
        else
        {
            //Invalid number of parameters
            System.err.println("Invalid parameters. Expecting [market_file] [loan_amount] as the program parameters.");
        }
    }
}
