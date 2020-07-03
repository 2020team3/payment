package com.example.payment;

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
        if( mileageStored.getEventType().equals("MileageStored") ){     
            System.out.println("========= 마일리지 저장 =========");
            System.out.println(mileageStored.getEventType());
//            paymentRepository.findByOrderId(mileageStored.getPurchaseId())
            paymentRepository.findByPurchaseId(mileageStored.getPurchaseId())
            .ifPresent(
                    payment -> {
                    	payment.setPayStatus("Mileage");
                    	payment.setMileage(mileageStored.getMileage());
                    	paymentRepository.save(payment);
                    }
            );
            System.out.println("==================");
        }
	}
}

