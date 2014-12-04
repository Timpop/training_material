package com.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
object JoinExample {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    case class Register (d: java.util.Date, uuid: String, cust_id: String, lat: Float, lng: Float)
    case class Click (d: java.util.Date, uuid: String, landing_page: Int)

    val reg: RDD[(String, Register)] = sc.textFile("data/reg.tsv").map(_.split("\t")).map(
      r => (r(1), Register(format.parse(r(0)), r(1), r(2), r(3).toFloat, r(4).toFloat))
    )

    val clk = sc.textFile("data/clk.tsv").map(_.split("\t")).map(
      c => (c(1), Click(format.parse(c(0)), c(1), c(2).trim.toInt))
    )

    (reg join clk) take 2 foreach println
  }
}
