package org.fyde

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.fyde.coupons.Api
import org.jooby.Kooby
import org.jooby.apitool.ApiTool
import org.jooby.json.Jackson
import org.jooby.run

fun main(args: Array<String>) {
    run(::App, *args)
}

class App : Kooby({
    use(Jackson(ObjectMapper().registerKotlinModule()))

    use(Api())

    use(ApiTool().swagger("/swagger")
            .filter { route -> route.pattern().startsWith("/v1/") })

})