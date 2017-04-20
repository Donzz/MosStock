package ru.donz.mosstock.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Request for acting in stock exchange
 * <p/>
 * User: Kdanilov
 * Date: 19.04.17
 * Time: 17:11
 */
public class Request
{
    /**
     * Surrogate ID
     */
    private UUID id;
    /**
     * Nanotime of creation for precision compare which request was created early. It isn't needed for this task but make sense in total
     */
    private long nanos;
    /**
     * Request type: BID or ASK
     */
    private RequestType type;
    /**
     * Amount
     */
    private int amount;
    /**
     * Amount left
     */
    private int amountLeft;
    /**
     * Price
     */
    private BigDecimal price;
    /**
     * Time of request creation
     */
    private Date creationTime;

    public UUID getId()
    {
        return id;
    }

    public void setId( UUID id )
    {
        this.id = id;
    }

    public long getNanos()
    {
        return nanos;
    }

    public void setNanos( long nanos )
    {
        this.nanos = nanos;
    }

    public RequestType getType()
    {
        return type;
    }

    public void setType( RequestType type )
    {
        this.type = type;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount( int amount )
    {
        this.amount = amount;
        setAmountLeft( this.amount );
    }

    public int getAmountLeft()
    {
        return amountLeft;
    }

    public void setAmountLeft( int amountLeft )
    {
        this.amountLeft = amountLeft;
    }

    public void decreaseAmountLeft( int deduction )
    {
        if( amountLeft < deduction )
        {
            throw new IllegalArgumentException( "Deduction can't be greater than amount left. Amount left: " + amountLeft + ", deduction: " + deduction );
        }
        this.amountLeft -= deduction;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice( BigDecimal price )
    {
        this.price = price;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime( Date creationTime )
    {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass( ) != o.getClass( ) )
        {
            return false;
        }

        Request request = ( Request ) o;

        if( amount != request.amount )
        {
            return false;
        }
        if( id != request.id )
        {
            return false;
        }
        if( nanos != request.nanos )
        {
            return false;
        }
        if( creationTime != null ? !creationTime.equals( request.creationTime ) : request.creationTime != null )
        {
            return false;
        }
        if( price != null ? !price.equals( request.price ) : request.price != null )
        {
            return false;
        }
        //noinspection RedundantIfStatement
        if( type != request.type )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "id=" + id +
                ", nanos=" + nanos +
                ", type=" + type +
                ", amount=" + amount +
                ", price=" + price +
                ", creationTime=" + creationTime +
                "} " + super.toString( );
    }
}
