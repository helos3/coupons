package org.fyde.coupons

import org.fyde.coupons.database.CouponType
import org.fyde.mapFrom
import org.jooby.Kooby
import org.jsoup.Jsoup

/**
 * @author Rushan Pyakshev
 */

class Coupons : Kooby({

    path("/v1/coupons/") {
        get("/:id") {
            TODO()
        }
        get("/:type") { req ->
            TODO()
//            getCoupons(req.param("type").toList().map { CouponType.from(it) })
        }
        get("/secret/:type") {
            TODO()
        }
    }
})

fun getCoupons(types: List<CouponType>) = types.asSequence()
        .map { couponsRouter[it]!!.invoke() }
        .reduce { acc, list -> acc + list }

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

        }),
        Pair(CouponType.NONE, { emptyList<String>() })
)

val couponsRouter: Map<CouponType, () -> List<String>> =
        mapFrom(CouponType.values()) { parsers[it]!! }
