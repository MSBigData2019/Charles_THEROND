package com.apprentissage


import org.apache.spark.{SparkConf, SparkContext}

object apprentissage {

  def main(args: Array[String]): Unit = {
      //create spark conf and context
      val spconf = new SparkConf()
      spconf.setMaster("local")
      spconf.setAppName("apprentissage")

      val sc = new SparkContext(spconf)
      Console.println("Mon context")
      Console.println(sc)
  }
}
