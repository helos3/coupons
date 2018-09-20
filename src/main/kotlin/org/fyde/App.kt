package org.fyde

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import org.fyde.coupons.Coupons
import org.jooby.Kooby
import org.jooby.apitool.ApiTool
import org.jooby.jdbc.Jdbc
import org.jooby.run
import org.jooby.json.Gzon



fun main(args: Array<String>) {
    run(::App, *args)
}

class App : Kooby({

    use(Gzon().doWith {config -> config.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)})

    use(Jdbc("db")
            .doWith { t -> t.dataSourceProperties.remove("url") }) // bcs postgres datasource has no url
    use(Exposed())
    use(Coupons())

    use(ApiTool().swagger("/swagger")
            .filter { route -> route.pattern().startsWith("/v1/") })



})