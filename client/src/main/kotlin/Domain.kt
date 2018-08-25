data class Position(val x: Double, val y: Double)
data class Tych(val position: Position,
                var currentRadius: Double,
                val shrinkSpeedPerSecond: Double)
data class Scoreboard(val values: Map<String, Int>)