package ballTree.test

import ballTree.main.ballTree
import com.twitter.algebird.ArrayBufferedOperation

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by datawlb on 2015/5/28.


object ballTest extends Serializable{
  //def apply(): Unit = {}
  def main (args: Array[String]) {
    val points = new ArrayBuffer[Array[Double]]()
    val label = new ArrayBuffer[String]()
    for (line <- Source.fromFile("C:\\Users\\Administrator\\Desktop\\iris.data").getLines){
      val lineSplit = line.split(",")
      val data = lineSplit.splitAt(lineSplit.length-1)
      val arr = data._1.map(x => x.toDouble)
      points.append(arr)
      label.append(data._2.head)
    }
    val pointsArr = points.toArray
    points.clear()

    val bt = new ballTree(40, pointsArr)
    val root = bt.buildBallTree(pointsArr)
    val res = bt.searchNN(pointsArr, root._2, pointsArr(0), 5)

    //val ballRoot = ballTree.run(points.toArray, 10)

    println(" ")
  }
}
*/