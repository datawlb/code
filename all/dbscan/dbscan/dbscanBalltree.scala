package dbscan

import java.io.PrintWriter

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by datawlb on 2015/6/19.
 */
class dbscanBalltree private(
                      private var epsilon: Double,
                      private var minPoints: Int
                      ) extends Serializable{
  // labelID count
  var labelID: Int = 1

  def this() = this(0.1, 5)

  def setEpsilon(value: Double): this.type = {
    this.epsilon = value
    this
  }

  def setMinPoints(value: Int): this.type = {
    this.minPoints = value
    this
  }
  def getDistance(A: Array[Double], B: Array[Double]): Double = {
    var dis = 0.0
    for (i <- 0 until A.length){
      val difference = A(i) - B(i)
      dis = dis + difference * difference
    }
    math.sqrt(dis)
  }
  def run(input: Array[Array[Double]]): Array[Int] = {
    /* build balltree to find nn  */
    val btree = new ballTree(100, input.length)
    val root = btree.buildBallTree(input)
    // use eg: val res = btree.searchNN(pointsArr, root, pointsArr(0), epsilon)

    var ii=0
    require(minPoints > 0)
    require(epsilon > 0)
    // label: -1 is noise, 0 is un classify
    val result = new Array[Int](input.length)
    var nnPoints = new ArrayBuffer[Int]()
    var nnTempPoints = new ArrayBuffer[Int]()
    var tempIdx = 0
    // scan each point,that unscanned
    for(i<- 0 until input.length){

      if(result(i) == 0){
        println("0current for i: " + i)
        // find nnPoints for current point
        nnPoints.clear()
        btree.kNeighbours(input, root, input(i), epsilon, nnPoints)
        nnPoints = nnPoints.sortBy(x => x)
        // corePoint or no
        if (nnPoints.length-1 > minPoints){
          result(i) = labelID
          nnPoints.foreach{idx =>
            if(idx==i){
              nnPoints
            } else {
              if (result(idx)<=0 ){
                result(idx) = labelID
                //ii+=1
                //println(ii)
              }
            }
          }

          while (nnPoints.nonEmpty){
            tempIdx = nnPoints.remove(0)
            if (tempIdx!=i){
              // find nnPoints for current point
              nnTempPoints.clear()
              btree.kNeighbours(input, root, input(tempIdx), epsilon, nnTempPoints)
              nnTempPoints = nnTempPoints.sortBy(x => x)
              if (nnTempPoints.length-1 > minPoints){
                nnTempPoints.foreach{idx =>
                  if(idx != tempIdx){
                    if(result(idx)<0 ){
                      result(idx) = labelID
                      //ii+=1
                      //println(ii)
                    }else if (result(idx)==0 ){
                      result(idx) = labelID
                      nnPoints.append(idx)
                      ii+=1
                      println(ii)
                    }
                  }

                }
              }
            }



          }
          labelID  = labelID + 1
          println("current labelID is:" + labelID)
        }else{
          result(i) = -1
        }
      }

    }
    result
  }
  // TODO: use kdtree or balltree fand nn.

}

object dbscanBalltree {
  def train(input: Array[Array[Double]],
            epsilon: Double,
            minPoints: Int): Array[Int] = {

    new dbscanBalltree().setEpsilon(epsilon).setMinPoints(minPoints).run(input)
  }

  // test
  def main (args: Array[String]) {

    ///*
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
        val result = train(pointsArr, 0.5,3)
    //*/
    /*
    val points = new ArrayBuffer[Array[Double]]()
    val pos = new ArrayBuffer[Array[Double]]()
    var pointID: Int = 0
    for (line <- Source.fromFile("C:\\Users\\Administrator\\Desktop\\horizonData30w.dat").getLines){    // ("/root/Desktop/horizonData.dat").getLines){    // /home/wanglb/horizonData.dat
    val tempArray = line.split("\t").map(x => x.toDouble).splitAt(2)
      pointID = pointID + 1
      points.append(tempArray._2)
      pos.append(tempArray._1)
    }

    val pointsArr = points.toArray
    points.clear()

    // test balltree
    //val btree = new ballTree(100, pointsArr.length)
    //val root = btree.buildBallTree(pointsArr)
    //val res = btree.searchNN(pointsArr, root, pointsArr(0), 100)
    val tstart = System.nanoTime()
    val result = train(pointsArr.take(50000), 0.02,5)
    println((System.nanoTime()-tstart)/1e9)


    val out = new PrintWriter("C:\\Users\\Administrator\\Desktop\\horizonResultDBSCANballtree.txt")///root/Desktop/horizonDataResult.txt
    for(i <- 0 until result.length){
      out.print(pos(i)(0))
      out.print("\t")
      out.print(pos(i)(1))
      out.print(result(i))
      out.println()
    }
    out.close()
    */
    val countMap = scala.collection.mutable.Map[Int, Int]()
    result.foreach{label =>
      if (countMap.contains(label))
        countMap(label) = countMap(label) + 1
      else
        countMap.put(label, 1)
    }
    countMap.foreach(ct => println("labelID:" + ct._1 + "  " + ct._2))
    println(countMap.size)
    println(" ")
  }
}
