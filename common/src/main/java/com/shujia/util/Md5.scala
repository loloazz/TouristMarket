package com.shujia.util

import java.security.MessageDigest

object Md5 {
  private def hashMD5(content: String): String = {
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    val encoded: Array[Byte] = md5.digest(content.getBytes)
    encoded.map("%02x".format(_)).mkString
  }

  def md5(content: String): String = {
    val str1: String = hashMD5(content + "shujia")
    val str2: String = hashMD5(str1 + "scala")
    str2.toUpperCase
  }

  def main(args: Array[String]): Unit = {
    println(md5("17600110022"))
  }

}