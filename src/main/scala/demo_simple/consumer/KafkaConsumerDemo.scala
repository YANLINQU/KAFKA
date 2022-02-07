package demo_simple.consumer

import java.util.{ Collections, Properties }
import org.apache.kafka.clients.consumer.{ ConsumerRecords, KafkaConsumer }

object KafkaConsumerDemo {
  def get_prop(): Properties = {
    val prop = new Properties
    // Specify the requested kafka cluster list
    prop.put("bootstrap.servers", "menthepourrie:9092")
    // A unique string that identifies the consumer group this consumer belongs to
    prop.put("group.id", "group01")
    // Serializer class for key that implements the
    prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    // Serializer class for value that implements the
    prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    prop.put("session.timeout.ms", "60000")
    prop
  }

  def main(args: Array[String]): Unit = {
    // Get the instance of the producer
    val kafkaConsumer = new KafkaConsumer[String, String](get_prop)
    // subscribe to a topic
    kafkaConsumer.subscribe(Collections.singletonList("first_topic"))
    // Start reading messages in an infinite loop
    while (true) {
      /**
       * If there is no message in Kafka, it will be read every timeout value. For example, the above code is set to 2 seconds, and it will be checked once after 2 seconds.
       * If there are still messages in Kafka that are not consumed, they will be read immediately without waiting.
       */
      val msgs: ConsumerRecords[String, String] = kafkaConsumer.poll(2000)
      val it = msgs.iterator()
      while (it.hasNext) {
        val msg = it.next()
        println(s"partition: ${msg.partition()}, offset: ${msg.offset()}, key: ${msg.key()}, value: ${msg.value()}")
      }
    }
  }
}
