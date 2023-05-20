package com.netprecision.lanchonete.services;

import com.netprecision.lanchonete.dtos.AddItemDTO;
import com.netprecision.lanchonete.dtos.RemoveItemDTO;
import com.netprecision.lanchonete.entities.ItemOrder;
import com.netprecision.lanchonete.entities.Order;
import com.netprecision.lanchonete.entities.Product;
import com.netprecision.lanchonete.enums.OrderStatus;
import com.netprecision.lanchonete.exceptions.AssignedValueException;
import com.netprecision.lanchonete.exceptions.ClosedOrderException;
import com.netprecision.lanchonete.exceptions.NotFoundException;
import com.netprecision.lanchonete.repositories.ItemsOrdersRepository;
import com.netprecision.lanchonete.repositories.OrdersRepository;
import com.netprecision.lanchonete.repositories.ProductsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemsOrdersServicesTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private ItemsOrdersRepository itemsOrdersRepository;

    private ItemsOrdersServices itemsOrdersServices;

    @Before
    public void setup() {
        itemsOrdersServices = new ItemsOrdersServices(ordersRepository, productsRepository, itemsOrdersRepository);
    }

    @Test
    public void testAddItemToOrder() {
        // Dados de entrada
        Integer orderId = 1;
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setProductId(1);
        addItemDTO.setAmount(2);

        // Mock das dependências
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));

        ItemOrder savedItemOrder = new ItemOrder();
        when(itemsOrdersRepository.save(any(ItemOrder.class))).thenReturn(savedItemOrder);

        // Execução do método que será testado
        itemsOrdersServices.addItemToOrder(orderId, addItemDTO);

        // Verificação do resultado
        verify(ordersRepository, times(1)).findById(orderId);
        verify(productsRepository, times(1)).findById(1);
        verify(itemsOrdersRepository, times(1)).save(any(ItemOrder.class));
    }

    @Test(expected = NotFoundException.class)
    public void testAddItemToOrderInvalidOrderId() {
        // Dados de entrada
        Integer orderId = 1;
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setProductId(1);
        addItemDTO.setAmount(2);

        // Mock das dependências
        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());

        // Execução do método que será testado
        itemsOrdersServices.addItemToOrder(orderId, addItemDTO);


        // Verificação de exceção
        // Espera-se que uma NotFoundException seja lançada
    }


    @Test(expected = ClosedOrderException.class)
    public void testAddItemToClosedOrder() {
        // Dados de entrada
        Integer orderId = 1;
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setProductId(1);
        addItemDTO.setAmount(2);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.CLOSED);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Execução do método que será testado
        itemsOrdersServices.addItemToOrder(orderId, addItemDTO);

        // Verificação de exceção
        // Espera-se que uma ClosedOrderException seja lançada
    }

    @Test(expected = NotFoundException.class)
    public void testAddItemToOrderInvalidProductId() {
        // Dados de entrada
        Integer orderId = 1;
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setProductId(1);
        addItemDTO.setAmount(2);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productsRepository.findById(1)).thenReturn(Optional.empty());

        // Execução do método que será testado
        itemsOrdersServices.addItemToOrder(orderId, addItemDTO);

        // Verificação de exceção
        // Espera-se que uma NotFoundException seja lançada
    }

    @Test(expected = AssignedValueException.class)
    public void testAddItemWithInvalidAmount() {
        // Dados de entrada
        Integer orderId = 1;
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setProductId(1);
        addItemDTO.setAmount(0);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));

        // Execução do método que será testado
        itemsOrdersServices.addItemToOrder(orderId, addItemDTO);

        // Verificação de exceção
        // Espera-se que uma AssignedValueException seja lançada
    }

    @Test
    public void testRemoveItemFromOrderSub() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(1);

        // Mock das dependências
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));

        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setOrder(order);
        itemOrder.setProduct(product);
        itemOrder.setAmount(2);
        when(itemsOrdersRepository.findByOrderIdAndProductId(order.getId(), product.getId())).thenReturn(itemOrder);

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação do resultado
        verify(ordersRepository, times(1)).findById(orderId);
        verify(productsRepository, times(1)).findById(1);
        verify(itemsOrdersRepository, times(1)).findByOrderIdAndProductId(order.getId(), product.getId());
        verify(itemsOrdersRepository, times(1)).save(itemOrder);
    }

    @Test
    public void testRemoveItemFromOrderDel() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(3);

        // Mock das dependências
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));

        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setOrder(order);
        itemOrder.setProduct(product);
        itemOrder.setAmount(2);
        when(itemsOrdersRepository.findByOrderIdAndProductId(order.getId(), product.getId())).thenReturn(itemOrder);

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação do resultado
        verify(ordersRepository, times(1)).findById(orderId);
        verify(productsRepository, times(1)).findById(1);
        verify(itemsOrdersRepository, times(1)).findByOrderIdAndProductId(order.getId(), product.getId());
        verify(itemsOrdersRepository, times(1)).delete(itemOrder);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveItemFromOrderInvalidOrderId() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(1);

        // Mock das dependências
        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação de exceção
        // Espera-se que uma NotFoundException seja lançada
    }

    @Test(expected = ClosedOrderException.class)
    public void testRemoveItemFromClosedOrder() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(1);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.CLOSED);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação de exceção
        // Espera-se que uma ClosedOrderException seja lançada
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveItemFromOrderInvalidProductId() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(1);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productsRepository.findById(1)).thenReturn(Optional.empty());

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação de exceção
        // Espera-se que uma NotFoundException seja lançada
    }

    @Test(expected = AssignedValueException.class)
    public void testRemoveItemWithInvalidAmount() {
        // Dados de entrada
        Integer orderId = 1;
        RemoveItemDTO removeItemDTO = new RemoveItemDTO();
        removeItemDTO.setProductId(1);
        removeItemDTO.setAmount(0);

        // Mock das dependências
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1);
        when(productsRepository.findById(1)).thenReturn(Optional.of(product));

        // Execução do método que será testado
        itemsOrdersServices.removeItemToOrder(orderId, removeItemDTO);

        // Verificação de exceção
        // Espera-se que uma AssignedValueException seja lançada
    }
}