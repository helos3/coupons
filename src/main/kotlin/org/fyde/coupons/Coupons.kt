package org.fyde.coupons

import org.jooby.Kooby
import org.jooby.apitool.ApiTool

/**
 * @author <a href="mailto:rpyakshev@wiley.com">Rushan Pyakshev</a>
 */

class Api:  Kooby({
    path("/v1") {
        path("/coupons") {
            get { req ->
                getCoupons(req.param("types").toList().map { CouponType.from(it) })
            }
            get("/:id") {  }
            path("/:type/secret") {
                put {  }
                delete {  }
            }

        }
    }

})

fun getCoupons(types: List<CouponType>) = types.asSequence()
        .map { couponsRouter[it]!!.invoke() }
        .reduce { acc, list -> acc + list }

val couponsRouter: Map<CouponType, () -> List<Coupon>> = mapOf(
        Pair(CouponType.KFC, ::kfc),
        Pair(CouponType.BK, ::bk),
        Pair(CouponType.NONE, { emptyList<Coupon>() })
)


enum class CouponType { KFC, BK, NONE;

    companion object {
        private val values = values().map { CouponType.toString() }

        fun from(input: String): CouponType = if (values.contains(input)) valueOf(input) else NONE

    }
}

data class Coupon(val url: String, val type: CouponType)

fun kfc() = listOf(Coupon("google.com", CouponType.KFC))

fun bk() = emptyList<Coupon>()