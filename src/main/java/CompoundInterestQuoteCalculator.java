import model.Lender;
import model.Quote;

import java.util.*;

/**
 * Implementation of the QuoteCalculator class that estimates the lowest rates by:
 * 1) Sorting the available lenders according to the rate that they offer
 * 2) Iterating through the sorted lenders and attempting to collect the highest amount of money at the lowest rates
 * 3) Estimating the average rate according to the formula found in https://www.investopedia.com/terms/i/interestrate.asp
 *    The formula is: Simple Interest = P (principal) x I (annual interest rate) x N (years)
 * 4) Estimating the total compound interest according to the formula found in https://www.investopedia.com/articles/investing/020614/learn-simple-and-compound-interest.asp
 *    The formula is P [(1 + i/n)nt – 1]
 * Any feedback to the user is being printed by the calling class
 * @see QuoteCalculator
 */
public class CompoundInterestQuoteCalculator implements QuoteCalculator
{
    @Override
    public Optional<Quote> calculateQuoteRate(int loanAmount, List<Lender> lenders, int installments, int compoundingPeriodsPerYear )
    {
        Optional<Quote> quoteHolder = Optional.empty();

        //Checking if the requested amount for the loan can be served by the existing offers.
        //If not the Quote is returned as an empty optional. Printing the feedback is handled by the calling class.
        if(canOfferQuote(loanAmount,lenders))
        {
            Map<Double,Integer> amountsPerRate = calculateAmountsPerRate(loanAmount, lenders);

            double totalMonthlyInterest = 0;

            for(Map.Entry entry : amountsPerRate.entrySet())
            {
                //Calculating the amount of money that will be paid for interest every month for each rate
                //Assuming interest rates provided by the Lenders are 'per year'
                double monthlyInterest = ((Double)entry.getKey() / compoundingPeriodsPerYear)*(Integer)entry.getValue();
                totalMonthlyInterest += monthlyInterest;
            }
            //Estimating the interest according to the following formula
            //Simple Interest = P (principal) x I (annual interest rate) x N (years)
            //found in: https://www.investopedia.com/terms/i/interestrate.asp
            //that in turn we transform to
            //I (annual interest rate) = SI(Simple Interest = monthlyInterest * numberOfInstallments) / (P (principal)  x N (years))
            double averageRate = ((totalMonthlyInterest * installments) / (loanAmount  * (installments/compoundingPeriodsPerYear)));

            //Estimating the total interest using the Compound interest formula
            //P [(1 + i/n)nt – 1]
            //found in: https://www.investopedia.com/articles/investing/020614/learn-simple-and-compound-interest.asp
            double totalInterest = loanAmount * ( Math.pow( (1 + averageRate/compoundingPeriodsPerYear),36) - 1);
            double totalRepayment = loanAmount + totalInterest;
            double monthlyRepayment = totalRepayment / installments;

            quoteHolder = Optional.of(new Quote(loanAmount,averageRate*100,monthlyRepayment, totalRepayment));
        }

        return quoteHolder;
    }
}
