package org.fyde.coupons.database


import org.jetbrains.exposed.sql.Table

enum class CouponType { KFC, BK, NONE;

    companion object {
        private val values = values().map { CouponType.toString() }

        fun from(input: String): CouponType = if (values.contains(input)) valueOf(input) else NONE
    }
}


object CouponMetadatas: Table() {
    val id = long("id").autoIncrement().primaryKey()
    val shareText = varchar("share_text", 255).nullable()
}

object CouponGroups: Table() {
    val id = long("id").autoIncrement().primaryKey()
    val terms = varchar("terms", 255)
    val obtainDate = date("publish_date")
    val url = varchar("url", 50)
    val invalidationDate = datetime("invalidation_date")
    val type = enumeration("type", CouponType::class)
}

object SecretCoupons: Table() {
    val url = varchar("url", 50).nullable()
    val couponId = long("coupon_id") references CouponMetadatas.id
    val publishDate = date("publish_date")
    val code = varchar("code", 10)
    val valid = bool("valid").default(true)
    val reportNumber = integer("report_number").default(0)
    val type = enumeration("type", CouponType::class)

}

object Coupons: Table() {
    val couponId = long("coupon_id") references CouponMetadatas.id
    val groupId = long("group_id") references CouponGroups.id
    val publishDate = date("publish_date")
    val url = varchar("url", 50)

}


