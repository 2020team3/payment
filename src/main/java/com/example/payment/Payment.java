package com.example.payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "Payment_table")
public class Payment {

	@Id
    @GeneratedValue
    Long id;
    Long purchaseId;
    int mileage;
    String payStatus;
       
    @PostPersist
    public void success(){
    	
        long time = (long) (600+Math.random()*300);
        try {
        	System.out.println("[ Random Time ] : " + time);
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrors");
			e1.printStackTrace();
		}
        
    	if (payStatus != null && payStatus.contentEquals("ordered")) {
    	PaymentApproved paymentApproved = new PaymentApproved();
    	paymentApproved.setPurchaseId(this.getPurchaseId());
    	paymentApproved.setMileage(this.getMileage());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(paymentApproved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor processor = PaymentApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build()); 
    	}
    }
    
        @PostUpdate
	public void cancel() {
		if (payStatus != null && payStatus.contentEquals("cancel")) {

			PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);

			paymentRepository.findById(this.getPurchaseId()).ifPresent(payment -> {

				PaymentCancelled paymentCancelled = new PaymentCancelled();
				paymentCancelled.setPurchaseId(payment.getPurchaseId());
				paymentCancelled.setMileage(this.getMileage());

				ObjectMapper objectMapper = new ObjectMapper();
				String json = null;

				try {
					json = objectMapper.writeValueAsString(paymentCancelled);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("JSON format exception", e);
				}

				Processor processor = PaymentApplication.applicationContext.getBean(Processor.class);
				MessageChannel outputChannel = processor.output();

				outputChannel.send(MessageBuilder.withPayload(json)
						.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

			});
		}

	}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

}
