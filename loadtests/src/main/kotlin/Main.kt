@file:JvmName("Main")

import com.github.myzhan.locust4j.AbstractTask
import com.github.myzhan.locust4j.Locust
import com.jayway.restassured.RestAssured.given

class OpenApplicationTask(private val weight: Int) : AbstractTask() {

    override fun getWeight(): Int {
        return weight
    }


    override fun getName(): String {
        return "Open application task"
    }

    override fun execute() {
        println("Main execute: ${Thread.currentThread().name}")
        try {
            val response = given().get("http://localhost:4567")

            Locust.getInstance().recordSuccess("http", name, response.time, 1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    val locust = Locust.getInstance()
    locust.setMasterHost("127.0.0.1")
    locust.setMasterPort(5557) //some free port to run the Locust slave
    locust.run(OpenApplicationTask(50))
}