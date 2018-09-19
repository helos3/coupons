package org.fyde.coupons

import org.fyde.Exposed
import org.fyde.mapFrom
import org.jooby.Kooby
import org.jsoup.Jsoup

/**
 * @author Rushan Pyakshev
 */

class Coupons : Kooby({

    use(Exposed())

    path("/v1/coupons/") {
        get {
            getCoupons(CouponType.values().toList())
        }
        get("/:id") {
            TODO()
        }
        get("/:type") { req ->
            getCoupons(req.param("type").toList().map { CouponType.from(it) })
        }
        path("/:type/secret") {
            put {
                TODO()
            }
            delete {
                TODO()
            }
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
