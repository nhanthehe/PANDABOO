package com.asm.pandaboo.controllers;


import com.asm.pandaboo.entities.*;
import com.asm.pandaboo.jpa.*;
import com.asm.pandaboo.models.AccountBean;
import com.asm.pandaboo.models.ProductBean;
import com.asm.pandaboo.models.PromotionBean;
import com.asm.pandaboo.services.AccountService;
import com.asm.pandaboo.services.EmailService;
import com.asm.pandaboo.services.RoleService;
import com.asm.pandaboo.services.UploadFile;
import com.asm.pandaboo.utils.Contants;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    AccountsJPA accountJPA;

	@Autowired
	RoleJPA roleJPA;

	@Autowired
	RoleAccountJPA roleAccountJPA;

    @Autowired
    CategoryJPA categoryJPA;

    @Autowired
    UnitJPA unitJPA;

    @Autowired
    ProductJPA productJPA;

    @Autowired
    PaymentJPA paymentJPA;

    @Autowired
    PaymentDetailJPA paymentDetailJPA;

    @Autowired
    PromotionJPA promotionJPA;

    @Autowired
    ImageJPA imageJPA;

    @Autowired
	AddressJPA clientJPA;

    @Autowired
    ShoppingCartJPA shoppingCartJPA;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    HttpServletResponse response;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpSession session;

	@Autowired
	private AccountService accountService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/statistical")
	public String statistic(Model model) {
		int sumTotal = 0;
		if (paymentJPA.getSoldTotal() != null) {
			sumTotal = paymentJPA.getSoldTotal();
		} else {
			sumTotal = 0;
		}
		model.addAttribute("sumTotal", sumTotal);

		int soldAll = 0;
		if (paymentJPA.getSoldAll() != null) {
			soldAll = paymentJPA.getSoldAll();
		} else {
			soldAll = 0;
		}
		model.addAttribute("soldAll", soldAll);

		int cliAll = 0;
		if (paymentJPA.getCliAll() != null) {
			cliAll = paymentJPA.getCliAll();
		} else {
			cliAll = 0;
		}
		model.addAttribute("cliAll", cliAll);

		List<Object[]> total = paymentJPA.getTop3ProductTotal();
		model.addAttribute("total", total);

		return "seller/chart";
	}

    @GetMapping("/register")
    public String register() {
        return "admin/register";
    }

	@GetMapping("/forgotPass")
	public String ForgotPassword() {
		return "admin/ForgotPassword";
	}

	@GetMapping("/changePass")
	public String ChangePassword() {
		return "admin/ChangePassword";
	}

	@PostMapping("/changePass")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword, Model model) {
		AccountEntity acc = (AccountEntity) session.getAttribute("acc");

		if (!oldPassword.equals(acc.getPassword())) {
			model.addAttribute("error", "Mật khẩu cũ không chính xác!");
			return "admin/ChangePassword";
		}
		if (!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("error", "Mật khẩu mới và mật khẩu xác nhận không khớp!");
			return "admin/ChangePassword";
		}
		acc.setPassword(newPassword);
		accountJPA.save(acc);

		session.removeAttribute("acc");
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(@RequestParam(name = "path", defaultValue = "") String path, Model model) {
		model.addAttribute("path", path);
		return "admin/login";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		session.removeAttribute("acc");
		return "redirect:/login";
	}

//	@PostMapping("/login")
//	public String loginCheck(@RequestParam("path") String path,
//							 @RequestParam("username") String username,
//							 @RequestParam("password") String password,
//							 Model model,
//							 HttpServletResponse response,
//							 HttpSession session) {
//
//		AccountEntity acc = accountJPA.getAccountsEntityByAcc(username);
//
//		if (acc == null || !password.equals(acc.getPassword())) {
//			System.out.println("Login failed!");
//			model.addAttribute("path", path);
//			return "admin/login";
//		} else {
//			Cookie name = new Cookie(Contants.COOKIE_UERNAME, username);
//			response.addCookie(name);
//			System.out.println("Login successful!");
//			model.addAttribute("acc", acc);
//			session.setAttribute("acc", acc);
//			session.setAttribute("cliByAcc", acc);
//			model.addAttribute("cliByAcc", acc);
//
//			for (RoleAccountEntity roleAcc : acc.getAccRoleAccounts()) {
//				String roleName = roleAcc.getRoleAccountRoleEntity().getRole_name().toLowerCase();
//				if (roleName.equalsIgnoreCase("superAdmin")) {
//					return handleRedirect(path, "/statistical", Arrays.asList(
//							"/statistical", "/", "/changePass", "/checkout", "/promotionDetail",
//							"/promotion", "/productsDetail", "/products", "/profile"));
//				} else if (roleName.equalsIgnoreCase("admin")) {
//					return handleRedirect(path, "/products", Arrays.asList(
//							"/", "/changePass", "/checkout", "/promotionDetail", "/promotion",
//							"/productsDetail", "/products", "/profile"));
//				} else if (roleName.equalsIgnoreCase("client")) {
//					return handleRedirect(path, "/pandaBooIndex", Arrays.asList(
//							"/singleProduct", "/confirmation", "/checkout", "/category", "/cart"));
//				}
//			}
//		}
//
//		model.addAttribute("path", path);
//		return "admin/login";
//	}
@PostMapping("/login")
public AccountEntity loginCheck(@RequestParam("username") String username,
								@RequestParam("password") String password,
								HttpSession session) {
	AccountEntity acc = accountJPA.getAccountsEntityByAcc(username);
	if (acc == null || !password.equals(acc.getPassword())) {
		System.out.println("Login failed!");
		return null;
	} else {
		session.setAttribute("acc", acc);

		for (RoleAccountEntity roleAcc : acc.getAccRoleAccounts()) {
			String roleName = roleAcc.getRoleAccountRoleEntity().getRole_name().toLowerCase();
			if (roleName.equalsIgnoreCase("superAdmin")) {
				// Additional handling for SuperAdmin if needed
			} else if (roleName.equalsIgnoreCase("admin")) {
				// Additional handling for Admin if needed
			} else if (roleName.equalsIgnoreCase("client")) {
				// Additional handling for Client if needed
			}
		}

		System.out.println("Login successful!");
		return acc;
	}
}

	private String handleRedirect(String path, String defaultPath, List<String> validPaths) {
		if (!path.isEmpty() && validPaths.contains(path)) {
			return "redirect:" + path;
		}
		return "redirect:" + defaultPath;
	}


    @PostMapping("/register")
    public AccountEntity saveRegister(AccountBean accountBean,
            @RequestParam("avatar") MultipartFile file
    ) {
		AccountEntity accountEntity = new AccountEntity();
        String fileName = uploadFile.Upload(file);
		accountEntity.setUsername(accountBean.getUsername());
		accountEntity.setPassword(accountBean.getPassword());
		accountEntity.setFullname(accountBean.getFullname());
		accountEntity.setPhone(accountBean.getPhone());
		accountEntity.setEmail(accountBean.getEmail());
		accountEntity.setAvatar(fileName);
		accountEntity.setStatus(true);
		accountJPA.save(accountEntity);
		System.out.println("1");
		RoleAccountEntity roleAccount = new RoleAccountEntity();
		for(RoleEntity role : roleJPA.findAll()){
			if(role.getRole_name().equalsIgnoreCase("client")){
				RoleEntity roleEntity = roleJPA.getRoleByName("client");
				roleAccount.setRoleAccountAccEntity(accountEntity);
				roleAccount.setRoleAccountRoleEntity(roleEntity);
				System.out.println("2");
				roleAccountJPA.save(roleAccount);

				ShoppingCartEntity cartEntity = new ShoppingCartEntity();
				cartEntity.setCartAccountEntity(accountEntity);
				shoppingCartJPA.save(cartEntity);
				System.out.println("3");
			}
		}
		return accountEntity;
    }


	@GetMapping("/client_list")
	public String client(Model model) {
		List<AddressEntity> clients = clientJPA.findAll();
		System.out.println("Fetched clients: " + clients);
		model.addAttribute("clients", clients);
		return "seller/client_list";
	}


	@GetMapping("/staff")
	public String showAddStaffList(Model model) {
		model.addAttribute("selectedRoles", List.of());
		model.addAttribute("roles", roleService.findAll()); // Ensure roles are set
		return "seller/staff";
	}

	@GetMapping("/staff_detail")
	public String showAddStaffForm() {
		return "seller/staff_detail";
	}

	@PostMapping("/add-staff")
	public String addStaff(@ModelAttribute AccountBean accountBean,
						   @RequestParam("avatar") MultipartFile file,
						   @RequestParam(required = false) List<Integer> roleIds, Model model) {
		String fileName = uploadFile.Upload(file);
		AccountEntity account = new AccountEntity();
		account.setUsername(accountBean.getUsername());
		account.setFullname(accountBean.getFullname());
		account.setPassword(accountBean.getPassword());
		account.setPhone(accountBean.getPhone());
		account.setEmail(accountBean.getEmail());
		account.setAvatar(fileName);
		account.setStatus(true);

		accountService.save(account);

		if (roleIds != null) {
			List<RoleAccountEntity> roleAccountEntities = roleIds.stream()
					.filter(roleId -> roleId != null) // Filter out null values
					.map(roleId -> {
						RoleAccountEntity roleAccount = new RoleAccountEntity();
						RoleEntity role = roleService.findById(roleId);
						roleAccount.setRoleAccountAccEntity(account);
						roleAccount.setRoleAccountRoleEntity(role);
						return roleAccount;
					}).collect(Collectors.toList());

			roleAccountJPA.saveAll(roleAccountEntities);

			// Set the roles to the account
			account.setAccRoleAccounts(roleAccountEntities);
		}

		// Send email notification
		try {
			emailService.sendMail(account.getEmail(), "Chào mừng bạn đã trở thành thành viện của PANDABOO Shop",
					"Dear " + account.getFullname() + ",<br><br>Bạn đã chính hức trở thành nhân viên của PANDABOO" +
							"<br>Tên tài khoản:"+account.getUsername()+"<br>Mật khẩu:"+account.getPassword());
		} catch (MessagingException e) {
			e.printStackTrace();
			// Handle the exception, maybe log it or display a message
		}

		return "redirect:/staff_detail";
	}



	@GetMapping("/update-staff/{id}")
	public String showUpdateStaffForm(@PathVariable String id, Model model) {
		Optional<AccountEntity> accountOptional = accountJPA.findById(id);
		if(accountOptional.isPresent()){
			AccountEntity accountEntity = accountOptional.get();
			model.addAttribute("account", accountEntity);
			model.addAttribute("roles", roleService.findAll());
			model.addAttribute("selectedRoles", accountEntity.getAccRoleAccounts().stream()
					.map(ra -> ra.getRoleAccountRoleEntity().getRole_id()).collect(Collectors.toList()));
			return "seller/staff_detail";
		}
		return "redirect:/staff_detail"; // Redirect if account not found
	}

	@PostMapping("/update-staff")
	public String updateStaff(AccountBean accountBean,Model model,
							  @RequestParam("acc_id") String id,
							  @RequestParam("avatar") MultipartFile file,
							  @RequestParam List<Integer> roleIds) {
		String fileName = uploadFile.Upload(file);
		Optional<AccountEntity> accountOptional = accountJPA.findById(id);
		if(accountOptional.isPresent()){
			AccountEntity account = accountOptional.get();
			account.setUsername(accountBean.getUsername());
			account.setFullname(accountBean.getFullname());
			account.setPassword(accountBean.getPassword());
			account.setPhone(accountBean.getPhone());
			account.setEmail(accountBean.getEmail());
			account.setAvatar(fileName);
			account.setStatus(true);
			accountService.save(account);

			// Xóa các vai trò hiện tại của tài khoản
			for (RoleAccountEntity roleAcc : account.getAccRoleAccounts()){
				if(roleAcc != null){
					roleAccountJPA.delete(roleAcc);
				}else{
					break;
				}
			}
			// Thêm các vai trò mới cho tài khoản
            if (roleIds != null) {
                List<RoleAccountEntity> roleAccountEntities = roleIds.stream()
                        .filter(roleId -> roleId != null) // Filter out null values
                        .map(roleId -> {
                            RoleAccountEntity roleAccount = new RoleAccountEntity();
                            RoleEntity role = roleService.findById(roleId);
                            roleAccount.setRoleAccountAccEntity(account);
                            roleAccount.setRoleAccountRoleEntity(role);
                            return roleAccount;
                        }).collect(Collectors.toList());

                roleAccountJPA.saveAll(roleAccountEntities);

                // Set the roles to the account
                account.setAccRoleAccounts(roleAccountEntities);
            }
			return "redirect:/staff";
		}
		return "seller/staff_detail";
	}

	@PostMapping("/delete-staff")
	public String deleteStaffDetail(@RequestParam("acc_id") String acc_id) {
		Optional<AccountEntity> accountOptional = accountJPA.findById(acc_id);
		if(accountOptional.isPresent()){
			AccountEntity accountEntity = accountOptional.get();
			accountEntity.setStatus(false);
			accountJPA.save(accountEntity);
		}
		return "redirect:/staff";
	}


	// ---------------------------------------PRODUCTS----------------------------------------

	@GetMapping("/products")
	public String products(Model model, @RequestParam("page") Optional<Integer> page) {
		int currentPage = page.orElse(1);
		if (currentPage < 1) {
			currentPage = 1;
		}
		int pageSize = 4;
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<ProductEntity> productsPage = productJPA.findAll(pageable);

		model.addAttribute("products", productsPage);

		int totalPages = productsPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "seller/products";
	}

	@PostMapping("/products")
	public String productsSearch(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("search") Optional<String> searchOptional) {
		int currentPage = page.orElse(1);
		if (currentPage < 1) {
			currentPage = 1;
		}
		int pageSize = 4;
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<ProductEntity> productsPage = null;
		if (searchOptional.isPresent()) {
			productsPage = productJPA.getProductByProdName("%" + searchOptional.get() + "%", pageable);
		} else {
			productsPage = productJPA.findAll(pageable);
		}
		model.addAttribute("products", productsPage);
		int totalPages = productsPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "seller/products";
	}

	@GetMapping("/productsDetail")
	public String productsShow() {
		return "seller/products_detail";
	}

	@PostMapping("/productsDetail")
	public String saveProductsDetail(@Valid ProductBean productBean, BindingResult error,
									 @RequestParam("red_price") String red_price, @RequestParam("images") ArrayList<MultipartFile> file,
									 Model model) {
		ProductEntity productEntity = new ProductEntity();
		Optional<CategoryEntity> catOptional = categoryJPA.findById(String.valueOf(productBean.getCat_id()));
		Optional<UnitEntity> unitOptional = unitJPA.findById(String.valueOf(productBean.getUnit_id()));
		if (catOptional.isPresent() && unitOptional.isPresent()) {
			productEntity.setCategoryEntity(catOptional.get());
			productEntity.setUnitEntity(unitOptional.get());
		}
		productEntity.setProd_name(productBean.getProd_name());
		productEntity.setPrice(productBean.getPrice());
		if (red_price.equals("")) {
			productEntity.setRed_price(0);
		} else {
			productEntity.setRed_price(productBean.getRed_price());
		}
		productEntity.setDescriptions(productBean.getDescriptions());
		productEntity.setStatus(true);
		ProductEntity productSaveEntity = productJPA.save(productEntity);
		for (MultipartFile image : productBean.getImages()) {
			String fileName = uploadFile.Upload(image);
			if (fileName != null) {
				ImageEntity imageEntity = new ImageEntity();
				imageEntity.setName(fileName);
				imageEntity.setImageProductEntity(productSaveEntity);
				imageJPA.save(imageEntity);
			} else {
				model.addAttribute("NotImage", "Ảnh không được để trống!");
			}
		}
		return "seller/products_detail";
	}

	@GetMapping("/update-product")
	public String updateProductDetail(@RequestParam("id") String id, Model model) {
		Optional<ProductEntity> prodOptional = productJPA.findById(id);
		if (prodOptional.isPresent()) {
			ProductEntity product = prodOptional.get();
			model.addAttribute("product", product);
		}
		return "seller/products_detail";
	}

	@PostMapping("/update-product")
	public String saveUpdateProductDetail(@Valid ProductBean productBean, BindingResult error,
			@RequestParam("prod_id") String prod_id, @RequestParam("red_price") String red_price, Model model) {
		Optional<ProductEntity> productOptional = productJPA.findById(prod_id);
		if (productOptional.isPresent()) {
			ProductEntity productEntity = productOptional.get();
			Optional<CategoryEntity> catOptional = categoryJPA.findById(String.valueOf(productBean.getCat_id()));
			Optional<UnitEntity> unitOptional = unitJPA.findById(String.valueOf(productBean.getUnit_id()));
			if (catOptional.isPresent() && unitOptional.isPresent()) {
				productEntity.setCategoryEntity(catOptional.get());
				productEntity.setUnitEntity(unitOptional.get());
			}
			productEntity.setProd_name(productBean.getProd_name());
			productEntity.setPrice(productBean.getPrice());
			if (red_price.equals("")) {
				productEntity.setRed_price(0);
			} else {
				productEntity.setRed_price(productBean.getRed_price());
			}
			productEntity.setDescriptions(productBean.getDescriptions());
			productEntity.setStatus(true);
			ProductEntity productSaveEntity = productJPA.save(productEntity);
			for (MultipartFile image : productBean.getImages()) {
				String fileName = uploadFile.Upload(image);
				if (fileName != null) {
					ImageEntity imageEntity = new ImageEntity();
					imageEntity.setName(fileName);
					imageEntity.setImageProductEntity(productSaveEntity);
					imageJPA.save(imageEntity);
				}
			}
		}
		return "redirect:/products";
	}

	@PostMapping("/delete-product")
	public String deleteProductDetail(@RequestParam("prod_id") String prod_id) {
		Optional<ProductEntity> productOptional = productJPA.findById(prod_id);
		if (productOptional.isPresent()) {
			ProductEntity productEntity = productOptional.get();
			productEntity.setRed_price(productEntity.getRed_price());
			productEntity.setStatus(false);
			productJPA.save(productEntity);
		}
		return "redirect:/products";
	}

	@PostMapping("/delete-productImg/{id}")
	public String deleteProductImage(@PathVariable("id") int id) {
		Optional<ImageEntity> imgOptional = imageJPA.findById(String.valueOf(id));
		if (imgOptional.isPresent()) {
			ImageEntity imageEntity = imgOptional.get();
			imageJPA.delete(imageEntity);
			uploadFile.removeFile(imageEntity.getName());
			return "redirect:/update-product?id=" + imageEntity.getImageProductEntity().getProd_id();
		}
		return "redirect:/products";
	}

	// -----------------------------------Category-------------------------------

	@PostMapping("/add_category")
	public String saveCaterory(@RequestParam("cat_name") String cat_name) {
		CategoryEntity entity = new CategoryEntity();
		entity.setCat_name(cat_name);
		categoryJPA.save(entity);
		return "redirect:/productsDetail";
	}

	@PostMapping("/delete-category")
	public String deleteCategory(@RequestParam("id") String id) {
		Optional<CategoryEntity> catOptional = categoryJPA.findById(id);
		if (catOptional.isPresent()) {
			CategoryEntity categoryEntity = catOptional.get();
			categoryJPA.delete(categoryEntity);
		}
		return "redirect:/productsDetail";
	}

	// ---------------------------------UNIT-------------------------------

	@PostMapping("/add_unit")
	public String saveUnit(@RequestParam("unit_name") String unit_name) {
		UnitEntity entity = new UnitEntity();
		entity.setUnit_name(unit_name);
		unitJPA.save(entity);
		return "redirect:/productsDetail";
	}

	@PostMapping("/delete-unit")
	public String deleteUnit(@RequestParam("id") String id) {
		Optional<UnitEntity> unitOptional = unitJPA.findById(id);
		if (unitOptional.isPresent()) {
			UnitEntity unitEntity = unitOptional.get();
			unitJPA.delete(unitEntity);
		}
		return "redirect:/productsDetail";
	}

	// -----------------------------------PAYMENTS-------------------------------

	@GetMapping("/payments")
	public String payments(Model model, @RequestParam("page") Optional<Integer> page) {
		int currentPage = page.orElse(1);
		if (currentPage < 1) {
			currentPage = 1;
		}
		int pageSize = 5;
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<PaymentEntity> paymentsPage = paymentJPA.findAll(pageable);

		model.addAttribute("payments", paymentsPage);

		int totalPages = paymentsPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "seller/payments";
	}

	@GetMapping("/paymentsDetail")
	public String paymentsDetail(@RequestParam("payId") int payId, Model model) {
		List<PaymentDetailEntity> paymaDetailEntities = paymentDetailJPA.getListPaymentDetailByPayId(payId);
		model.addAttribute("paymentDetails", paymaDetailEntities);
		return "seller/paymentsDetail";
	}

	@GetMapping("/update-status-payment")
	public String updateStstusPayment(@RequestParam("payId") int payId, @RequestParam("status") int status) {
		Optional<PaymentEntity> paymentoptional = paymentJPA.findById(String.valueOf(payId));
		if (paymentoptional.isPresent()) {
			PaymentEntity paymanetEntity = paymentoptional.get();
			paymanetEntity.setStatus(status);
			paymentJPA.save(paymanetEntity);
		}
		return "redirect:/payments";
	}

	// ---------------------------PROMOTION--------------------------------

	@GetMapping("/promotion")
	public String promotion(Model model) {
		model.addAttribute("promotions", promotionJPA.findAll());
		return "seller/promotion";
	}

	@GetMapping("/add-promotion")
	public String addPromotion(Model model) {
		model.addAttribute("promotionBean", new PromotionBean());
		return "seller/promotion_detail";
	}

	@PostMapping("/add-promotion")
	public String addPromotionSave(@Valid @ModelAttribute("promotionBean") PromotionBean promotionBean,
			BindingResult error, Model model) {
		if (error.hasErrors()) {
			model.addAttribute("error", error);
			return "seller/promotion_detail";
		}

		PromotionEntity promotionEntity = new PromotionEntity();
		promotionEntity.setProm_name(promotionBean.getProm_name());
		promotionEntity.setStart_price(promotionBean.getStart_price());
		promotionEntity.setStart_date(promotionBean.getStart_date());
		promotionEntity.setEnd_date(promotionBean.getEnd_date());
		promotionEntity.setDiscount(promotionBean.getDiscount());
		promotionEntity.setStatus(true);
		promotionEntity.setNote(promotionBean.getNote());
		promotionJPA.save(promotionEntity);

		return "redirect:/promotion";
	}

	@GetMapping("/update-promotion")
	public String updatePromotion(@RequestParam("prom_id") int prom_id, Model model) {
		Optional<PromotionEntity> prOptional = promotionJPA.findById(prom_id);
		if (prOptional.isPresent()) {
			PromotionEntity promotionEntity = prOptional.get();
			model.addAttribute("promotion", promotionEntity);
		}
		return "seller/promotion_detail";
	}

	@PostMapping("/update-promotion")
	public String updatePromotion(@Valid @ModelAttribute("promotionBean") PromotionBean promotionBean,
			BindingResult error, Model model, @RequestParam("prom_id") int prom_id) {
		if (error.hasErrors()) {
			model.addAttribute("error", error);
			return "seller/promotion_detail";
		}

		Optional<PromotionEntity> prOptional = promotionJPA.findById(prom_id);
		if (prOptional.isPresent()) {
			PromotionEntity promotionEntity = prOptional.get();
			promotionEntity.setProm_name(promotionBean.getProm_name());
			promotionEntity.setStart_price(promotionBean.getStart_price());
			promotionEntity.setStart_date(promotionBean.getStart_date());
			promotionEntity.setEnd_date(promotionBean.getEnd_date());
			promotionEntity.setDiscount(promotionBean.getDiscount());
			promotionEntity.setStatus(true);
			promotionEntity.setNote(promotionBean.getNote());
			promotionJPA.save(promotionEntity);
		}
		return "redirect:/promotion";
	}

	@PostMapping("/delete-promotion")
	public String deletePromotion(@RequestParam("prom_id") int prom_id) {
		Optional<PromotionEntity> prOptional = promotionJPA.findById(prom_id);
		if (prOptional.isPresent()) {
			PromotionEntity promotionEntity = prOptional.get();
			promotionEntity.setStatus(false);
			promotionJPA.save(promotionEntity);
		}
		return "redirect:/promotion";
	}

	@GetMapping("/removedProducts")
	public String removedProductsShow() {
		return "seller/removedProducts";
	}

	@PostMapping("/restore-product")
	public String restoreProducts(@RequestParam("prod_id") String prod_id) {
		Optional<ProductEntity> productOptional = productJPA.findById(prod_id);
		if (productOptional.isPresent()) {
			ProductEntity productEntity = productOptional.get();
			productEntity.setRed_price(productEntity.getRed_price());
			productEntity.setStatus(true);
			productJPA.save(productEntity);
		}
		return "redirect:/removedProducts";
	}

    // -----------------------------------Model
    // Attribute------------------------------------

    @ModelAttribute("products")
    public List<ProductEntity> Product() {
        return productJPA.findAll();
    }

    @ModelAttribute("categorys")
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

	@ModelAttribute("roles")
	public List<RoleEntity> getRoles() {
		return roleJPA.findAll();
	}

    @ModelAttribute("promotions")
    public List<PromotionEntity> getPromotionEntities() {
        return promotionJPA.findAll();
    }

    @ModelAttribute("payments")
    public List<PaymentEntity> getPaymentEntities() {
        return paymentJPA.findAll();
    }

}
