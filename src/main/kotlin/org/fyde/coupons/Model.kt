package org.fyde.coupons

enum class CouponType { KFC, BK, NONE;

    companion object {
        private val values = values().map { CouponType.toString() }

        fun from(input: String): CouponType = if (values.contains(input)) valueOf(input) else NONE
    }
}