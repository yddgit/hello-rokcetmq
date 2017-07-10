package com.my.project;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Push消费者示例，订阅消息
 * @author yang
 *
 */
public class PushConsumer {

	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("pushConsumer");
		consumer.setNamesrvAddr("192.168.19.128:9876");

		try {
			//订阅PushTopic下Tag为*(all)的消息，tag通常设为orderId
			consumer.subscribe("orders", "*");
			//程序第一次启动从消息队列头取数据
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					Message msg = msgs.get(0);
					System.out.println(new String(msg.getBody()));
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
