package org.fyde

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.fyde.coupons.Coupons
import org.jooby.Kooby
import org.jooby.apitool.ApiTool
import org.jooby.jdbc.Jdbc
import org.jooby.json.Jackson
import org.jooby.run

fun main(args: Array<String>) {
    run(::App, *args)
}

class App : Kooby({
    use(Jackson(ObjectMapper().registerKotlinModule()))
    use(Jdbc("db")
            .doWith { t -> t.dataSourceProperties.remove("url") }) // bcs postgres datasource has no url
    use(Exposed())
    use(Coupons())

    use(ApiTool().swagger("/swagger")
            .filter { route -> route.pattern().startsWith("/v1/") })

})