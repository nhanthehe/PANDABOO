CREATE database PANDABOO
use PANDABOO
------Tài khảo------
create table accounts(
	acc_id int identity primary key,
	username varchar(36) not null,
	password varchar(20) not null,
	fullname nvarchar(72) not null,
	avatar nvarchar(255),
	roles bit not null,
	status bit default 1
)
GO

select * from accounts
INSERT INTO accounts (username, password, fullname, avatar, roles, status)
VALUES ('huynh', '123456', 'Lương Thị Như Huỳnh', 'avatar2.jpg', 1, 1);
GO


create table clients(
	cli_id int identity primary key,
	email varchar(72),
	phone varchar(10),
	road nvarchar(255),
	ward nvarchar(255),
	district nvarchar(255),
	city nvarchar(255),	
	acc_id int foreign key references accounts(acc_id),
	status bit default 1
)
GO

create table promotions(
	prom_id int identity primary key,
	prom_name nvarchar(255) not null,
	start_price float not null,
	start_date date not null,
	end_date date not null,
	discount float not null,
	note nvarchar(max),
	status bit default 1
)
GO

create table payments(
	pay_id int identity(1,1) primary key,
	cli_id int foreign key references clients(cli_id),
	prom_id int foreign key references promotions(prom_id),
	pay_date date,
	total_quantity int,
	order_total float,
	pay_methods nvarchar(72),
	status int 
)
GO

create table paymentdetails(
	paydetail_id int identity(1,1) primary key,
	pay_id int foreign key references payments(pay_id),
	prod_id int ,
	prod_name nvarchar(72),
	prod_quantity int,
	red_price float,
	prod_images nvarchar(255)
)
GO

create table units(
	unit_id int identity primary key,
	unit_name nvarchar(255) not null,	
)
GO

create table categories(
	cat_id int identity primary key,
	cat_name nvarchar(255) not null,	
)

create table products(
	prod_id int identity primary key,
	prod_name nvarchar(72) not null,
	cat_id int foreign key references categories(cat_id),
	unit_id int foreign key references units(unit_id),
	price float not null,
	red_price float not null,
	descriptions nvarchar(max),
	status bit default 1
)
GO

create table images(
	img_id int identity primary key,
	prod_id int foreign key references products(prod_id),
	name nvarchar(255) not null,	
)
GO

create table shoppingcarts(
	cart_id int identity primary key,
	cli_id int foreign key references clients(cli_id)
)
GO



create table paydetails(
	detail_id int identity primary key,
	prod_id int foreign key references products(prod_id),
	cart_id int foreign key references shoppingcarts(cart_id),
	quantity int not null
)
GO
insert into paydetails(prod_id,cart_id,quantity)
values(1,1,4),
(1,1,2)

delete accounts where acc_id=4
update products set status = 1 where prod_id =2
select * from accounts

select * from shoppingcarts
select * from products
select * from images
select * from clients
select * from paydetails
select * from paymentdetails
select * from payments where cli_id=2
select * from promotions
select o.prod_name,o.red_price from paydetails p 
inner join shoppingcarts s on s.cart_id=p.cart_id
inner join clients c on c.cli_id = s.cli_id
inner join products o on o.prod_id=p.prod_id
where c.cli_id = 1

select * from paymentdetails
select * from payments
select COUNT(DISTINCT cli_id) from payments where status = 2
SELECT top 3 pd.prod_id, pd.prod_name, SUM(red_price*prod_quantity) AS total_revenue
FROM paymentdetails pd
GROUP BY pd.prod_id, pd.prod_name
ORDER BY total_revenue DESC;

select * from products where cat_id=1

SELECT SUM(order_total) AS total_revenue
FROM payments

select COUNT(prod_id) as daban, prod_id, prod_name from paymentdetails
where prod_id = 1
group by prod_id,prod_name
select * from paymentdetails