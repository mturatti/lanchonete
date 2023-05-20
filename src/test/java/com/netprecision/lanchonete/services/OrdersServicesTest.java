package com.netprecision.lanchonete.services;

import com.netprecision.lanchonete.dtos.CloseOrderDTO;
import com.netprecision.lanchonete.dtos.CreateOrderDTO;
import com.netprecision.lanchonete.dtos.ItemOrderDTO;
import com.netprecision.lanchonete.dtos.ValueOfTheOrderDTO;
import com.netprecision.lanchonete.entities.ItemOrder;
import com.netprecision.lanchonete.entities.Order;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.enums.OrderStatus;
import com.netprecision.lanchonete.exceptions.AssignedValueException;
import com.netprecision.lanchonete.exceptions.NotFoundException;
import com.netprecision.lanchonete.repositories.ItemsOrdersRepository;
import com.netprecision.lanchonete.repositories.OrdersRepository;
import com.netprecision.lanchonete.repositories.ProductsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServicesTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private ItemsOrdersRepository itemsOrdersRepository;

    private OrdersServices ordersServices;

    @Before
    public void setup() {
        ordersServices = new OrdersServices(ordersRepository, productsRepository, itemsOrdersRepository);
    }

    @Test
    public void testCreateOrder() {
        // Dados de entrada
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setItems(Arrays.asList(new ItemOrderDTO(1, 2), new ItemOrderDTO(2, 3)));

        // Mock das dependências
        Order order = new Order();
        order.setId(1);

        Product product1 = new Product();
        product1.setId(1);
        when(productsRepository.findById(product1.getId())).thenReturn(java.util.Optional.of(product1));

        Product product2 = new Product();
        product2.setId(2);
        when(productsRepository.findById(product2.getId())).thenReturn(java.util.Optional.of(product2));

        ItemOrder itemOrder1 = new ItemOrder();
        itemOrder1.setAmount(2);
        itemOrder1.setProduct(product1);
        ItemOrder itemOrder2 = new ItemOrder();
        itemOrder2.setAmount(3);
        itemOrder2.setProduct(product2);
        List<ItemOrder> items = Arrays.asList(itemOrder1, itemOrder2);

        // Execução do método que será testado
        Order createdOrder = ordersServices.create(createOrderDTO);

        // Verificação do resultado
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.OPEN, createdOrder.getStatus());
        assertEquals(LocalDate.now(), createdOrder.getRequestDate());
        assertEquals(items.get(0).getProduct(), createdOrder.getItems().get(0).getProduct());
        assertEquals(items.get(1).getProduct(), createdOrder.getItems().get(1).getProduct());
    }

    @Test(expected = AssignedValueException.class)
    public void testCreateOrderWithoutItems() {
        // Dados de entrada
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setItems(Arrays.asList());

        // Execução do método que será testado
        ordersServices.create(createOrderDTO);

        // Verificação de exceção
        // Espera-se que uma AssignedValueException seja lançada
    }

    @Test
    public void testListAllOrders() {
        // Mock das dependências
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        when(ordersRepository.findAll()).thenReturn(orders);

        // Execução do método que será testado
        List<Order> result = ordersServices.listAll();

        // Verificação do resultado
        assertEquals(orders, result);
    }

    @Test
    public void testFindOneOrder() {
        // Mock das dependências
        Order order = new Order();
        when(ordersRepository.findById(1)).thenReturn(java.util.Optional.of(order));

        // Execução do método que será testado
        Order result = ordersServices.findOne(1);

        // Verificação do resultado
        assertEquals(order, result);
    }

    @Test(expected = NotFoundException.class)
    public void testFindOneOrderInvalidId() {
        // Mock das dependências
        when(ordersRepository.findById(1)).thenReturn(java.util.Optional.empty());

        // Execução do método que será testado
        ordersServices.findOne(1);

        // Verificação de exceção
        // Espera-se que uma NotFoundException seja lançada
    }

    @Test
    public void testOrderValue() {
        // Mock das dependências
        Order order = new Order();
        ItemOrder itemOrder1 = new ItemOrder();
        itemOrder1.setAmount(2);
        itemOrder1.setProduct(new Product(1, "Product 1", 10));
        ItemOrder itemOrder2 = new ItemOrder();
        itemOrder2.setAmount(3);
        itemOrder2.setProduct(new Product(2, "Product 2", 15));
        order.setItems(Arrays.asList(itemOrder1, itemOrder2));
        when(ordersRepository.findById(1)).thenReturn(java.util.Optional.of(order));

        // Execução do método que será testado
        ValueOfTheOrderDTO result = ordersServices.orderValue(1);

        // Verificação do resultado
        assertNotNull(result);
        assertEquals(65, result.getValueOfTheOrder(), 0);
    }

    @Test
    public void testCloseOrder() {
        // Dados de entrada
        CloseOrderDTO closeOrderDTO = new CloseOrderDTO();
        closeOrderDTO.setTotal(100);

        // Mock das dependências
        Order order = new Order();
        order.setId(1);
        when(ordersRepository.findById(1)).thenReturn(java.util.Optional.of(order));
        when(ordersRepository.save(order)).thenReturn(order);

        // Execução do método que será testado
        Order closedOrder = ordersServices.closeOrder(1, closeOrderDTO);

        // Verificação do resultado
        assertNotNull(closedOrder);
        assertEquals(OrderStatus.CLOSED, closedOrder.getStatus());
        assertEquals(100, closedOrder.getTotal(), 0);
        verify(ordersRepository, times(1)).save(order);
    }
}
