package org.hni.admin.service;

import java.time.LocalDate;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.hni.common.DateUtils;
import org.hni.order.om.Order;
import org.hni.order.service.OrderService;
import org.hni.provider.om.Provider;
import org.hni.user.om.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/orders", description = "Operations on Orders and OrderItems")
@Component
@Path("/orders")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Inject private OrderService orderService;
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Returns the order with the given id"
		, notes = ""
		, response = Order.class
		, responseContainer = "")
	public Order getOrder(@PathParam("id") Long id) {
		return orderService.get(id);
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Creates a new order or saves the order with the given id"
		, notes = "An order without an ID field will be created"
		, response = Order.class
		, responseContainer = "")
	public Order saveOrder(Order order) {
		return orderService.save(order);
	}

	@DELETE
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Deletes the order with the given id"
		, notes = ""
		, response = Order.class
		, responseContainer = "")
	public Order getDelete(@PathParam("id") Long id) {
		return orderService.delete(new Order(id));
	}

	@GET
	@Path("/next")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Returns the next order obtaining a lock on it so others cannot work on it at the same time.  You may pass an optional parameter 'providerId' to filter to a specific provider"
		, notes = ""
		, response = Order.class
		, responseContainer = "")
	public Order getNextOrder(@QueryParam("providerId") Long providerId) {
		if ( null != providerId ) {
			return orderService.next(new Provider(providerId));
		}
		return orderService.next();
	}

	@PUT
	@Path("/completed/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Returns the next order obtaining a lock on it so others cannot work on it at the same time.  You may pass an optional parameter 'providerId' to filter to a specific provider"
		, notes = ""
		, response = Order.class
		, responseContainer = "")
	public void completeOrder(@PathParam("id") Long id, @QueryParam("pickupDate") String pickupDateString) {
		// also need payment info
		orderService.complete(orderService.get(id), DateUtils.parseDateTime(pickupDateString));
	}

	private static final String ORDER_COUNT = "{\"order-count\":\"%d\"}";
	@GET
	@Path("/count")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Returns the # pending orders. You may pass an optional parameter 'providerId' to filter to a specific provider"
		, notes = ""
		, response = Order.class
		, responseContainer = "")
	public String getOrderCount(@QueryParam("providerId") Long providerId) {
		if ( null != providerId ) {
			return String.format(ORDER_COUNT, orderService.countOrders(new Provider(providerId)));
		}
		return String.format(ORDER_COUNT, orderService.countOrders());
	}
	
	
	@GET
	@Path("/users/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Returns a collection of orders for the given user between the given dates.  If the endDate is not supplied it will default to current date"
	, notes = "accepted date formats yyyy-mm-dd, yyyy/mm/dd, mm-dd-yyyy, mm/dd/yyyy"
	, response = Order.class
	, responseContainer = "")
	public Collection<Order> getUserOrdersBetweenDates(@PathParam("id") Long id, @QueryParam("startDate") String startDate , @QueryParam("endDate") String endDate) {
		
		LocalDate start = DateUtils.parseDate(startDate);
		LocalDate end = LocalDate.now();
		if ( !StringUtils.isEmpty(endDate) ) {
			end = DateUtils.parseDate(endDate);
		}
		return orderService.get(new User(id), start, end);
	}


}
