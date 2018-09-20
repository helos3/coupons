package org.fyde.coupons

import java.time.LocalDate

/**
 * @author <a href="mailto:rpyakshev@wiley.com">Rushan Pyakshev</a>
 */

data class Coupon(
        val id: Long,
        val image: String,
        val shareText: String
)

data class SecretCoupon(
        val id: Long,
        val shareText: String,
        val code: String? = null,
        val image: String? = null
)

data class CouponGroup(
        val terms: String,
        val coupons: List<Coupon>,
        val until: LocalDate? = null
)

data class CouponsResponse(
        val groups: List<CouponGroup>,
        val secretCoupons: List<SecretCoupon>
)