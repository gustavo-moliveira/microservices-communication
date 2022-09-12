package com.mscommunication.productapi.modules.product.product.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscommunication.productapi.modules.product.product.dto.ProductStockDTO;
import com.mscommunication.productapi.modules.product.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {
    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
        productService.updateProductStock(product);
        log.info("Recebendo mensagem: {}", new ObjectMapper().writeValueAsString(product));
    }
}
