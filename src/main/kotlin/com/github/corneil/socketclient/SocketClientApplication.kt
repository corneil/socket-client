package com.github.corneil.socketclient


import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.time.Instant
import java.util.logging.LogManager
import java.util.logging.Logger


fun initLogging() {
  val file = File("logging.properties")
  val stream = (if (file.isFile) file.inputStream() else
        ClassLoader.getSystemClassLoader().getResourceAsStream("logging.properties")
  ) ?: throw RuntimeException("Cannot find logging.properties")
  LogManager.getLogManager().readConfiguration(stream);
}


fun main(args: Array<String>) {
  initLogging()
  val logger = Logger.getLogger("socket-client")
  if (args.isNotEmpty()) {
    System.setProperty("app.host", args[0])
  }
  if (args.size >= 2) {
    System.setProperty("app.port", args[1])
  }
  val port = System.getProperty("app.port", "9000");
  val host = System.getProperty("app.host", "localhost")
  var downTime = 0L
  while (true) {
    val start = System.currentTimeMillis()
    try {
      downTime += runClient(host, port, logger)
      logger.info("DOWN:${downTime}ms")
    } catch (x: Throwable) {
      downTime += System.currentTimeMillis() - start;
      println(7.toChar())
      logger.info("DOWN:${downTime}ms:$x")
    }
    Thread.sleep(1000)
  }
}

private fun runClient(host: String?, port: String, logger: Logger): Long {
  val start = System.currentTimeMillis()
  logger.info("Connecting to $host:$port")
  val client = Socket(host, port.toInt())
  val output = PrintWriter(client.getOutputStream(), true)
  val input = BufferedReader(InputStreamReader(client.getInputStream()))
  var up = false
  var upTimeStamp = 0L
  while (true) {
    val ts = Instant.now()
    logger.fine("Sending:$ts")
    output.println(ts.toString())
    val response = input.readLine()
    if (response != null) {
      logger.fine("Received:$response")
      if (!up) {
        upTimeStamp = System.currentTimeMillis()
        up = true
        logger.info("UP")
      }
      val responseTime = Instant.now().toEpochMilli() - ts.toEpochMilli()
      logger.info("Response:${response.trim()}:in ${responseTime}ms")
      Thread.sleep(5000)
    } else {
      logger.info("Response:null")
      client.close()
      break

    }
  }
  return System.currentTimeMillis() - upTimeStamp - start
}
