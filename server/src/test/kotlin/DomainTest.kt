import org.junit.Test
import kotlin.test.assertTrue

class DomainTest {

  @Test
  fun test() {
    val tych1 = Tych(User(score = 100), Position(0.0, 0.0), 0)
    val tych2 = Tych(User(score = 99), Position(0.0, 0.0), 0)
    assertTrue(tych1.(getConsumes())(tych2))
  }
}