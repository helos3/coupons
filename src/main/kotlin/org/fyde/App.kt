package org.fyde

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.typesafe.config.Config
import org.fyde.coupons.Coupons
import org.fyde.coupons.database.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jooby.Jooby
import org.jooby.Kooby
import org.jooby.apitool.ApiTool
import org.jooby.flyway.Flywaydb
import org.jooby.jdbc.Jdbc
import org.jooby.json.Gzon
import org.jooby.run

fun main(args: Array<String>) {
    run(::App, *args)
}

class App : Kooby({
    use(Infrastructure())
    use(Coupons())

    use(ApiTool().swagger()
            .filter { route -> route.pattern().startsWith("/v1/") })

})

class Infrastructure : Kooby({
    use(Gzon().doWith { config -> config.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES) })
    use(Jdbc(couponsDbQualifier)
            .doWith { t -> t.dataSourceProperties.remove("url") }) // bcs postgres datasource has no url
    use(Exposed(couponsDbQualifier))
    use(Flywaydb(couponsDbQualifier))
})

//todo: use props?
const val couponsDbQualifier = "db.coupons"

fun Jooby.coupons() = require(couponsDbQualifier, Database::class.java)

class Wrapper(val kooby: Kooby) {
    fun somth() = kooby.require(Config::class.java)
    fun Jooby.coupons() = require(couponsDbQualifier, Database::class.java)

}