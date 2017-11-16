import model.Lender;
import model.Quote;

import java.util.*;

/**
 * Interface used to calculate the lowest rate possible
 */
public interface QuoteCalculator {

    /**
     * Calculates the lowest rate possible for the existing list of available lenders and
     * the requested amount of money
     *
     * @param loanAmount the requested amount of money
     * @param lenders the list of available Lenders in our system
     * @param installments the number of installments for this loan
     * @param compoundingPeriodsPerYear the number of times the loan is compounded per year
     *
     * @returns <code>Optional<Quote></Quote></code> an Optional object containing the generated quote
     *
     * @see Lender
     * */
    Optional<Quote> calculateQuoteRate(int loanAmount, List<Lender> lenders, int installments, int compoundingPeriodsPerYear);

    /**
     * Calculates the amount of money that will be borrowed mapped by the respective interest rate
     *
     * @param loanAmount the requested amount of money
     * @param lenders the list of available Lenders in our system
     *
     * @see Lender
     * */

    default Map<Double,Integer> calculateAmountsPerRate(int loanAmount, List<Lender> lenders)
    {
        //Sorting and mapping the available lenders using their rate
        //Order of rates is guaranteed by the fact that a TreeMap is being used
        Map<Double, List<Lender>> sortedLenders = new TreeMap<>();

        for(Lender lender : lenders)
        {
            if(sortedLenders.get(lender.getRate()) == null)
            {
                sortedLenders.put(lender.getRate(), new ArrayList<>());
            }
            sortedLenders.get(lender.getRate()).add(lender);
        }

        int amountTobeCollected = loanAmount;
        Map<Double,Integer> amountsPerRate = new HashMap<>();

        for(Double rate : sortedLenders.keySet())
        {
            List<Lender> lendersPerRate = sortedLenders.get(rate);
            //Sorting the Lenders based on the highest amount of money that they offer (Lender.available)
            Collections.sort(lendersPerRate, Comparator.comparing(Lender::getAvailable).reversed());

            for(Lender lender : lendersPerRate)
            {
                if(lender.getAvailable() > 0)
                {
                    if(lender.getAvailable() <= amountTobeCollected)
                    {
                        amountTobeCollected -= lender.getAvailable();

                        amountsPerRate.merge(rate, lender.getAvailable(), Integer::sum);

                        lender.setAvailable(0);
                    }
                    else
                    {
                        //Assuming that it is possible to lend part of the money offered by the Lender
                        amountsPerRate.merge(rate, amountTobeCollected, Integer::sum);

                        lender.setAvailable(lender.getAvailable() - amountTobeCollected );

                        amountTobeCollected = 0;
                    }
                    if(amountTobeCollected == 0)
                    {
                        break;
                    }
                }
            }

            if(amountTobeCollected == 0)
            {
                break;
            }
        }

        return amountsPerRate;
    }

    /**
     * Validates if the requested amount of money can be served by the existing founds available by our lenders
     *
     * @param loanAmount the requested amount of money
     * @param lenders the list of available Lenders in our system
     *
     * @returns <code>boolean</code> a boolean value indicating if the loan can be served
     *
     * @see Lender
     * */
    default boolean canOfferQuote(int loanAmount, List<Lender> lenders)
    {
        double totalAvailable = lenders.
                stream().
                mapToDouble(Lender::getAvailable).
                sum();

        return loanAmount < totalAvailable;
    }
}
