package org.fyde

import com.google.inject.Binder
import com.google.inject.Key
import com.google.inject.name.Names
import com.typesafe.config.Config
import org.jetbrains.exposed.sql.Database
import org.jooby.*
import java.util.NoSuchElementException
import javax.sql.DataSource


fun <K, T> mapFrom(keys: Collection<K>, init: (K) -> T): Map<K, T> = keys.map { it to init(it) }.toMap()

fun <K, T> mapFrom(keys: Array<K>, init: (K) -> T): Map<K, T> = keys.map { it to init(it) }.toMap()


class Exposed(val name: String = "db") : Jooby.Module {
    override fun configure(env: Env, conf: Config, binder: Binder) {
        val dskey = Key.get(DataSource::class.java, Names.named(name))
        val ds = env.get(dskey)
                .orElseThrow { NoSuchElementException("DataSource missing: $dskey") }
        val db = Database.connect(ds)
        env.serviceKey().generate(Database::class.java, name) { key -> binder.bind(key).toInstance(db) }
    }
}

fun <T: Any?> T.toResult() = this?.let { Results.json(it) } ?: Results.noContent()