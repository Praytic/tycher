import spark.Spark

fun main(args: Array<String>) {
    Spark.staticFiles.location("/public") //index.html is served at localhost:4567 (default port)
    Spark.staticFiles.expireTime(600)
    Spark.webSocket("/chat", ChatWebSocketHandler::class.java)
    Spark.init()
}
