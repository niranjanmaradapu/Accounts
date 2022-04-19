package com.otsi.retail.paymentgateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
	@Value("${RazoryPay.key_Id}")
	private String Key;
	
	@Value("${RazorPay.key_Secret}")
	private String secert;
	
	@Value("${newsale_queue}")
	private String newSaleQueue;
	
	@Value("${newsale_exchange}")
	private String newSaleExchange;
	
	@Value("${payment_newsale_rk}")
	private String paymentNewsaleRK;
	
	@Value("${razorpay.callback-url:}")
	private String razorpayCallBackUrl;
	
	@Bean
	public Queue queue() {
		return new Queue(newSaleQueue);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(newSaleExchange);
	}

	@Bean
	public Binding binding(Queue queue, DirectExchange directExchange) {

		return BindingBuilder.bind(queue).to(directExchange).with(paymentNewsaleRK);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory factory) {

		RabbitTemplate template = new RabbitTemplate(factory);
		template.setMessageConverter(messageConverter());
		return template;

	}
}
