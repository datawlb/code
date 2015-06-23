package dbscan

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by datawlb on 2015/6/15.
 */
class dbscan private(
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
    var ii=0
    require(minPoints > 0)
    require(epsilon > 0)
    // label: -1 is noise, 0 is un classify
    val result = new Array[Int](input.length)
    val nnPoints = new ArrayBuffer[Int]()
    val nnTempPoints = new ArrayBuffer[Int]()
    var tempIdx = 0
    // scan each point,that unscanned
    for(i<- 0 until input.length){
      println("current for i: " + i)
      if(result(i) == 0){
        // find nnPoints for current point
        nnPoints.clear()
        for(idx<-0 until input.length){
          if(idx!=i){
            if (getDistance(input(idx), input(i)) < epsilon)
              nnPoints.append(idx)
          }
        }
        // corePoint or no
        if (nnPoints.length > minPoints){
          result(i) = labelID
          nnPoints.foreach{idx =>
            if (result(idx)<=0){
              result(idx) = labelID
              ii+=1
              println(ii)
            }
          }

          while (nnPoints.nonEmpty){
            tempIdx = nnPoints.remove(0)
            // find nnPoints for current point
            nnTempPoints.clear()
            for(idx<-0 until input.length){
              if(idx!=tempIdx){
                if (getDistance(input(idx), input(tempIdx)) < epsilon)
                  nnTempPoints.append(idx)
              }
            }

            if (nnTempPoints.length > minPoints){
              nnTempPoints.foreach{idx =>
                if(result(idx)<0){
                  result(idx) = labelID
                  ii+=1
                  println(ii)
                }else if (result(idx)==0){
                  result(idx) = labelID
                  nnPoints.append(idx)
                  ii+=1
                  println(ii)
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

object dbscan {
  def train(input: Array[Array[Double]],
            epsilon: Double,
            minPoints: Int): Array[Int] = {

    new dbscan().setEpsilon(epsilon).setMinPoints(minPoints).run(input)
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
    val result = train(pointsArr, 2.6,10)
//*/
/*
    val points = new ArrayBuffer[Array[Double]]()
    var pointID: Int = 0
    for (line <- Source.fromFile("C:\\Users\\Administrator\\Desktop\\horizonData30w.dat").getLines){    // ("/root/Desktop/horizonData.dat").getLines){    // /home/wanglb/horizonData.dat
    val tempArray = line.split("\t").map(x => x.toDouble).splitAt(2)._2
      pointID = pointID + 1
      points.append(tempArray)
    }
    val pointsArr = points.toArray
    points.clear()
    var sample1 = pointsArr.take(50000)
    val tstart = System.nanoTime()
    val result = train(sample1, 0.02,5)
    println((System.nanoTime()-tstart)/1e9)
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
