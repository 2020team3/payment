package com.example.payment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler {

	@Autowired
    PaymentRepository paymentRepository;
	
	@StreamListener(Processor.INPUT)
	public void onEventListen(@Payload MileageStored mileageStored){
        if (mileageStored.getEventType().equals("MileageStored")) {
        	System.out.println("=========MileageStored=========");  
        	paymentRepository.findById(mileageStored.getPurchaseId())
                    .ifPresent(
                            payment -> {
                            	payment.setPurchaseId(mileageStored.getPurchaseId());
                            	payment.setMileage(mileageStored.getMileage());
                            	payment.setPayStatus("mileage changed");
                            	paymentRepository.save(payment);
                            }
                    )
            ;
        }
     
	}
	
	@StreamListener(Processor.INPUT)
    public void vodPurchased(@Payload VodPurchased vodPurchased) {
        if (vodPurchased.getEventType().equals("VodPurchased")) {
        	System.out.println("=========VodPurchased=========");  
        	paymentRepository.findById(vodPurchased.getPurchaseId())
                    .ifPresent(
                            payment -> {
                            	payment.setPayStatus("ordered");
                            	payment.setPurchaseId(vodPurchased.getPurchaseId());
                            	paymentRepository.save(payment);
                            }
                    )
            ;
        }
    }

	@StreamListener(Processor.INPUT)
    public void vodPurchaseCancelled(@Payload VodPurchaseCancelled vodPurchaseCancelled) {
        if (vodPurchaseCancelled.getEventType().equals("VodPurchaseCancelled")) {
        	System.out.println("=========VodPurchaseCancelled=========");  
        	paymentRepository.findById(vodPurchaseCancelled.getPurchaseId())
                    .ifPresent(
                            payment -> {
                            	payment.setPayStatus("cancel");
                            	payment.setPurchaseId(vodPurchaseCancelled.getPurchaseId());
                            	paymentRepository.save(payment);
                            }
                    )
            ; 
        }
    }
    
}

