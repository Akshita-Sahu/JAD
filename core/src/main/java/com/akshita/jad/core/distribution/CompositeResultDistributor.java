package com.akshita.jad.core.distribution;

/**
 * ，
 * @author gongdewei 2020/4/30
 */
public interface CompositeResultDistributor extends ResultDistributor {

    void addDistributor(ResultDistributor distributor);

    void removeDistributor(ResultDistributor distributor);
}
