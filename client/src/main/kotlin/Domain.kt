data class Position(val x: Double, val y: Double)
data class Tych(var position: Position,
                var currentRadius: Double,
                var shrinkSpeedPerSecond: Double)
data class Scoreboard(val values: Map<String, Int>)