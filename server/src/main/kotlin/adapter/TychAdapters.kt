package adapter

import com.github.salomonbrys.kotson.typeAdapter
import java.lang.UnsupportedOperationException
import java.util.*
import TychRequest
import Position
import TychResponse

val tychRequestAdapter = typeAdapter<TychRequest> {
    write {
        throw UnsupportedOperationException("This should not be used.")
    }
    read {
        beginArray()
        val xpos = nextDouble()
        val ypos = nextDouble()
        endArray()
        TychRequest(Position(xpos, ypos), Date().time)
    }
}

val tychResponseAdapter = typeAdapter<TychResponse> {
    write {
        beginArray()
        value(it.position.x)
        value(it.position.y)
        value(it.radius)
        value(it.shrinkSpeed)
        endArray()
    }
    read {
        throw UnsupportedOperationException("This should not be used.")
    }
}