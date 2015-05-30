package ballTree.main

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
