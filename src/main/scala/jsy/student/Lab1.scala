package jsy.student

import jsy.lab1._

object Lab1 extends jsy.util.JsyApplication with jsy.lab1.Lab1Like {
  import jsy.lab1.Parser
  import jsy.lab1.ast._

  /*
   * CSCI 3155: Lab 1
   * Sam Leon, Michael Godhe
   *
   * Partner: <Your Partner's Name>
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   *
   * Replace the '???' expression with your code in each function. The
   * '???' expression is a Scala expression that throws a NotImplementedError
   * exception.
   *
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   *
   * Your lab will not be graded if it does not compile.
   *
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * '???' as needed to get something that compiles without error.
   */

  /*
   * Example: Test-driven development of plus
   *
   * A convenient, quick-and-dirty way to experiment, especially with small code
   * fragments, is to use the interactive Scala interpreter. The simplest way
   * to use the interactive Scala interpreter in IntelliJ is through a worksheet,
   * such as Lab1Worksheet.sc. A Scala Worksheet (e.g., Lab1Worksheet.sc) is code
   * evaluated in the context of the project with results for each expression
   * shown inline.
   *
   * Step 0: Sketch an implementation in Lab1.scala using ??? for unimmplemented things.
   * Step 1: Do some experimentation in Lab1Worksheet.sc.
   * Step 2: Write a test in Lab1Spec.scala, which should initially fail because of the ???.
   * Step 3: Fill in the ??? to finish the implementation to make your test pass.
   */

  //def plus(x: Int, y: Int): Int = ???
  def plus(x: Int, y: Int): Int = x + y

  /* Exercises */

  def abs(n: Double): Double = {if(n < 0) -n else n;}

  def xor(a: Boolean, b: Boolean): Boolean =
  {
    if(a == b) false else true;
  }

  def repeat(s: String, n: Int): String =
  {
    if (n < 0) throw new IllegalArgumentException("Int n must be greater than or equal to 0.");
    else if (n == 0) "";
    else s * n;
  }

  def sqrtStep(c: Double, xn: Double): Double = xn-(xn*xn-c)/(2*xn);

  def sqrtN(c: Double, x0: Double, n: Int): Double =
  {
    if (n < 0) throw new IllegalArgumentException("Int n must be greater than or equal to 0.");
    else if (n == 0) x0;
    else if (n!=0) sqrtN(c, sqrtStep(c, x0), n-1);
    else sqrtStep(c, x0);
  }


  def sqrtErr(c: Double, x0: Double, epsilon: Double): Double =
  {
    require(epsilon>0)
    val err:Double=abs(x0*x0-c)
    if(err<epsilon) x0 else sqrtErr(c, sqrtStep(c, x0), epsilon)
  }

  def sqrt(c: Double): Double = {
    require(c >= 0)
    if (c == 0) 0 else sqrtErr(c, 1.0, 0.0001)
  }

  /* Search Tree */

  // Defined in Lab1Like.scala:
  //
  // sealed abstract class SearchTree
  // case object Empty extends SearchTree
  // case class Node(l: SearchTree, d: Int, r: SearchTree) extends SearchTree

  def repOk(t: SearchTree): Boolean = {
    def check(t: SearchTree, min: Int, max: Int): Boolean = t match {
      case Empty => true
      case Node(l, d, r) => {
        val n=t.asInstanceOf[Node]
        if(n.d>=min && n.d<=max) check(n.l, min, n.d) && check(n.r, n.d, max) else false
      }
    }
    check(t, Int.MinValue, Int.MaxValue)
  }

  def insert(t: SearchTree, n: Int): SearchTree =
  {
    t match
    {
      case Empty => Node(Empty, n, Empty)
      case Node(l, d, r) => {
        val node=t.asInstanceOf[Node]
        if (node.d>n) Node(insert(node.l, n), node.d, node.r) else Node(node.l, node.d, insert(node.r, n))
      }
    }
  }

  def deleteMin(t: SearchTree): (SearchTree, Int) = {
    require(t != Empty)
    (t: @unchecked) match {
      case Node(Empty, d, Empty) => (Empty, d)
      case Node(Empty, d, r) => (r, d)
      case Node(l, d, r) =>
        val (l1, m) = deleteMin(l)
        (Node(l1.asInstanceOf[SearchTree], t.asInstanceOf[Node].d, t.asInstanceOf[Node].r), m)
    }
  }

  def insertmerge(t: SearchTree, n: SearchTree): SearchTree =
  {
    t match
    {
      case Empty => n
      case Node(l, d, r) => {
        val node=t.asInstanceOf[Node]
        if (node.d>n.asInstanceOf[Node].d) Node(insertmerge(node.l, n), node.d, node.r) else Node(node.l, node.d, insertmerge(node.r, n))
      }
    }
  }

  def mergesubtrees(l: SearchTree, r: SearchTree): SearchTree =
  {
    if(l==Empty) r
    if(r==Empty) l

    //General idea: r is greater than l, so we can merge the entire tree l in r somewhere.
    insertmerge(r, l)
  }

  def delete(t: SearchTree, n: Int): SearchTree =
  {
    require(t != Empty)
    t match
    {
      case Empty => Empty
      case Node(Empty, d, Empty) => if(d==n) Empty else t
      case Node(Empty, d ,r) => if(d == n) r else Node(Empty, d, delete(r, n))
      case Node(l, d, Empty) => if(d==n) l else Node(delete(l, n), d, Empty)
      case Node(l, d, r) => if(d==n) mergesubtrees(l, r) else{if(n>d) Node(l, d, delete(r, n)) else Node(delete(l, n), d, r)}
    }
  }

  /* JavaScripty */

  def eval(e: Expr): Double = e match {
    case N(n) => { // This represents a value
        n
      }
    case Unary(uo, exp) => {
      uo match {
        case Neg => -1*eval(exp)
        // Just fail through:
        case _ => eval(exp)
      }
    }
    case Binary(b, e1, e2) => {
      b match {
        case Plus => eval(e1) + eval(e2)
        case Minus => eval(e1) - eval(e2)
        case Times => eval(e1) * eval(e2)
        case Div => eval(e1) / eval(e2)
      }
    }
    case _ => {
      0.0
    }
  }

 // Interface to run your interpreter from a string.  This is convenient
 // for unit testing.
 def eval(s: String): Double = eval(Parser.parse(s))

 /* Interface to run your interpreter from the command-line.  You can ignore the code below. */

 def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }

    val expr = Parser.parseFile(file)

    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }

    if (debug) { println("Evaluating ...") }

    val v = eval(expr)

    println(prettyNumber(v))
  }

}
