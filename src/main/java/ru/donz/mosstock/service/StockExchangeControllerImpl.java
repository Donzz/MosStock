package ru.donz.mosstock.service;

import com.google.common.collect.SortedMultiset;
import ru.donz.mosstock.domain.AuctionResult;
import ru.donz.mosstock.domain.Deal;
import ru.donz.mosstock.domain.Request;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * User: Donz
 * Date: 20.04.17
 * Time: 0:31
 */
public class StockExchangeControllerImpl implements StockExchangeController
{
    @Override
    public RequestPool createRequestPool()
    {
        return new RequestPoolImpl( 1, 1000, BigDecimal.ONE, new BigDecimal( 100 ), 1000000 );
    }

    @Override
    public AuctionResult calculate( RequestPool pool )
    {
        AuctionResult result = new AuctionResult();
        SortedSet<Request> asks = pool.getAsks();
        SortedSet<Request> bids = pool.getBids();

        Iterator<Request> askIter = asks.iterator();
        Iterator<Request> bidIter = bids.iterator();

        if( askIter.hasNext() && bidIter.hasNext() )
        {
            Request ask = askIter.next();
            Request bid = bidIter.next();
            while( ask.getPrice().compareTo( bid.getPrice() ) >= 0 )
            {
                Deal d = new Deal( ask, bid );
                result.addDeal( d );

                ask.decreaseAmountLeft( d.getAmount() );
                bid.decreaseAmountLeft( d.getAmount() );
                result.setTotalSum( result.getTotalSum().add( d.getPrice().multiply( new BigDecimal( d.getAmount() ) ) ) );
                result.setTotalAmount( result.getTotalAmount() + d.getAmount() );

                if( ask.getAmountLeft() == 0 && askIter.hasNext() )
                {
                    ask = askIter.next();
                }
                else if( ask.getAmountLeft() == 0 && !askIter.hasNext() )
                {
                    break;
                }
                if( bid.getAmountLeft() == 0 && bidIter.hasNext() )
                {
                    bid = bidIter.next();
                }
                else if( bid.getAmountLeft() == 0 && !bidIter.hasNext() )
                {
                    break;
                }
            }
        }

        return result;
    }
}
