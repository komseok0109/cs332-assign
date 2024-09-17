package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = (x: Int) => 1 <= x && 10 >= x
    val s5 = (x: Int) => 7 <= x && 18 >= x
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("funset test1") {
    new TestSets {
      val squaredFilteredSet = map(filter(union(s4, s5), x=> x % 2 == 0), x => x * x)
      assert(forall(squaredFilteredSet, x => x % 4 == 0))
    }
  }

  test("funset test2") {
    new TestSets {
      val emptySet = intersect(s4, singletonSet(100))
      assert(!exists(emptySet, x => true))
    }
  }

  test("funset test3") {
   new TestSets {
     val unionDiffIntersect = diff(union(s4, s5), intersect(s4, s5))
     assert(!exists(unionDiffIntersect, x => 7 <= x && 10 >= x), "funset 3-1")
     assert(contains(unionDiffIntersect, 3), "funset 3-2")
     assert(!contains(unionDiffIntersect, 8), "funset 3-3")
   }
  }

  test("funset test4") {
    new TestSets {
      val mappedSet1 = map(intersect(s4, s5), x => 2 * x + 1)
      val mappedSet2 = map(intersect(s4, s5), x => - x + 25)
      assert(contains(intersect(mappedSet1, mappedSet2), 17))
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection test") {
    new TestSets {
      val s = intersect(s4, s5)
      assert(contains(s, 8), "Intersection 1")
      assert(contains(s, 10), "Intersection 2")
      assert(!contains(s, 14), "Intersection 3")
    }
  }

  test("diff test") {
    new TestSets {
      val s = diff(s4, s5)
      assert(contains(s, 3), "Difference 1")
      assert(contains(s, 5), "Difference 2")
      assert(!contains(s, 8), "Difference 3")
    }
  }

  test("filter test") {
    new TestSets {
      val s = filter(s4, x => x % 2 == 0)
      assert(contains(s, 4), "Filter 1")
      assert(contains(s, 10), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
    }
  }

  test("forall test") {
    assert(forall(x => x >= 200 && x <= 300, x => x < 400 ), "forall 1")
    assert(!forall(x => x >= 200 && x <= 300, x => x > 200 ), "forall 2")
    assert(forall(x => x >= -900 && x <= 900, x => x < 2000 ), "forall 3")
    new TestSets {
      assert(forall(union(s4, s5), x => x <= 20 ), "forall 4")
    }
  }

  test("exists test") {
    assert(!exists(x => x % 2 == 0, x => x % 2 == 1 ), "exists 1")
    new TestSets {
      assert(exists(intersect(s4, s5), x => x % 4 == 0 ), "exists 2")
    }
  }

  test("map test") {
    new TestSets {
      val doubledSet: Set = map(intersect(s4, s5), x => x * 2)
      assert(contains(doubledSet, 20), "map 1")
      assert(contains(doubledSet, 14), "map 2")
      assert(!contains(doubledSet, 8), "map 3")
    }
  }

}
