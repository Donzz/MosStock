package ru.donz.mosstock;

import ru.donz.mosstock.domain.AuctionResult;
import ru.donz.mosstock.domain.RequestType;
import ru.donz.mosstock.service.RequestPool;
import ru.donz.mosstock.service.StockExchangeController;
import ru.donz.mosstock.service.StockExchangeControllerImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{

    public static void main( String[] args )
    {
        Main main = new Main();
        main.start();
    }

    /**
     * Console reader, writer and starter of stock exchange controller
     */
    public void start()
    {
        StockExchangeController controller = new StockExchangeControllerImpl();
        RequestPool requestPool = controller.createRequestPool();

        System.out.println("INPUT:");
        Pattern pattern = Pattern.compile( "(?<type>[BS])\\s(?<amount>\\d{1,4})\\s(?<price>\\d{1,3}(\\.\\d{0,2}))?\\s*" );
        Scanner scanner = new Scanner( System.in ).useDelimiter( "\n" );
        String s;
        do
        {
            s = scanner.next();
            if( s.equals( "commit" ) )
            {
                break;
            }
            Matcher m = pattern.matcher( s );
            if( !pattern.matcher( s ).matches() )
            {
                System.out.println("Right pattern:");
                System.out.println("<B|S> <integer> <decimal with dot>");
            }
            else
            {
                m.find();
                String type = m.group("type");
                String amount = m.group("amount");
                String price = m.group("price");
                try
                {
                    parseAndAddRequest( requestPool, type, amount, price );
                }
                catch( NumberFormatException e )
                {
                    System.out.println("Wrong format exception");
                }
                catch( IllegalArgumentException e )
                {
                    System.out.println( e.getMessage() );
                }
                catch( Exception e )
                {
                    System.out.println( "Something else wrong: " + e );
                }
            }
        } while( true );

        AuctionResult result = controller.calculate( requestPool );
        System.out.println( "OUTPUT:" );
        if( result.getTotalAmount() == 0 )
        {
            System.out.println( "0 n/a" );
        }
        else
        {
            System.out.println( result.getTotalAmount() + " " + result.getSimpleAverageSum() );
        }
    }

    private void parseAndAddRequest( RequestPool requestPool, String type, String amount, String price )
    {
        requestPool.addRequest( "B".equals( type ) ? RequestType.ASK : RequestType.BID, Integer.parseInt( amount ),
                new BigDecimal( price ).setScale( 2, RoundingMode.HALF_UP ) );
    }
}
