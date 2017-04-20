package ru.donz.mosstock.service;

import org.junit.Before;
import org.junit.Test;
import ru.donz.mosstock.domain.Request;
import ru.donz.mosstock.domain.RequestType;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * User: Donz
 * Date: 20.04.17
 * Time: 2:32
 */
public class RequestPoolImplTest
{
    private RequestPool requestPool;

    @Before
    public void setUp() throws Exception
    {
        requestPool = new RequestPoolImpl( 1, 1000, BigDecimal.ONE, new BigDecimal( 100 ), 3 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRequestValidationMinAmount() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 0, BigDecimal.ONE );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRequestValidationMaxAmount() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 1001, BigDecimal.ONE );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRequestValidationMinPrice() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 1, BigDecimal.ZERO );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRequestValidationMaxPrice() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 101 ) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRequestValidationMaxRequests() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
    }

    public void addRequestValidationRequestsCount() throws Exception
    {
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        assertEquals( "Wrong requests count", 3, requestPool.getAsks().size() );
    }

    @Test
    public void testGetAsks() throws Exception
    {
        Request r1 = requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        Request r2 = requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 10 ) );
        Request r3 = requestPool.addRequest( RequestType.ASK, 1, new BigDecimal( 20 ) );
        assertEquals( "Ask request with the biggest price must be first", r3, requestPool.getAsks().first() );
    }

    @Test
    public void testGetBids() throws Exception
    {
        Request r1 = requestPool.addRequest( RequestType.BID, 1, new BigDecimal( 10 ) );
        Request r2 = requestPool.addRequest( RequestType.BID, 1, new BigDecimal( 20 ) );
        Request r3 = requestPool.addRequest( RequestType.BID, 1, new BigDecimal( 10 ) );
        assertEquals( "Bid request with the biggest price must be last", r2, requestPool.getBids().last() );
    }
}
