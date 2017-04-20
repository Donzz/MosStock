package ru.donz.mosstock.service;

import org.junit.Before;
import org.junit.Test;
import ru.donz.mosstock.domain.AuctionResult;
import ru.donz.mosstock.domain.Request;
import ru.donz.mosstock.domain.RequestType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: Donz
 * Date: 20.04.17
 * Time: 2:23
 */
public class StockExchangeControllerImplTest
{
    private StockExchangeController controller;

    @Before
    public void setUp()
    {
        controller = new StockExchangeControllerImpl();
    }

    @Test
    public void createRequestPool() throws Exception
    {
        RequestPool requestPool = controller.createRequestPool();
        assertNotNull( requestPool );
    }

    @Test( expected = ArithmeticException.class )
    public void calculateNoDeals() throws Exception
    {
        RequestPool requestPool = controller.createRequestPool();
        requestPool.addRequest( RequestType.BID, 100, new BigDecimal( 10.10 ) );
        requestPool.addRequest( RequestType.ASK, 150, new BigDecimal( 10.00 ) );
        AuctionResult result = controller.calculate( requestPool );
        assertEquals( "No deals must be in result", 0, result.getDeals().size() );
        assertEquals( "Total amount must be 0", 0, result.getTotalAmount() );
        assertEquals( "Average price must be NaN", 0, result.getSimpleAverageSum() );
    }

    @Test
    public void calculateTwoDeals() throws Exception
    {
        RequestPool requestPool = controller.createRequestPool();
        requestPool.addRequest( RequestType.ASK, 100, new BigDecimal( 15.40 ) );
        requestPool.addRequest( RequestType.ASK, 100, new BigDecimal( 15.30 ) );
        requestPool.addRequest( RequestType.BID, 150, new BigDecimal( 15.30 ) );
        AuctionResult result = controller.calculate( requestPool );
        assertEquals( "Two deals must be in result", 2, result.getDeals().size() );
        assertEquals( "Total amount must be 150", 150, result.getTotalAmount() );
        assertEquals( "Average price must be 15.33", new BigDecimal( 15.37 ).setScale( 2, RoundingMode.HALF_UP ),
                result.getSimpleAverageSum() );
    }
}
