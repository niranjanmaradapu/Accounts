package com.otsi.retail.hsnDetails.config;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

	@Value("${getStoreDetails_url}")
	private String storeDetails;
	
	@Value("${getUserDetails_url}")
	private String userDetails;
	
	@Value("${getCustomerDetails_url}")
	private String customerDetails;
	
	@Value("${getCustomerDetailsFromURM_url}")
	private String getCustomerDetailsFromURM;
	
	//for payment
	
	@Value("${payment_creditNotes_queue}")
	private String payemntCreditnotesQueue;
	
	@Value("${payments_exchange}")
	private String payemntsExchange;
	
	@Value("${payments_creditNotes_rk}")
	private String paymentCreditNotesRK;
	
	@Bean
	public Queue payemntCreditnotesQueue() {
		return new Queue(payemntCreditnotesQueue);
	}

	@Bean
	public DirectExchange payemntsExchange() {
		return new DirectExchange(payemntsExchange);
	}

	@Bean
	public Binding paymentCreditNotesRK(Queue payemntCreditnotesQueue, DirectExchange payemntsExchange) {

		return BindingBuilder.bind(payemntCreditnotesQueue).to(payemntsExchange).with(paymentCreditNotesRK);
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