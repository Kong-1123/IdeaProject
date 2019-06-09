/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.rabbitmq;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.informationplatform.amqp.connection.impl.RabbitMQConnectionImpl;
import cn.com.dhcc.informationplatform.amqp.consumer.ConsumerClient;
import cn.com.dhcc.informationplatform.amqp.consumer.handler.MQChannelDataHandler;
import cn.com.dhcc.informationplatform.amqp.consumer.impl.DefaultConsumerClient;
import cn.com.dhcc.informationplatform.amqp.prop.MQProperties;
import cn.com.dhcc.query.queryapicommon.rabbitmq.RabbitMqUtil;

/**
 * rabbitmq 初始化工具类
 * @author yuzhao.xue
 * @date 2019年1月17日
 */
@Component
public class RabbitMqInit {

	private static Logger log = LoggerFactory.getLogger(RabbitMqInit.class);
	/**
	 * 企业mq-交换机，queue name，routing key
	 */
	private static String ENT_MQ_NAME = "E_P_D_W";
	
	/**
	 * rabbitmq 配置文件名称
	 */
	private static final String AMQPFILENAME = "amqp.properties";
	@Autowired
	private MQChannelDataHandler mqChannelDataHandler;
	@PostConstruct
	public void initConSumer(){
		RabbitMqUtil.getRabbitMQFactory().topicExchangeBindingqueue(ENT_MQ_NAME, ENT_MQ_NAME, ENT_MQ_NAME);
		MQProperties prop = new MQProperties(AMQPFILENAME);
		ConnectionFactory connectionFactory = new RabbitMQConnectionImpl(prop).getConnectionFactory();//实例化连接工厂
		ConsumerClient consumer = new DefaultConsumerClient(connectionFactory,true);//消费者客户端
		try {
			consumer.onMessage(mqChannelDataHandler, ENT_MQ_NAME);
		} catch (Exception e) {
			log.error("initConSumer出现异常：{}",e);
		}
	}
	
	
}
