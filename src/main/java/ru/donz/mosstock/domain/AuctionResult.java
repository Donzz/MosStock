package ru.donz.mosstock.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Auction result
 *
 * User: Kdanilov
 * Date: 19.04.17
 * Time: 17:22
 */
public class AuctionResult
{
    private List<Deal> deals = new ArrayList<>(  );
    private BigDecimal totalSum = BigDecimal.ZERO;
    private int totalAmount;

    public void addDeal( Deal deal )
    {
        deals.add( deal );
    }

    public List<Deal> getDeals()
    {
        return deals;
    }

    public BigDecimal getCalculatedAveragePrice()
    {
        BigDecimal totalSum = BigDecimal.ZERO.setScale( 2 );
        int totalAmount = 0;
        for( Deal d : deals )
        {
            totalSum = totalSum.add( d.getPrice( ).multiply( new BigDecimal( d.getAmount() ) ) );
            totalAmount += d.getAmount( );
        }

        return totalSum.divide( new BigDecimal( totalAmount ), 2, RoundingMode.HALF_UP );
    }

    public int getCalculatedTotalAmount()
    {
        int totalAmount = 0;
        for( Deal d : deals )
        {
            totalAmount += d.getAmount( );
        }

        return totalAmount;
    }

    public BigDecimal getSimpleAverageSum()
    {
        return totalSum.divide( new BigDecimal( totalAmount ), 2, RoundingMode.HALF_UP );
    }

    public BigDecimal getTotalSum()
    {
        return totalSum;
    }

    public void setTotalSum( BigDecimal totalSum )
    {
        this.totalSum = totalSum;
    }

    public int getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount( int totalAmount )
    {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        AuctionResult result = ( AuctionResult ) o;

        if( totalAmount != result.totalAmount ) return false;
        if( totalSum != null ? !totalSum.equals( result.totalSum ) : result.totalSum != null ) return false;
        //noinspection RedundantIfStatement
        if( !deals.equals( result.deals ) ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = deals.hashCode();
        result = 31 * result + ( totalSum != null ? totalSum.hashCode() : 0 );
        result = 31 * result + totalAmount;
        return result;
    }

    @Override
    public String toString()
    {
        return "AuctionResult{" +
                "deals=" + deals +
                ", totalSum=" + totalSum +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
