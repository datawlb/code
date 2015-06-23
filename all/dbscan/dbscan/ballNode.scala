package dbscan

/**
 * Created by datawlb on 2015/5/28.
 */
class ballNode(
              val id: Int,
              val idxStart: Int,
              val idxEnd: Int,
              val pivot: Array[Double],
              var radius: Double,
              var leftNode: Option[ballNode],
              var rightNode: Option[ballNode]
                ) extends Serializable {
  def preOrder(root: Option[ballNode], searchMap: scala.collection.mutable.Map[Int, Array[Double]]): Unit = {
    // get
    // val searchArray = ArrayBuffer[Array[Double]]()  val scores = scala.collection.mutable.Map(

    if (root.nonEmpty) {
      searchMap.put(root.get.id, root.get.pivot)
      if (root.get.leftNode.nonEmpty)
        preOrder(root.get.leftNode, searchMap)
      if (root.get.rightNode.nonEmpty)
        preOrder(root.get.rightNode, searchMap)
    }
  }
}

object ballNode {
  def apply(
    id: Int,
    idxStart: Int,
    idxEnd: Int,
    pivot: Array[Double],
    radius: Double,
    leftNode: Option[ballNode],
    rightNode: Option[ballNode]): ballNode = {
    new ballNode(id, idxStart, idxEnd, pivot, radius, leftNode, rightNode)
  }
}
