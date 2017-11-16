package model;

import java.text.DecimalFormat;

/**
 * POJO class representing the Quote object
 */
public class Quote
{
    private int requestedAmount;
    private double rate;
    private double monthlyRepayment;
    private double totalRepayment;

    public Quote(int requestedAmount, double rate, double monthlyRepayment, double totalRepayment)
    {
        this.requestedAmount = requestedAmount;
        this.rate = rate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
    }

    @Override
    public String toString() {

        DecimalFormat dfZero = new DecimalFormat("#");
        DecimalFormat dfOne = new DecimalFormat("#.0");
        DecimalFormat dfTwo = new DecimalFormat("#.00");

        return "Requested Amount: \u00A3" + dfZero.format(requestedAmount) + "\n"+
                "Rate: " + dfOne.format(rate) + "%\n"+
                "Monthly Repayment: \u00A3" + dfTwo.format(monthlyRepayment) + "\n"+
                "Total Repayment: \u00A3" + dfTwo.format(totalRepayment);
    }
}
