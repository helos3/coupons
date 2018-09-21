package org.fyde.coupons.database

import org.fyde.coupons.Coupon
import org.fyde.coupons.CouponGroup
import org.fyde.coupons.SecretCoupon
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime

/**
 * @author <a href="mailto:rpyakshev@wiley.com">Rushan Pyakshev</a>
 */

fun selectByType(type: CouponType) : List<CouponGroup>? {
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
                        .add(Coupon(it[CouponMetadatas.id], it[Coupons.url], shareText(it[CouponMetadatas.shareText])))
            }

    return groupIdToTerms
            .takeIf { it.isNotEmpty() }
            ?.map { CouponGroup(it.value, groupIdToCoupon[it.key]!!) }
}

fun getCoupon(id: Long): Coupon? =
        Coupons.innerJoin(CouponMetadatas, { Coupons.couponId }, { CouponMetadatas.id })
                .slice(CouponMetadatas.id, CouponMetadatas.shareText, Coupons.url)
                .select { CouponMetadatas.id.eq(id) }
                .map { Coupon(it[CouponMetadatas.id], it[Coupons.url], shareText(it[CouponMetadatas.shareText])) }
                .firstOrNull()


fun selectSecretCouponsByType(type: CouponType) : List<SecretCoupon>? =
        selectSecretCoupons(SecretCoupons.type.eq(type)).takeIf { it.isNotEmpty() }

fun getSecretCoupon(id: Long) : SecretCoupon? =
        selectSecretCoupons(SecretCoupons.couponId.eq(id)).firstOrNull()

private fun selectSecretCoupons(where: Op<Boolean>): List<SecretCoupon> =
        SecretCoupons.innerJoin(CouponMetadatas, { SecretCoupons.couponId }, { CouponMetadatas.id })
                .slice(CouponMetadatas.id, CouponMetadatas.shareText, SecretCoupons.code, SecretCoupons.url, SecretCoupons.description)
                .select { SecretCoupons.valid.eq(true).and(where) }
                .map {
                    SecretCoupon(
                            it[CouponMetadatas.id],
                            shareText(it[CouponMetadatas.shareText]),
                            it[SecretCoupons.description],
                            it[SecretCoupons.code],
                            it[SecretCoupons.url]
                    )
                }

private fun shareText(input: String?) = input ?: "def"


