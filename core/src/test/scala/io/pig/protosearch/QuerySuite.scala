package io.pig.protosearch

import io.pig.lucille.Parser

class QuerySuite extends munit.FunSuite {

  val index = CatIndex.index

  test("TermQ") {
    val q = Parser.parseQ("fast").map(_.head)
    assertEquals(
      q.flatMap(q => BooleanQuery.search(index, q)),
      Right(List((1, 0.6931471805599453))),
    )
  }

  test("AndQ") {
    val q = Parser.parseQ("fast AND cat").map(_.head)
    assertEquals(
      q.flatMap(q => BooleanQuery.search(index, q)),
      Right(List((1, 0.9241962407465937))),
    )
  }

  test("Double AndQ") {
    val q = Parser.parseQ("the AND fast AND cat").map(_.head)
    assertEquals(
      q.flatMap(q => BooleanQuery.search(index, q)),
      Right(List((1, 1.4735023850806486))),
    )
  }

  test("OrQ") {
    val q = Parser.parseQ("fast OR cat").map(_.head)
    val results = List(
      (0, 0.23104906018664842),
      (1, 0.9241962407465937),
      (2, 0.23104906018664842),
    )
    assertEquals(q.flatMap(q => BooleanQuery.search(index, q)), Right(results))
  }

  test("Double OrQ") {
    val q = Parser.parseQ("the OR fast OR cat").map(_.head)
    val results = List(
      (0, 0.7803552045207033),
      (1, 1.4735023850806486),
      (2, 0.23104906018664842),
    )
    assertEquals(q.flatMap(q => BooleanQuery.search(index, q)), Right(results))
  }

}
