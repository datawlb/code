package org.test

import scala.util.Random

/**
 * Created by datawlb on 2015/5/10.
 */
object ReservoirSampling {
  def reservoirSample(data: Array[Int], k: Int): Array[Int] = {
    val result = new Array[Int](k)
    for(i<-0 until(data.length)){
      if (i<k)
        result(i) = data(i)
      else{
        val rand1 = new Random().nextInt(i+1)
        if(rand1 < k)
          result(rand1) = data(i)
      }
    }
    result
  }
  def main (args: Array[String]) {
    val k = 50
    val n = 1000
    val data = new Array[Int](n)
    for(i<-0 until(n))
      data(i) = i
    println(reservoirSample(data, k).length)
  }
}
