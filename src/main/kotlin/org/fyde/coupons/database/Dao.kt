package org.fyde.coupons.database

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.fyde.coupons.Coupon
import org.fyde.coupons.CouponGroup
import org.fyde.coupons.SecretCoupon
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.jooby.internal.apitool.antlr.misc.MultiMap

/**
 * @author <a href="mailto:rpyakshev@wiley.com">Rushan Pyakshev</a>
 */

fun selectByType(type: CouponType) {
    val groupIdToTerms = mutableMapOf<Long, String>()
    val groupIdToCoupon = mutableMapOf<Long, MutableList<Coupon>>()

    Coupons
            .innerJoin(CouponGroups, { Coupons.groupId }, { CouponGroups.id })
            .innerJoin(CouponMetadatas, { Coupons.couponId }, { CouponMetadatas.id })
            .slice(CouponGroups.id, CouponGroups.terms, CouponMetadatas.id, CouponMetadatas.shareText, Coupons.url)
            .select { CouponGroups.invalidationDate.greater(DateTime.now()) and CouponGroups.type.eq(type) }
            //todo groupby
            .forEach {
                groupIdToTerms.getOrPut(it[CouponGroups.id]) { it[CouponGroups.terms] }
                groupIdToCoupon.getOrPut(it[CouponGroups.id]) { mutableListOf() }
                        .add(Coupon(it[CouponMetadatas.id], it[Coupons.url], it[CouponMetadatas.shareText] ?: "def"))
            }

    groupIdToTerms.map{CouponGroup(it.value, groupIdToCoupon[it.key]!!) }
}

fun selectSecretCouponsByType(type: CouponType) {
    SecretCoupons.innerJoin(CouponMetadatas, { SecretCoupons.couponId }, { CouponMetadatas.id })
            .slice(CouponMetadatas.id, CouponMetadatas.shareText, SecretCoupons.code, SecretCoupons.url)
            .select { SecretCoupons.valid.eq(true) and SecretCoupons.type.eq(type) }
            .map { SecretCoupon(
                    it[CouponMetadatas.id],
                    it[CouponMetadatas.shareText] ?: "def",
                    it[SecretCoupons.code],
                    it[SecretCoupons.url]
            ) }

}


