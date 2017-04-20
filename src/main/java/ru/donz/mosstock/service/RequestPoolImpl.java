package ru.donz.mosstock.service;

import ru.donz.mosstock.domain.Request;
import ru.donz.mosstock.domain.RequestType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Poo
 *
 * User: Donz
 * Date: 20.04.17
 * Time: 0:07
 */
public class RequestPoolImpl implements RequestPool
{
    private int maxRequests;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private int minAmount;
    private int maxAmount;

    private SortedSet<Request> asks = new TreeSet<>( new RequestComparator().reversed() );
    private SortedSet<Request> bids = new TreeSet<>( new RequestComparator() );

    public RequestPoolImpl( int minAmount, int maxAmount, BigDecimal minPrice, BigDecimal maxPrice, int maxRequests )
    {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.maxRequests = maxRequests;
    }

    @Override
    public Request addRequest( RequestType type, int amount, BigDecimal price )
    {
        validate( amount, price );

        Request request = new Request( );
        //It's pseudo unique, but OK for this case
        request.setId( UUID.randomUUID() );
        request.setType( type );
        request.setAmount( amount );
        request.setPrice( price.setScale( 2, RoundingMode.HALF_UP ) );
        request.setCreationTime( new Date( ) );
        request.setNanos( System.nanoTime( ) );

        if( RequestType.ASK == type )
        {
            asks.add( request );
        }
        else if( RequestType.BID == type )
        {
            bids.add( request );
        }
        else
        {
            throw new IllegalArgumentException( "Request type + " + type + " wasn't expected " );
        }

        return request;
    }

    @Override
    public SortedSet<Request> getAsks()
    {
        return asks;
    }

    @Override
    public SortedSet<Request> getBids()
    {
        return bids;
    }

    private void validate( int amount, BigDecimal price )
    {
        int newRequestsAmount = bids.size() + asks.size() + 1;
        if( newRequestsAmount > maxRequests )
        {
            throw new IllegalArgumentException( "Requests amount " + newRequestsAmount + " greater than " + maxRequests );
        }
        if( amount < minAmount || amount > maxAmount )
        {
            throw new IllegalArgumentException( "Amount in one request " + amount + " is not in the legal range from " + minAmount + " to " + maxAmount );
        }
        if( price == null || price.compareTo( minPrice ) < 0 || price.compareTo( maxPrice ) > 0 )
        {
            throw new IllegalArgumentException( "Price in request " + price + " is not in the legal range from " + minPrice + " to " + maxPrice );
        }
    }


    /**
     * We need to use TreeSet for log(n) time cost. But we can't use only prices as they can be the same and in this case TreeSet replace element as it use comparator as equals.
     * So we add UUID comparison in case of equal prices.
     */
    static class RequestComparator implements Comparator<Request>
    {

        @Override
        public int compare( Request o1, Request o2 )
        {
            int c = o1.getPrice().compareTo( o2.getPrice() );
            if( c == 0 )
            {
                c = o1.getId().compareTo( o2.getId() );
            }
            return c;
        }
    }


}
