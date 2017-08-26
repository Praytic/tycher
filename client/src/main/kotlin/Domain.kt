data class Position(val x: Double, val y: Double)
data class Tych(val position: Position,
                var currentRadius: Double,
                val shrinkSpeed: Double)