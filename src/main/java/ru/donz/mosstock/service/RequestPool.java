package ru.donz.mosstock.service;

import ru.donz.mosstock.domain.Request;
import ru.donz.mosstock.domain.RequestType;

import java.math.BigDecimal;
import java.util.SortedSet;

/**
 * Pool of all request
 *
 * User: KDanilov
 * Date: 19.04.17
 * Time: 17:29
 */
public interface RequestPool
{
    /**
     * Creates new request and adds it to request pool
     * @param type type of request
     * @param amount amount in request
     * @param price price in request
     * @return new request
     */
    Request addRequest( RequestType type, int amount, BigDecimal price );

    /**
     * @return sorted set (TreeSet) of asks
     */
    SortedSet<Request> getAsks();

    /**
     * @return sorted set (TreeSet) of bids
     */
    SortedSet<Request> getBids();
}
