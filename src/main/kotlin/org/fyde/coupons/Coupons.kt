package org.fyde.coupons

import org.fyde.coupons.database.*
import org.fyde.couponsDbQualifier
import org.fyde.mapFrom
import org.fyde.toResult
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jooby.Kooby
import org.jooby.apitool.ApiTool
import org.jsoup.Jsoup

/**
 * @author Rushan Pyakshev
 */

class Coupons : Kooby({

    path("/v1/coupons/") {
        get("/secret") { req ->
            req.param("type").toString()
                    .let { str -> CouponType.from(str) }
                    ?.let {
                        transaction(require(couponsDbQualifier, Database::class.java)) {
                            selectSecretCouponsByType(it)
                        }
                    }.toResult()

        }
        get("/secret/:id") { req ->
            transaction(require(couponsDbQualifier, Database::class.java)) {
                getSecretCoupon(req.param("id").longValue())
            }.toResult()
        }

        get("/coupons/:id") { req ->
            transaction(require(couponsDbQualifier, Database::class.java)) {
                getCoupon(req.param("id").longValue()).toResult()
            }
        }

        get("/coupons") { req ->
            req.param("type").toString()
                    .let { str -> CouponType.from(str) }
                    ?.let {
                        transaction(require(couponsDbQualifier, Database::class.java)) {
                            selectByType(it)
                        }
                    }.toResult()

        }
    }

})

val parsers: Map<CouponType, () -> List<String>> = mapOf(
        Pair(CouponType.KFC, {
            Jsoup.connect("https://www.kfc.ru/promo/74").get()
                    .getElementsByClass("coupon-list__item")
                    .map { it.child(0).attr("src") }
        }),
        Pair(CouponType.BK, {
            Jsoup.connect("https://burgerking.ru/bigboard/coupons").get()
                    .getElementsByAttributeValue("class", "coupon-img mt20")
                    .map { it.attr("src").replace("..", "https://burgerking.ru") }

        })
)

val couponsRouter: Map<CouponType, () -> List<String>> =
        mapFrom(CouponType.values()) { parsers[it]!! }
