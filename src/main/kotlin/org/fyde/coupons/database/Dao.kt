package org.fyde.coupons.database

import com.google.common.collect.Multimap
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author <a href="mailto:rpyakshev@wiley.com">Rushan Pyakshev</a>
 */

fun selectByType(type: CouponType){
    Coupons
            .innerJoin(CouponGroups, {Coupons.groupId}, {CouponGroups.id})
            .innerJoin(CouponMetadatas, {Coupons.couponId}, {CouponMetadatas.id})
            .slice()
}