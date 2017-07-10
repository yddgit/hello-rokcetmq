package com.my.project;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * 生产者示例
 * @author yang
 *
 */
public class Producer {
	public static void main(String[] args) throws Exception {
		//创建一个Producer，指定组group
		DefaultMQProducer producer = new DefaultMQProducer("group");
		//设定NameServer的地址
		producer.setNamesrvAddr("192.168.19.128:9876");
		//启动Producer连接NameServer，确定要将消息发送到哪个Broker
		producer.start();
		for(int i=0; i<10; i++) {
			Message msg = new Message("orders", ("order" + i).getBytes());
			SendResult result = producer.send(msg);
			System.out.println(result);
			System.out.println(msg + " send out");
			Thread.sleep(500);
		}
		producer.shutdown();
	}
}
