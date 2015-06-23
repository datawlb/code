package dbscan

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by datawlb on 2015/5/28.
 */
class ballTree  (
                        var minLeafNB: Int,
                        val pointNB: Int
                         )extends Serializable {
  // default
  //def this() = this(40, )
  var indexs = new Array[Int](pointNB)

  def setMinLeafNB(minLeafNB: Int): this.type  = {
    this.minLeafNB = minLeafNB
    this
  }

  def buildBallTree(input: Array[Array[Double]]): Option[ballNode] = {
    require(this.minLeafNB >= 10)
    for (i <- 0 until input.length)
      this.indexs(i) = i
    val pivot = getPivot(input, this.indexs, 0, input.length-1)
    val radius = getRadius(input, this.indexs, 0, input.length-1, pivot)
    val root = Option(new ballNode(1, 0, input.length-1, pivot, radius, Option.empty, Option.empty))
    localBuildBallTree(input, root, 1)
    root
  }

  def localBuildBallTree(input: Array[Array[Double]], node: Option[ballNode], depth: Int): Unit = {
    if ((node.get.idxEnd-node.get.idxStart) < this.minLeafNB || node.get.radius < 0.00001)
      Option.empty
    else {
      splitNode(input, node)
      localBuildBallTree(input, node.get.leftNode, depth+1)
      localBuildBallTree(input, node.get.rightNode, depth+1)
      //print(node.get.id + "  " + node.get.radius + "  ")
      // weka have this step(update radius),but if use,the radius will reduce,the search result precision will reduce.
      //node.get.radius = (node.get.leftNode.get.radius+node.get.rightNode.get.radius+ballTree.getDistance(
      //node.get.leftNode.get.pivot, node.get.rightNode.get.pivot))/2
      //println(node.get.radius + "  ")
    }
  }

  def splitNode(input: Array[Array[Double]], node: Option[ballNode]): Unit ={

    // 1.get furthest1: furthest far from node's pivot
    var maxDist = 0.0
    var tempDist = 0.0
    var idx = node.get.idxStart
    for(i <- node.get.idxStart until node.get.idxEnd+1){
      tempDist = getDistance(input(this.indexs(i)), node.get.pivot)
      if (tempDist > maxDist){
        maxDist = tempDist
        idx = i
      }
    }
    val furthest1 = input(this.indexs(idx))
    // 2.get furthest2: furthest far from furthest1
    idx = node.get.idxStart
    maxDist = 0
    for(i <- node.get.idxStart until node.get.idxEnd+1){
      tempDist = getDistance(input(this.indexs(i)), furthest1)
      if (tempDist > maxDist){
        maxDist = tempDist
        idx = i
      }
    }
    val furthest2 = input(this.indexs(idx))

    // 3.update indexs, really split node
    var split = node.get.idxEnd
    idx = node.get.idxStart
    while (idx <= split){
      // maxDist: distance far from furthest1, tempDist: distance far from furthest2
      // furthest1 in left, furthest2 in right
      maxDist = getDistance(input(indexs(idx)), furthest1)
      tempDist = getDistance(input(indexs(idx)), furthest2)
      if (maxDist >= tempDist){
        val temp = indexs(idx)
        indexs(idx) = indexs(split)
        if (idx != split){
          indexs(split) = temp
          split = split - 1
        }else{
          idx = idx + 1
        }
      }else{
        idx = idx + 1
      }
    }

    // 4.update node info
    // leftNode:idxStart->split-1, rightNode:split->idxEnd
    val pivotL = getPivot(input, indexs, node.get.idxStart, split-1)
    val radiusL = getRadius(input, indexs, node.get.idxStart, split-1, pivotL)
    node.get.leftNode = Option(new ballNode(node.get.id*2, node.get.idxStart, split-1, pivotL, radiusL, Option.empty, Option.empty))
    val pivotR = getPivot(input, indexs, split, node.get.idxEnd)
    val radiusR = getRadius(input, indexs, split, node.get.idxEnd, pivotR)
    node.get.rightNode = Option(new ballNode(node.get.id*2+1, split, node.get.idxEnd, pivotR, radiusR, Option.empty, Option.empty))
  }

  def searchNN(input: Array[Array[Double]], node: Option[ballNode], target: Array[Double], epsilon: Double): ArrayBuffer[Int] = {
    val result = new ArrayBuffer[Int]()

    kNeighbours(input, node, target, epsilon, result)
    result
  }

  def kNeighbours(input: Array[Array[Double]], node: Option[ballNode], target: Array[Double], epsilon: Double, result: ArrayBuffer[Int]): Unit ={
    val nodeDist = getDistance(target, node.get.pivot) - node.get.radius

    if (epsilon > nodeDist) {

      if (node.get.leftNode.nonEmpty && node.get.rightNode.nonEmpty) {
        //
        kNeighbours(input, node.get.leftNode, target, epsilon, result)
        kNeighbours(input, node.get.rightNode, target, epsilon, result)
      } else if (node.get.leftNode.nonEmpty || node.get.rightNode.nonEmpty) {
        //
        throw new Exception("This node only one leaf, Unreasonable!")
      } else if (node.get.leftNode.isEmpty && node.get.rightNode.isEmpty) {
        //
        //println(node.get.id)
        for (i <- node.get.idxStart until node.get.idxEnd + 1) {
          if (getDistance(target, input(this.indexs(i))) < epsilon) {
            result.append(this.indexs(i))
          }
        }
      } else {
        //println(node.get.id + "radius")
      }
    }
  }
  def getPivot(input: Array[Array[Double]], indexs: Array[Int], idxStart: Int, idxEnd: Int): Array[Double] = {
    require(idxEnd>=idxStart)
    var pivot = new Array[Double](input(0).length)
    for (i <- idxStart until idxEnd+1){
      for (j <- 0 until pivot.length){
        pivot(j) = pivot(j) + input(indexs(i))(j)
      }
    }
    val length = idxEnd - idxStart + 1
    pivot.map(x => x/length)
    pivot
  }

  def getRadius(input: Array[Array[Double]], indexs: Array[Int], idxStart: Int, idxEnd: Int, pivot: Array[Double]): Double = {
    var radius: Double = 0
    var temp: Double = 0
    for (i <- idxStart until idxEnd + 1){
      temp = getDistance(pivot, input(indexs(i)))
      if (temp > radius)
        radius = temp
    }
    radius
  }

  def getDistance(A: Array[Double], B: Array[Double]): Double = {
    var dis = 0.0
    for (i <- 0 until A.length){
      val difference = A(i) - B(i)
      dis = dis + difference * difference
    }
    math.sqrt(dis)
  }
}

/*
object ballTree extends Serializable{
  var btree: ballTree
  //var root: Option[ballNode]
  def apply(minLeafNB: Int, pointNB: Int): Unit = {
    btree.setMinLeafNB(minLeafNB)
    btree = new ballTree(minLeafNB, pointNB)
  }
  def run(
           input: Array[Array[Double]]
             ): Option[ballNode] = {
    //val bt = new ballTree(minLeafNB, input.length)
    val root = btree.buildBallTree(input)
    root
  }
  def findEpsilon(input: Array[Array[Double]], target: Array[Double], epsilon: Double): ArrayBuffer[Int] ={
    val result = new ArrayBuffer[Int]()
    btree.kNeighbours(input, root, target, epsilon, result)
    result
  }

  //def predict()

  def getPivot(input: Array[Array[Double]], indexs: Array[Int], idxStart: Int, idxEnd: Int): Array[Double] = {
    require(idxEnd>=idxStart)
    var pivot = new Array[Double](input(0).length)
    for (i <- idxStart until idxEnd+1){
      for (j <- 0 until pivot.length){
        pivot(j) = pivot(j) + input(indexs(i))(j)
      }
    }
    val length = idxEnd - idxStart + 1
    pivot.map(x => x/length)
    pivot
  }

  def getRadius(input: Array[Array[Double]], indexs: Array[Int], idxStart: Int, idxEnd: Int, pivot: Array[Double]): Double = {
    var radius: Double = 0
    var temp: Double = 0
    for (i <- idxStart until idxEnd + 1){
      temp = getDistance(pivot, input(indexs(i)))
      if (temp > radius)
        radius = temp
    }
    radius
  }

  def getDistance(A: Array[Double], B: Array[Double]): Double = {
    var dis = 0.0
    for (i <- 0 until A.length){
      val difference = A(i) - B(i)
      dis = dis + difference * difference
    }
    math.sqrt(dis)
  }

  def main (args: Array[String]) {
/*
    val points = new ArrayBuffer[Array[Double]]()
    val label = new ArrayBuffer[String]()
    for (line <- Source.fromFile("C:\\Users\\Administrator\\Desktop\\iris.data").getLines){
      val lineSplit = line.split(",")
      val data = lineSplit.splitAt(lineSplit.length-1)
      val arr = data._1.map(x => x.toDouble)
      points.append(arr)
      label.append(data._2.head)
    }
*/
///*
    val points = new ArrayBuffer[Array[Double]]()
    var pointID: Int = 0
    for (line <- Source.fromFile("C:\\Users\\Administrator\\Desktop\\horizonData30w.dat").getLines){    // ("/root/Desktop/horizonData.dat").getLines){    // /home/wanglb/horizonData.dat
    val tempArray = line.split("\t").map(x => x.toDouble).splitAt(2)._2
      pointID = pointID + 1
      points.append(tempArray)
    }
//*/
    val pointsArr = points.toArray
    points.clear()
    val resBallTree = run(pointsArr, pointsArr.take(30), 100, 5)


    // use N2 test
    val bt = new ballTree(40, pointsArr.length)
    for (i <- 0 until pointsArr.length)
      bt.indexs(i) = i
    val resN2 = bt.findNeighboursN2(bt.indexs.zip(pointsArr), pointsArr(29), 5)

    println(" ")
  }
}
// compare with kdtree,balltree cost more time in the build tree,and the knn precision may small than kdtree.

*/