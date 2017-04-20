package ru.donz.mosstock.domain;

import java.math.BigDecimal;

/**
 * It isn't needed, but just in case.
 * The object that contains information about deals were done during auction
 *
 * User: Donz
 * Date: 20.04.17
 * Time: 1:21
 */
public class Deal
{
    private Request askRequest;
    private Request bidRequest;
    private int amount;
    private BigDecimal price;

    public Deal( Request askRequest, Request bidRequest )
    {
        if( RequestType.ASK != askRequest.getType( ) || RequestType.BID != bidRequest.getType( ) )
        {
            throw new IllegalArgumentException( "Wrong requests or amount. Ask must be ask, bid must be bid, amount must be greater than zero. " +
                    "But they were: askRequest.getType()=" + askRequest.getType() + ", bidRequest.getType()=" + bidRequest.getType() + ", amount=" + amount );
        }
        if( askRequest.getPrice().compareTo( bidRequest.getPrice( ) ) < 0 )
        {
            throw new IllegalArgumentException( "Wrong requests. Ask price must not be less than bid price. " +
                    "But they were: askRequest.getPrice()=" + askRequest.getPrice() + ", bidRequest.getPrice()=" + bidRequest.getPrice() );
        }
        this.askRequest = askRequest;
        this.bidRequest = bidRequest;
        this.amount = Math.min( this.askRequest.getAmountLeft(), this.bidRequest.getAmountLeft() );
        //The price of deal is the price of the earlier request
        this.price = askRequest.getNanos() < bidRequest.getNanos() ? askRequest.getPrice() : bidRequest.getPrice();
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public Request getAskRequest()
    {
        return askRequest;
    }

    public void setAskRequest( Request askRequest )
    {
        this.askRequest = askRequest;
    }

    public Request getBidRequest()
    {
        return bidRequest;
    }

    public void setBidRequest( Request bidRequest )
    {
        this.bidRequest = bidRequest;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount( int amount )
    {
        this.amount = amount;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        Deal deal = ( Deal ) o;

        if( amount != deal.amount ) return false;
        if( !askRequest.equals( deal.askRequest ) ) return false;
        if( !bidRequest.equals( deal.bidRequest ) ) return false;
        //noinspection RedundantIfStatement
        if( !price.equals( deal.price ) ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = askRequest.hashCode();
        result = 31 * result + bidRequest.hashCode();
        result = 31 * result + amount;
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Deal{" +
                "askRequest=" + askRequest +
                ", bidRequest=" + bidRequest +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
