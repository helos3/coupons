package org.fyde.coupons.database


import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

enum class CouponType { KFC, BK;

    companion object {
        private val values = values().map { CouponType.toString() }

        fun from(input: String): CouponType? = if (values.contains(input)) valueOf(input) else null
    }
}


object CouponMetadatas: Table("coupon_metadata") {
    val id = long("id").autoIncrement().primaryKey()
    val shareText = varchar("share_text", 1024).nullable()
}

object CouponGroups: Table("coupon_group") {
    val id = long("id").autoIncrement().primaryKey()
    val terms = varchar("terms", 1024)
    val obtainDate = date("publish_date").default(DateTime.now())
    val url = varchar("url", 50)
    val invalidationDate = datetime("invalidation_date")
    val type = enumerationByName("type", 10, CouponType::class)
}

object SecretCoupons: Table("secret_coupon") {
    val url = varchar("url", 50).nullable()
    val couponId = long("coupon_id") references CouponMetadatas.id
    val publishDate = date("publish_date").default(DateTime.now())
    val code = varchar("code", 10)
    val valid = bool("valid").default(true)
    val reportNumber = integer("report_number").default(0)
    val description = varchar("description", 1024)
    val type = enumerationByName("type", 10, CouponType::class)

}

object Coupons: Table("coupon") {
    val couponId = long("coupon_id") references CouponMetadatas.id
    val groupId = long("group_id") references CouponGroups.id
    val publishDate = date("publish_date").default(DateTime.now())
    val url = varchar("url", 50)
}


