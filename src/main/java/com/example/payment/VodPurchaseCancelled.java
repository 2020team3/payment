package com.example.payment;

public class VodPurchaseCancelled {
	String eventType;
    Long purchaseId;
    Long vodId;
    String vodName;
    
    public VodPurchaseCancelled(){
        this.eventType = VodPurchaseCancelled.class.getSimpleName();
    }
	public String getEventType() {	return eventType;	}
	public void setEventType(String eventType) {this.eventType = eventType;	}
	public Long getPurchaseId() {return purchaseId;	}
	public void setPurchaseId(Long purchaseId) {this.purchaseId = purchaseId;	}
	public Long getVodId() {return vodId;	}
	public void setVodId(Long vodId) {	this.vodId = vodId;	}
	public String getVodName() {return vodName;	}
	public void setVodName(String vodName) {this.vodName = vodName;	}
}
