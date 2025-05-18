package com.event.payment_service.service;

import java.util.UUID;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.event.payment_service.dto.OrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class RazorpayService {

    @Value("${razorpay.api.key}")
    private String keyId;
    
    @Value("${razorpay.api.secret}")
    private String keySecret;
    
    @Value("${razorpay.currency}")
    private String currency;

    public OrderResponse createOrder(Double amount) throws RazorpayException{
        try {

            //create a clien
            RazorpayClient razorpayClient = new RazorpayClient(keyId,keySecret);

            //create an order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount*100);  //amount in paise
            orderRequest.put("currency", currency);
            String receiptId = "receipt_" + UUID.randomUUID().toString().replace("-", "").substring(0, 30);
            orderRequest.put("receipt", receiptId);
            orderRequest.put("payment_capture", 1); //auto capture

            Order order = razorpayClient.orders.create(orderRequest);

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(order.get("id"));
            orderResponse.setCurrency(order.get("currency"));
            Number amountInPaise = (Number) order.get("amount");
            orderResponse.setAmount(amountInPaise.doubleValue() / 100.0);
            orderResponse.setKeyId(keyId);

            return orderResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean verifyPaymentSignature(String orderId,String paymentId,String signature){
        try {

            String data = orderId + "|" + paymentId;
            return Utils.verifySignature(data, signature, keySecret);
            
        } catch (Exception e) {
            return false;
        }
    }
    
}
