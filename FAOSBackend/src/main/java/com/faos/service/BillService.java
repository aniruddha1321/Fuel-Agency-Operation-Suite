package com.faos.service;

import com.faos.exception.CustomExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.model.Bill;
import com.faos.repository.BillRepository;

import java.util.Optional;


@Service
public class BillService {
	  @Autowired
	    private BillRepository billRepository;

	   
	    	 public Bill createBill(String deliveryOption, boolean surchargeApplicable, long count) {
	    	        Bill bill = new Bill();

	    	        long price = 1000;
					if(count>6)
					{ price=1000+1000*20/100;
					}// Base price of the cylinder
	    	        long gst = 10;      // Fixed GST value
	    	        long deliveryCharge;
	    	        long totalPrice;

	    	        // ✅ Determine delivery charge based on option
	    	        if ("Normal".equalsIgnoreCase(deliveryOption)) {
	    	            deliveryCharge = 50;
	    	        } else if ("Express".equalsIgnoreCase(deliveryOption)) {
	    	            deliveryCharge = 100;
	    	        } else {
	    	            throw new IllegalArgumentException("Invalid delivery option: " + deliveryOption);
	    	        }

	    	        // ✅ Apply surcharge if booking count > 6
	    	        long surcharge = surchargeApplicable ? (long) (0.2 * price) : 0;
	    	        
	    	        // ✅ Calculate total price with surcharge
	    	        totalPrice = price + gst + deliveryCharge + surcharge;

	    	        // ✅ Set values in the Bill entity
	    	        bill.setPrice(price);
	    	        bill.setGst(gst);
	    	        bill.setDeliveryCharge(deliveryCharge);
	    	        bill.setSurcharge(surcharge);
	    	        bill.setTotalPrice(totalPrice);

	    	        return billRepository.save(bill);
	    	    
	    }

	public Optional<Bill> findByBookingId(Long billId) {
				 return billRepository.findById(billId);
	}

	public Bill findByBookingIds(Long billId) throws CustomExceptions {
		return billRepository.findById(billId)
				.orElseThrow(() -> new CustomExceptions("Bill not found with ID: " + billId));
	}

}
