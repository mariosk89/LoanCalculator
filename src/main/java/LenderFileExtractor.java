import model.Lender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extractor class for extracting the list of available lenders from the provided csv file
 */
public class LenderFileExtractor
{
    /**
     * Extracts the list of lenders from the provided file and handles all required IO operations
     *
     * @param csvFile the csv file containing the Lenders
     *
     * @return <code>List<Lender></code> the list of extracted Lenders
     *
     * @see Lender
     * */
    public static List<Lender> extractLenders(File csvFile)
    {
        List<Lender> lenders = new ArrayList<Lender>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            String entry = "";
            while ((entry = br.readLine()) != null) {

                String[] lenderDetails = entry.split(",");

                //Ignore the first line of the file
                if(lenderDetails.length == 3)
                {
                    if(!entry.equals("Lender,Rate,Available"))
                    {
                        try
                        {
                            //Validating if the values for 'rate' and 'available' are numerical
                            Double rate = Double.parseDouble(lenderDetails[1]);
                            int available = Integer.parseInt(lenderDetails[2]);

                            lenders.add(new Lender(lenderDetails[0], rate, available));
                        }
                        catch (NumberFormatException nfe)
                        {
                            //Invalid data format. This entry will be omitted
                            System.err.format("Invalid data format. Entry' %s , %s , %s ' will be omitted\n", lenderDetails[0], lenderDetails[1], lenderDetails[2]);
                        }
                    }
                }
                else
                {
                    //Invalid data format. This entry will be omitted
                    System.err.println("Invalid data format. Entry '"+entry+"' will be omitted");
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not read specified file: '"+csvFile.getName()+"'");
        }

        return lenders;
    }
}
