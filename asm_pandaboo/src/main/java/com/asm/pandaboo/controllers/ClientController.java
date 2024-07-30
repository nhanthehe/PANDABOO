package com.asm.pandaboo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asm.pandaboo.entities.AccountEntity;
import com.asm.pandaboo.entities.CategoryEntity;
import com.asm.pandaboo.entities.ClientEntity;
import com.asm.pandaboo.entities.ImageEntity;
import com.asm.pandaboo.entities.PayDetailEntity;
import com.asm.pandaboo.entities.PaymentDetailEntity;
import com.asm.pandaboo.entities.PaymentEntity;
import com.asm.pandaboo.entities.ProductEntity;
import com.asm.pandaboo.entities.PromotionEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;
import com.asm.pandaboo.entities.UnitEntity;
import com.asm.pandaboo.interfaces.PayService;
import com.asm.pandaboo.jpa.AccountsJPA;
import com.asm.pandaboo.jpa.CategoryJPA;
import com.asm.pandaboo.jpa.ClientJPA;
import com.asm.pandaboo.jpa.ImageJPA;
import com.asm.pandaboo.jpa.PayDetailJPA;
import com.asm.pandaboo.jpa.PaymentDetailJPA;
import com.asm.pandaboo.jpa.PaymentJPA;
import com.asm.pandaboo.jpa.ProductJPA;
import com.asm.pandaboo.jpa.PromotionJPA;
import com.asm.pandaboo.jpa.ShoppingCartJPA;
import com.asm.pandaboo.jpa.UnitJPA;
import com.asm.pandaboo.models.ClientBean;
import com.asm.pandaboo.models.PaymentBean;
import com.asm.pandaboo.models.ProductBean;
import com.asm.pandaboo.services.UploadFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ClientController {

	@Autowired
	AccountsJPA accountJPA;

	@Autowired
	CategoryJPA categoryJPA;

	@Autowired
	UnitJPA unitJPA;

	@Autowired
	ProductJPA productJPA;

	@Autowired
	ImageJPA imageJPA;

	@Autowired
	PaymentJPA paymentJPA;

	@Autowired
	PayDetailJPA payDetailJPA;

	@Autowired
	PaymentDetailJPA paymentDetailJPA;

	@Autowired
	ClientJPA clientJPA;

	@Autowired
	ShoppingCartJPA shoppingCartJPA;

	@Autowired
	PromotionJPA promotionJPA;
	@Autowired
	PayService payService;

	@Autowired
	UploadFile uploadFile;

	@Autowired
	HttpServletRequest request;

	@GetMapping("/pandaBooIndex")
	public String index(Model model) {
		model.addAttribute("path", request.getServletPath());
		return "client/index";
	}

	@GetMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("cart", payService);
		return "client/cart";
	}

//	@GetMapping("/add-to-cart")
//	public String updateCart(@RequestParam("id") int id, @RequestParam("clientId") int cliId,
//			@RequestParam("path") String path, @RequestParam(name = "quantity", defaultValue = "1") int quantity,
//			Model model) {
//		payService.add(id, cliId);
//		return String.format("redirect:%s", path);
//	}
	
	@GetMapping("/add-to-cart")
	public String updateCart(@RequestParam("id") int id,
	                         @RequestParam("clientId") int cliId,
	                         @RequestParam("path") String path,
	                         @RequestParam(name = "quantity", defaultValue = "1") int quantity,
	                         @RequestParam(name = "page", required = false) Integer page,
	                         @RequestParam(name = "size", required = false) Integer size,
	                         Model model) {
	    payService.add(id, cliId);
	    
	    if (page != null && size != null) {
	        return String.format("redirect:/category?page=%d&size=%d", page, size);
	    } else {
	    	return String.format("redirect:%s", path);
	    }
	}


	@GetMapping("/add-to-cart-with-quantity")
	public String updateCart(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
			@RequestParam("clientId") int cliId, @RequestParam("path") String path, Model model) {
		payService.add(id, quantity, cliId);
		return String.format("redirect:%s", path);
	}

	@GetMapping("/upQuantity")
	public String upQuantity(@RequestParam("quantity") int quantity, @RequestParam("path") String path) {
		return String.format("redirect:%s&quantity=%d", path, quantity + 1);
	}

	@GetMapping("/downQuantity")
	public String downQuantity(@RequestParam("quantity") int quantity, @RequestParam("path") String path) {
		return String.format("redirect:%s&quantity=%d", path, quantity > 1 ? quantity - 1 : 1);
	}

	@GetMapping("/update-shopping-cart")
	public String updateCart(@RequestParam("id") int id, @RequestParam("clientId") int cliId,
			@RequestParam("quantity") int quantity, Model model) {
		if (quantity <= 0) {
			payService.remove(id, cliId);
		} else {
			payService.update(id, quantity, cliId);
		}
		return "redirect:/cart";
	}

	@GetMapping("/remove-cart")
	public String removeCart(@RequestParam("id") int id, @RequestParam("clientId") int cliId) {
		payService.remove(id, cliId);
		System.out.println("delete");
		return "redirect:/cart";
	}
	
	@GetMapping("/clear-cart")
	public String clearCart(@RequestParam("clientId") int cliId) {
		List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByCliId(cliId);
		ShoppingCartEntity shoppingCartEntity = shoppingCartJPA.findShoppingCartByCliID(cliId);
		if(shoppingCartEntity != null) {
			List<PayDetailEntity> payDetailEntity = payDetailJPA.getPayDetailByCartID(shoppingCartEntity.getCart_id());
			if(payDetailEntity != null) {
				for (PayDetailEntity payDetailEntity2 : payDetailEntities) {
					payDetailJPA.delete(payDetailEntity2);
				}
			}								
		}
		return "redirect:/cart";
	}

	@GetMapping("/singleProduct")
	public String singleProduct(@RequestParam("id") String id, Model model,
			@RequestParam("quantity") Optional<Integer> quantity) {
		Optional<ProductEntity> prodOptional = productJPA.findById(id);
		if (prodOptional.isPresent()) {
			ProductEntity product = prodOptional.get();
			model.addAttribute("product", product);
			
			int sold = 0;
			if(productJPA.getSoldById(id)!=null) {
				sold = productJPA.getSoldById(id);
			}else {
				sold = 0;
			}
			model.addAttribute("sold", sold);
			
			int quantityValue = quantity.orElse(1);
			model.addAttribute("quantity", quantityValue);
			model.addAttribute("path", "/singleProduct?id=" + id);
		}
		return "client/single-product";
	}

	@GetMapping("/category")
	public String category(Model model,
	                       @RequestParam(name = "page", defaultValue = "1") int page,
	                       @RequestParam(name = "size", defaultValue = "3") int size,
	                       @RequestParam(name = "catId", required = false) String idOptional) {
	    if (page < 1) {
	        page = 1;
	    }
	    Pageable pageable = PageRequest.of(page - 1, size);

	    Page<ProductEntity> productPage;
	    if (idOptional != null && !idOptional.isEmpty()) {
	        try {
	            productPage = productJPA.getProductByCatID(idOptional, pageable);
	            model.addAttribute("cat_id", idOptional);
	        } catch (Exception e) {
	            productPage = Page.empty();
	        }
	    } else {
	        productPage = productJPA.findAll(pageable);
	    }

	    int totalPages = productPage.getTotalPages();
	    if (totalPages > 0) {
	        List<Integer> pageNumbers = new ArrayList<>();
	        for (int i = 1; i <= totalPages; i++) {
	            pageNumbers.add(i);
	        }
	        model.addAttribute("pageNumbers", pageNumbers);
	    }

	    model.addAttribute("productPage", productPage);
	    model.addAttribute("selectedSize", size);

	    return "client/category";
	}

	
	@GetMapping("/myPayments")
	public String myPayments(@RequestParam("cliId") int cliId,Model model) {
		List<PaymentEntity> paymentsEntity = paymentJPA.getListPaymentByClientID(cliId);
		model.addAttribute("payments", paymentsEntity);
		return "client/myPayments";
	}

	@GetMapping("/checkout")
	public String checkout(@RequestParam("clientId") int clientId, Model model) {
		Optional<ClientEntity> clientOptional = clientJPA.findById(clientId);
		if (clientOptional.isPresent()) {
			ClientEntity clientEntity = clientOptional.get();
			List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByCliId(clientId);
			
			Date currentDate = new Date();
			List<PromotionEntity> promotions = promotionJPA.findAllActivePromotions(currentDate);
			//Optional<PromotionEntity> promOptional = promotionJPA.findById(prom_id);
			double amount = payService.getAmount(clientId);	
			double shippingFee = (amount >= 500000) ? 0 : 30000;
//			double voucher = 0;
//			PromotionEntity promotionEntity = promOptional.get();
//		        if(amount >= promotionEntity.getStart_price()) {
//		        	voucher = (amount)*promotionEntity.getDiscount()/100;
//		        	System.out.println("1");
//		        }else {
//		        	voucher = 0;
//		        	System.out.println("2");
//		        }
//		    double totalPayment = amount+shippingFee-voucher;
			model.addAttribute("client", clientEntity);
			model.addAttribute("cartItems", payDetailEntities);
			model.addAttribute("shippingFee", shippingFee);
			//model.addAttribute("voucher", voucher);
			model.addAttribute("totalAmount", amount);
			//model.addAttribute("totalPayment", totalPayment);
			//model.addAttribute("proId", promOptional);
			model.addAttribute("promotions", promotions);
		}
		return "client/checkout";
	}

	
	@PostMapping("/checkout")
	public String payCart(@Valid PaymentBean pay, BindingResult error,Model model,
			RedirectAttributes redirect, @RequestParam("clientId") int clientId,
			@RequestParam("prom_id") int prom_id) {
		
		if (error.hasErrors()) {
			model.addAttribute("error", error);
		}
		Optional<ClientEntity> clientOptional = clientJPA.findById(clientId);
		if (clientOptional.isPresent()) {
			ClientEntity clientEntity = clientOptional.get();			
			List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByCliId(clientId);
			PaymentEntity paymentEntity = new PaymentEntity();
			Optional<PromotionEntity> promOptional = promotionJPA.findById(prom_id);
			paymentEntity.setPayDate(new Date());
			int totalQuantity = 0;
			int amount = 0;
			for (PayDetailEntity payDetail : payDetailEntities) {
				totalQuantity += payDetail.getQuantity();
				
				paymentEntity.setTotalQuantity(totalQuantity);
				
				paymentEntity.setPayMethod("Thanh toán khi nhận hàng.");
				paymentEntity.setStatus(1);
				paymentEntity.setClientPaymentsEntity(clientEntity);
				if(promOptional.isPresent()) {
					PromotionEntity promotionEntity = promOptional.get();
					
					amount += payDetail.getPaydetailProductEntity().getRed_price() * payDetail.getQuantity();
					int shippingFee = (amount >= 500000) ? 0 : 30000;
			  
			        int voucher = 0;
			        if(amount >= promotionEntity.getStart_price()) {
			        	voucher = (int)((amount)*promotionEntity.getDiscount()/100);
			        	System.out.println("1");
			        }else {
			        	voucher = 0;
			        	System.out.println("2");
			        }
			        double totalPayment = amount+shippingFee-voucher;
			        paymentEntity.setOrderTotal(totalPayment);			        
					paymentEntity.setPromotionEnity(promotionEntity);
					
				}else {
					amount += payDetail.getPaydetailProductEntity().getRed_price() * payDetail.getQuantity();
					int shippingFee = (amount >= 500000) ? 0 : 30000;
					paymentEntity.setPromotionEnity(null);
					double totalPayment = amount+shippingFee;
				    paymentEntity.setOrderTotal(totalPayment);			        
				}
						
				paymentJPA.save(paymentEntity);
				
				model.addAttribute("payment", paymentEntity);
				
		        
				PaymentDetailEntity paymentDetailEntity = new PaymentDetailEntity();
				paymentDetailEntity.setPaymentPaymentDetailEntity(paymentEntity);
				paymentDetailEntity.setProd_id(payDetail.getPaydetailProductEntity().getProd_id());
				paymentDetailEntity.setProd_name(payDetail.getPaydetailProductEntity().getProd_name());
				paymentDetailEntity.setRed_price(payDetail.getPaydetailProductEntity().getRed_price());
				paymentDetailEntity.setProd_quantity(payDetail.getQuantity());
				paymentDetailEntity.setProd_images(payDetail.getPaydetailProductEntity().getImages().get(0).getName());
				paymentDetailJPA.save(paymentDetailEntity);
				
				ShoppingCartEntity shoppingCartEntity = shoppingCartJPA.findShoppingCartByCliID(clientId);
				if(shoppingCartEntity != null) {
					List<PayDetailEntity> payDetailEntity = payDetailJPA.getPayDetailByCartID(shoppingCartEntity.getCart_id());
					if(payDetailEntity != null) {
						for (PayDetailEntity payDetailEntity2 : payDetailEntities) {
							payDetailJPA.delete(payDetailEntity2);
						}
					}								
				}
			}		

			model.addAttribute("client", clientEntity);
			model.addAttribute("cartItems", payDetailEntities);
			model.addAttribute("totalAmount", payService.getAmount(clientId));
			return "redirect:/confirmation?clientId="+clientEntity.getCli_id()+"&payId="+paymentEntity.getPayId();
		}
		redirect.addFlashAttribute("pay", pay);
		return "redirect:/checkout";
	}
	
	@GetMapping("/update-status-MyPayment")
	public String updateStstusPayment(@RequestParam("payId")int payId, @RequestParam("status")int status, Model model) {
		Optional<PaymentEntity> paymentoptional= paymentJPA.findById(String.valueOf(payId));
		int cliId = 0;
		if (paymentoptional.isPresent()) {
			PaymentEntity paymanetEntity = paymentoptional.get();
			cliId = paymanetEntity.getClientPaymentsEntity().getCli_id();
			if(paymanetEntity.getStatus()==2) {
				model.addAttribute("NoUpdateStatus", "Không thể hủy đơn hàng do cửa hàng đã nhận đơn!");
				System.out.println("2");
				
//				return String.format("redirect:/myPayments?cliId=%d",cliId);
			}else {
				paymanetEntity.setStatus(status);
				paymentJPA.save(paymanetEntity);
				System.out.println("1");
//				return String.format("redirect:/myPayments?cliId=%d",cliId);
			}
			
		}
		return String.format("redirect:/myPayments?cliId=%d",cliId);
	}
	
	@GetMapping("/confirmation")
	public String confirmation(@RequestParam("payId") int payId, Model model) {

		List<PaymentDetailEntity> paymentDetail = paymentDetailJPA.getListPaymentDetailByPayId(payId);
		model.addAttribute("payment", paymentDetail);
		int amount = 0;
		for (PaymentDetailEntity payDetail : paymentDetail) {
			
			amount += payDetail.getRed_price() * payDetail.getProd_quantity();
			int shippingFee = (amount >= 500000) ? 0 : 30000;
			//int totalPayment = amount + shippingFee;
			model.addAttribute("shippingFee", shippingFee);			
	        model.addAttribute("totalPayment", amount);
		}	
		
		Optional<PaymentEntity> paymentEntityOptional = paymentJPA.findById(String.valueOf(payId));
	    if (paymentEntityOptional.isPresent()) {
	        model.addAttribute("paymentEntity", paymentEntityOptional.get());
	    }
		return "client/confirmation";
	}

	@GetMapping("/profile")
	public String profileClient(@RequestParam("accId") int accId, Model model) {
		ClientEntity entity = clientJPA.getClientByAccID(accId);
		if (entity != null) {
			model.addAttribute("client", entity);
		}
		return "client/profile";
	}

	@PostMapping("/profile")
	public String updateProfileClient(@Valid ClientBean clientBean, BindingResult error,
			@RequestParam("accId") int accId, @RequestParam("avatar") MultipartFile file, Model model) {
		Optional<AccountEntity> accOptional = accountJPA.findById(String.valueOf(accId));
		if (error.hasErrors()) {
			model.addAttribute("error", error);
		}
		if (accOptional.isPresent()) {
			String fileName = uploadFile.Upload(file);
			AccountEntity accEntity = accOptional.get();
			if (fileName != null) {
				accEntity.setAvatar(fileName);
			} else {
				accEntity.setAvatar(accEntity.getAvatar());
			}
			accEntity.setFullname(clientBean.getFullname());
			accountJPA.save(accEntity);
			System.out.println(accEntity.getFullname() + " + " + fileName);
			ClientEntity cliEntity = clientJPA.getClientByAccID(accId);
			cliEntity.setPhone(clientBean.getPhone());
			cliEntity.setRoad(clientBean.getRoad());
			cliEntity.setWard(clientBean.getWard());
			cliEntity.setDistrict(clientBean.getDistrict());
			cliEntity.setCity(clientBean.getCity());
			cliEntity.setEmail(clientBean.getEmail());
			System.out.println(clientBean.getPhone() + " + " + clientBean.getRoad() + " + " + clientBean.getWard()
					+ " + " + clientBean.getDistrict() + " + " + clientBean.getCity() + " + " + clientBean.getCity());
			clientJPA.save(cliEntity);

		}
		return String.format("redirect:/profile?accId=%d", accId);
	}

	@ModelAttribute("products")
	public List<ProductEntity> Product() {
		return productJPA.findAll();
	}

	@ModelAttribute("categories")
	public List<CategoryEntity> Category() {
		return categoryJPA.findAll();
	}

	@ModelAttribute("units")
	public List<UnitEntity> Unit() {
		return unitJPA.findAll();
	}

	@ModelAttribute("images")
	public List<ImageEntity> getImages() {
		return imageJPA.findAll();
	}

	@ModelAttribute("accounts")
	public List<AccountEntity> getAccounts() {
		return accountJPA.findAll();
	}

}
