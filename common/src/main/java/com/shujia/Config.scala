package com.shujia

import java.io.InputStream
import java.util.Properties

/** *
 * 该类是用来得到配置文件的具体的实现类
 */
object Config {
  private val inputStream: InputStream = this.getClass // 通过反射得到类对象
    .getClassLoader
    .getResourceAsStream("Config.properties") // 得到资源的输入流

  private val properties = new Properties()

  properties.load(inputStream) // 加载配置文件

  def getConfig(key: String): String = {
    properties.getProperty(key) // 得到配置文件的配置
  }

}
