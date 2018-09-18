package org.idk.coupons

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jooby.Kooby
import org.jooby.json.Jackson
import org.jooby.run

fun main(args: Array<String>) {
    run(::App, *args)
}

class App : Kooby({
    use(Jackson(ObjectMapper().registerKotlinModule()))

    path("/v1") {
        path("/coupons") {
            get { req ->
                req.param("types").toList().map { CouponType.from(it) }
            }

        }
    }
})

fun getCoupons(types: List<CouponType>) = types.map { couponsRouter[it]!!.invoke() }
        .reduce { acc, list -> acc + list }

val couponsRouter: Map<CouponType, () -> List<Coupon>> = mapOf(
        Pair(CouponType.KFC, ::kfc),
        Pair(CouponType.BK, ::bk),
        Pair(CouponType.NONE, { emptyList<Coupon>() })
)


enum class CouponType { KFC, BK, NONE;

    companion object {
        private val values = values().map { toString() }

        fun from(input: String): CouponType = values.contains(input)
                .takeIf { b -> b }
                ?.let { valueOf(input) } ?: NONE


    }
}

data class Coupon(val url: String, val type: CouponType)

fun kfc() = listOf(Coupon("google.com", CouponType.KFC))

fun bk() = emptyList<Coupon>()