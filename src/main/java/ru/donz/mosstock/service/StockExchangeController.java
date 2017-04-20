package ru.donz.mosstock.service;

import ru.donz.mosstock.domain.AuctionResult;

/**
 * Controller of stock exchange
 *
 * User: Kdanilov
 * Date: 19.04.17
 * Time: 17:19
 */
public interface StockExchangeController
{
    /**
     * Return new request pool
     * @return new request pool
     */
    RequestPool createRequestPool();

    /**
     * Calculates results of all requests
     * @param pool pool of all request to calculate result for
     * @return result of auction
     */
    AuctionResult calculate( RequestPool pool );
}
