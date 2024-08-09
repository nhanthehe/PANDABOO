package com.asm.pandaboo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.asm.pandaboo.jpa.*;
import com.asm.pandaboo.models.AccountBean;
import com.asm.pandaboo.models.AddressBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asm.pandaboo.entities.AccountEntity;
import com.asm.pandaboo.entities.CategoryEntity;
import com.asm.pandaboo.entities.AddressEntity;
import com.asm.pandaboo.entities.ImageEntity;
import com.asm.pandaboo.entities.PayDetailEntity;
import com.asm.pandaboo.entities.PaymentDetailEntity;
import com.asm.pandaboo.entities.PaymentEntity;
import com.asm.pandaboo.entities.ProductEntity;
import com.asm.pandaboo.entities.PromotionEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;
import com.asm.pandaboo.entities.UnitEntity;
import com.asm.pandaboo.interfaces.PayService;
import com.asm.pandaboo.models.PaymentBean;
import com.asm.pandaboo.services.UploadFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
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
	AddressJPA addressJPA;

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
	public List<ProductEntity> index(Model model) {
		model.addAttribute("path", request.getServletPath());
		return productJPA.getProducts();
	}

	@GetMapping("/cart/{accId}")
	public List<PayDetailEntity> cart(Model model, @PathVariable int accId) {
		List<PayDetailEntity> cartList = payService.getCartList(accId);
		model.addAttribute("cart", cartList);
		return cartList;
	}

//	@GetMapping("/add-to-cart")
//	public String updateCart(@RequestParam("id") int id, @RequestParam("clientId") int cliId,
//			@RequestParam("path") String path, @RequestParam(name = "quantity", defaultValue = "1") int quantity,
//			Model model) {
//		payService.add(id, cliId);
//		return String.format("redirect:%s", path);
//	}

	@PostMapping("/add-to-cart/{accId}/{id}")
	public List<PayDetailEntity> updateCart(
			@PathVariable int id,
			@PathVariable int accId,
			@RequestParam(name = "quantity", defaultValue = "1") int quantity,
			@RequestParam(name = "page", required = false) Integer page,
			// @RequestParam("path") String path,
            @RequestParam(name = "size", required = false) Integer size,
            Model model
	) {
	    payService.add(id, accId);
		return payDetailJPA.getPayDetailByAccIdAndProdId(String.valueOf(accId),String.valueOf(id));
//	    if (page != null && size != null) {
//	        return String.format("redirect:/category?page=%d&size=%d", page, size);
//	    } else {
//	    	return String.format("redirect:%s", path);
//	    }
	}


	@PostMapping("/add-to-cart-with-quantity/{id}/{accId}/{quantity}")
	public List<PayDetailEntity> updateCart(
			@PathVariable int id,
			@PathVariable int quantity,
			@PathVariable int accId
			//,@RequestParam("path") String path,
			// Model model
	) {
		payService.add(id, quantity, accId);
		return payDetailJPA.getPayDetailByAccIdAndProdId(String.valueOf(accId),String.valueOf(id));
		//return String.format("redirect:%s", path);
	}

	@GetMapping("/upQuantity")
	public String upQuantity(@RequestParam("quantity") int quantity, @RequestParam("path") String path) {
		return String.format("redirect:%s&quantity=%d", path, quantity + 1);
	}

	@GetMapping("/downQuantity")
	public String downQuantity(@RequestParam("quantity") int quantity, @RequestParam("path") String path) {
		return String.format("redirect:%s&quantity=%d", path, quantity > 1 ? quantity - 1 : 1);
	}

	@PutMapping("/update-shopping-cart/{id}/{accId}/{quantity}")
	public List<PayDetailEntity> updateCart(@PathVariable int id, @PathVariable int accId,
											@PathVariable int quantity
			, Model model
	) {
		if (quantity <= 0) {
			payService.remove(id, accId);
		} else {
			payService.update(id, quantity, accId);
		}
		return payDetailJPA.getPayDetailByAccIdAndProdId(String.valueOf(accId),String.valueOf(id));

	}

	@DeleteMapping("/remove-cart/{id}/{accId}")
	public void removeCart(@PathVariable int id, @PathVariable int accId) {
		payService.remove(id, accId);
		System.out.println("delete");
		//return "redirect:/cart";
	}

	@DeleteMapping ("/clear-cart/{accId}")
	public List<PayDetailEntity> clearCart(@PathVariable String accId) {
		List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByAccId(accId);
		ShoppingCartEntity shoppingCartEntity = shoppingCartJPA.findShoppingCartByCliID(accId);
		if(shoppingCartEntity != null) {
			List<PayDetailEntity> payDetailEntity = payDetailJPA.getPayDetailByCartID(shoppingCartEntity.getCart_id());
			if(payDetailEntity != null) {
				for (PayDetailEntity payDetailEntity2 : payDetailEntities) {
					payDetailJPA.delete(payDetailEntity2);
				}
			}
		}
		return payService.getCartList(Integer.parseInt(accId));
	}

	@GetMapping("/singleProduct/{id}")
	public ProductEntity singleProduct(@PathVariable String id, Model model,
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
		return productJPA.getProductByID(id);
	}

	@GetMapping("/category")
	public List<ProductEntity> category(
			Model model,
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
	        productPage = productJPA.getProducts(pageable);
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

	    return productJPA.getProducts();
	}


	@GetMapping("/myPayments/{accId}")
	public List<PaymentEntity> myPayments(@PathVariable int accId,Model model) {
		List<PaymentEntity> paymentsEntity = paymentJPA.getListPaymentByClientID(accId);
		model.addAttribute("payments", paymentsEntity);
		return paymentsEntity;
	}

	@GetMapping("/checkout/{accId}")
	public List<PayDetailEntity> checkout(@PathVariable String accId, Model model) {
		Optional<AccountEntity> accountOptional = accountJPA.findById(accId);
		if (accountOptional.isPresent()) {
			AccountEntity accountEntity = accountOptional.get();
			List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByAccId(accId);

			Date currentDate = new Date();
			List<PromotionEntity> promotions = promotionJPA.findAllActivePromotions(currentDate);
			//Optional<PromotionEntity> promOptional = promotionJPA.findById(prom_id);
			double amount = payService.getAmount(Integer.parseInt(accId));
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
			model.addAttribute("client", accountEntity);
			model.addAttribute("cartItems", payDetailEntities);
			model.addAttribute("shippingFee", shippingFee);
			//model.addAttribute("voucher", voucher);
			model.addAttribute("totalAmount", amount);
			//model.addAttribute("totalPayment", totalPayment);
			//model.addAttribute("proId", promOptional);
			model.addAttribute("promotions", promotions);
			return payDetailJPA.getFindByAccId(accId);
		}
		return payDetailJPA.getFindByAccId(accId);
	}


	@PostMapping("/checkout/{accountId}/{prom_id}")
	public List<PaymentDetailEntity> payCart(
			//@Valid PaymentBean pay, BindingResult error,
			Model model,
			RedirectAttributes redirect,
			@PathVariable String accountId,
			@PathVariable int prom_id) {

//		if (error.hasErrors()) {
//			model.addAttribute("error", error);
//		}
		Optional<AccountEntity> accountOptional = accountJPA.findById(accountId);
		if (accountOptional.isPresent()) {
			AccountEntity accountEntity = accountOptional.get();
			List<PayDetailEntity> payDetailEntities = payDetailJPA.getFindByAccId(accountId);

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
				paymentEntity.setAccountPaymentsEntity(accountEntity);

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

				ShoppingCartEntity shoppingCartEntity = shoppingCartJPA.findShoppingCartByCliID(accountId);
				if(shoppingCartEntity != null) {
					List<PayDetailEntity> payDetailEntity = payDetailJPA.getPayDetailByCartID(shoppingCartEntity.getCart_id());
					if(payDetailEntity != null) {
						for (PayDetailEntity payDetailEntity2 : payDetailEntities) {
							payDetailJPA.delete(payDetailEntity2);
						}
					}
				}
			}

			model.addAttribute("client", accountEntity);
			model.addAttribute("cartItems", payDetailEntities);
			model.addAttribute("totalAmount", payService.getAmount(Integer.parseInt(accountId)));
			return paymentDetailJPA.getListPaymentDetailByAccId(Integer.parseInt(accountId));
		}
//		redirect.addFlashAttribute("pay", pay);
		//return error;
		return paymentDetailJPA.getListPaymentDetailByAccId(Integer.parseInt(accountId));
	}

	@PutMapping("/update-status-MyPayment/{payId}/{status}")
	public PaymentEntity updateStatusPayment(@PathVariable int payId, @PathVariable int status, Model model) {
		Optional<PaymentEntity> paymentoptional= paymentJPA.findById(String.valueOf(payId));
		int cliId = 0;
		if (paymentoptional.isPresent()) {
			PaymentEntity paymanetEntity = paymentoptional.get();
			cliId = paymanetEntity.getAccountPaymentsEntity().getAcc_id();
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
		return paymentJPA.findById(String.valueOf(payId)).get();
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
		AccountEntity entity = accountJPA.getAccountsByAccId(accId);
		if (entity != null) {
			model.addAttribute("client", entity);
		}
		return "client/profile";
	}

	@PostMapping("/profile")
	public String updateProfileClient(
			@Valid AccountBean accountBean, BindingResult error, AddressBean addressBean,
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
			accEntity.setFullname(accountBean.getFullname());
			accEntity.setPhone(accountBean.getPhone());
			accEntity.setEmail(accountBean.getEmail());
			accountJPA.save(accEntity);
			System.out.println(accEntity.getFullname() + " + " + fileName);
			AddressEntity addressEntity = addressJPA.getAccountByAccID(accId);
			if(addressEntity != null){
				addressEntity.setRoad(addressBean.getRoad());
				addressEntity.setWard(addressBean.getWard());
				addressEntity.setDistrict(addressBean.getDistrict());
				addressEntity.setCity(addressBean.getCity());
				addressEntity.setStatus(true);
				addressEntity.setAccountEntity(accEntity);
				addressJPA.save(addressEntity);
			}else{
				AddressEntity addEntity = new AddressEntity();
				addEntity.setRoad(addressBean.getRoad());
				addEntity.setWard(addressBean.getWard());
				addEntity.setDistrict(addressBean.getDistrict());
				addEntity.setCity(addressBean.getCity());
				addEntity.setStatus(true);
				addEntity.setAccountEntity(accEntity);
				addressJPA.save(addEntity);
			}

			System.out.println(accountBean.getPhone() + " + " + addressBean.getRoad() + " + " + addressBean.getWard()
					+ " + " + addressBean.getDistrict() + " + " + addressBean.getCity() );
//			clientJPA.save(cliEntity);

		}
		return String.format("redirect:/profile?accId=%d", accId);
	}

	@ModelAttribute("products")
	public List<ProductEntity> Product() {
		return productJPA.getProducts();
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
