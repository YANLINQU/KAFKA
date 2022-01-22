package demo_partition.producer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

object KafkaProducerDemo {

  def get_prop(): Properties = {
    val prop = new Properties
    // Specify the requested kafka cluster list
    prop.put("bootstrap.servers", "menthepourrie:9092")
    /**
     * The number of acknowledgments the producer requires the leader to have received before considering a request complete.
     * This controls the durability of records that are sent. The following settings are allowed:
     */
    prop.put("acks", "all")
    // Request failed retries
    prop.put("retries", "3")
    // Serializer class for key that implements the
    prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // Serializer class for value that implements the
    prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // The configuration controls the maximum amount of time the client will wait for the response of a request.
    prop.put("request.timeout.ms", "60000")
    prop
  }

  def main(args: Array[String]): Unit = {
    // Get the instance of the producer
    val producer = new KafkaProducer[String, String](get_prop)
    // Send a number from 1-100 one by one to first_topic
    for (i <- 1 to 100) {
      val msg = s"${i}: this is a first_topic ${i} kafka data"
      println("send -->" + msg)
      val rmd: RecordMetadata = ((i % 2 == 0) match {
        case true  => producer.send(new ProducerRecord[String, String]("first_topic", 0, i.toString, msg)).get()
        case false => producer.send(new ProducerRecord[String, String]("first_topic", 1, i.toString, msg)).get()
      })
      println(rmd.toString)
      Thread.sleep(500)
    }
    producer.close()
  }
}
