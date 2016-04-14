local TECalculate = {}

TECalculate.__index=TECalculate
local JSON = require("JSON")

--定义的全局变量
local orderDiscount=0 				-- 菜品的整单折扣
local orderBasePrice=0				-- 总单价格 ＝ a basePrice*count + b basePric*count
local orderAddTax=0					-- 整单 所有税费 ＝＝ a addTaxAmount * count + b addTaxAmount * count
local orderDiscountPrice=0			-- 所有菜品的折后价格 ＝a itemPrice＊count ＋ b itemPrice*count
local orderDiscountAmount=0			-- 所有总的折扣价格 = A discountAmount*count + B discountAmount*count 
local paymentMethod=0   			-- 付款方式，如果为 1 则需要计算round 否则 不需要计算
local round=0 						-- round


local itemDiscount=0 				-- 单个菜品的折扣率
local priceAdjustment=0 			-- 单个菜品的加价费用
local menuPrice=0					-- 单个菜品的价格
local basePrice=0					-- 单个菜品的总价格 ＝ 菜品价格(menuPrice)＋菜品加价(priceAdjustment)＋ 单个菜品加料总价(modifierAmount)


local modifierAmount=0  			-- 单个菜品加料总价格 a unitPrice * a count + b unitPrice * b count
local discountAmount=0				-- 单凭折扣后的价格 basePrice*(itemDiscount+orderDiscount-itemDiscount*orderDiscount)
local itemPrice=0					-- 单个菜品实际的价格 basePrice -discountAmount


local addTaxAmount=0 				-- 单个菜品的税务总价格 ＝ gst ＋ server chrg
local amount=0						-- 菜品服务税率价格

function TECalculate:new( orderDetail )
 	-- body
 	local self={}
 	setmetatable(self,TECalculate)
	self.odDetail=orderDetail
	return self
 end 


 function TECalculate:CalculateData()
 	-- body
	orderDiscount=self.odDetail.orderDiscount
	--menuItem array
	menuAllTab=self:menuItemData(self.odDetail.item)
	self.odDetail.basePrice=string.format("%.2f",menuAllTab.currentMenuBasePrice)
	self.odDetail.discountPrice=string.format("%.2f",menuAllTab.currentMenuItemPrice)
	self.odDetail.discountAmount=string.format("%.2f",menuAllTab.currentMenuDiscount)
	self.odDetail.totalGst=string.format("%.2f",menuAllTab.currentMenuGST)
	self.odDetail.totalServiceTax=string.format("%.2f",menuAllTab.currentMenuServer)
	self.odDetail.addTax = string.format("%.2f",menuAllTab.currentMenuAddTax)


	paymentMethod=self.odDetail.payment.paymentMethod

	-- 如果paymentMethod （付款方式） 为 1 （现金付款） 则需要计算round 
	if(paymentMethod==1) then
		lastValue=tonumber(string.format("%.2f",tonumber(self.odDetail.discountPrice+self.odDetail.addTax)%.1))
		--折扣
		if(lastValue==0.05)then
			round=0
		elseif(lastValue>0.05)then
			result=tonumber(self.odDetail.basePrice)+(0.1-lastValue)
			round=lastValue-0.1
		elseif(lastValue<0.05)then
			result=tonumber(self.odDetail.basePrice)-lastValue;
			round=lastValue
		end
	end

	round=string.format("%.2f",round)
	self.odDetail.round=round

	self.odDetail.payPrice=string.format("%.2f",self.odDetail.discountPrice+self.odDetail.addTax-round)

	--print("item--->>",JSON:encode(self.odDetail))
	return self.odDetail
 end

-- menuItem 菜单item
function TECalculate:menuItemData( menuDateItem)
	-- body
	local currentMenuBasePrice=0 		-- 当前菜品item总价格
	local currentMenuAddTax=0		-- 当前整单所有税率
	local currentMenuItemPrice=0	-- 当前所有菜品的折后价格
	local currentMenuDiscount=0		-- 当前所有总的折扣价格
	local currentMenuGST=0			-- 当前所有总GST税率
	local currentMenuServer=0		-- 当前所有总Server税率
	if menuDateItem==nil then
		return
	end
	
	for i=1,#(menuDateItem) do
		menuItem=menuDateItem[i]
		local menuCount=menuItem.count
		
		--判断是否为删除的菜品，如果是则不进行计算
		if(menuItem.isDeleted~=true) then
			menuPrice=menuItem.menuPrice
			itemDiscount=menuItem.itemDiscount
			priceAdjustment=menuItem.priceAdjustment
			-- modifier 加料菜品 array
			modifierAmount=self:ModifierData(menuItem.modifier)
			menuItem.modifierAmount=modifierAmount  --设置单个菜品加料总价格

			basePrice=menuPrice+priceAdjustment+modifierAmount
			menuItem.basePrice=basePrice			--设置单个菜品的总价格


			if menuItem.allowCustomDiscount == false then
				discountAmount=0
			else
				discountAmount=basePrice*(itemDiscount+orderDiscount-itemDiscount*orderDiscount)
			end
			
			menuItem.discountAmount=discountAmount  --设置单个菜品整个的折扣

			itemPrice=basePrice -discountAmount
			menuItem.itemPrice=itemPrice     		--设置单个菜品实际的价格

			itemTaxTab=self:ItemTaxData(menuItem.itemTax)
			addTaxAmount=itemTaxTab.currentItemTaxDataItem

			menuItem.addTaxAmount=addTaxAmount		--设置单个菜品总税率

			currentMenuBasePrice = currentMenuBasePrice + basePrice * menuCount 
			currentMenuAddTax = currentMenuAddTax+addTaxAmount * menuCount
			currentMenuItemPrice = currentMenuItemPrice + itemPrice * menuCount
			currentMenuDiscount = currentMenuDiscount + discountAmount * menuCount
			currentMenuGST = currentMenuGST + itemTaxTab.currentGst * menuCount
			currentMenuServer = currentMenuServer + itemTaxTab.currentServerChrg * menuCount
		end
	end
	menuTab={currentMenuBasePrice=currentMenuBasePrice,currentMenuAddTax=currentMenuAddTax,currentMenuItemPrice=currentMenuItemPrice,currentMenuDiscount=currentMenuDiscount,currentMenuGST=currentMenuGST,currentMenuServer=currentMenuServer}

	return  menuTab
end



-- modifier 加料菜品 array 处理方法
function TECalculate:ModifierData(modifierDateItem)
	-- body

	local  currentModifierAmount=0 --当前加料总价格
	if modifierDateItem ==nil then
		return
	end
	for i=1,#(modifierDateItem) do
		modifierItem=modifierDateItem[i]
		currentModifierAmount=currentModifierAmount+(modifierItem.unitPrice * modifierItem.count)
	end
	--print("currentModifierAmount--->>",currentModifierAmount)
	return currentModifierAmount

end

-- itemTax 单个菜品的税率
function TECalculate:ItemTaxData( itemTaxDataItem )
	-- body
	if itemTaxDataItem==nil then
		return
	end
	-- CYJ 
--	if self.odDetail.restaurantId == '5649569e72c6892bf008df49' then
--		tab = self:CYJCalculateRat(itemTaxDataItem)
--	else
--		tab = self:DefaultCalculateRat(itemTaxDataItem)
--	end
	tab = self:CYJCalculateRat(itemTaxDataItem)

	return tab

end


-- default 方式计算gst service
function TECalculate:DefaultCalculateRat( itemTaxDataItem )
	-- body
	local  currentItemTaxDataItem=0 	--当前菜品所有的税率总和
	local  currentServerChrg=0			--当前server 税率
	local  currentGst=0					--当前get 税率
	if self.odDetail.type ~= 2 then
		for i=1,#(itemTaxDataItem) do
			itemTaxItem=itemTaxDataItem[i]
			if itemTaxItem.enable==true then
				amount=itemTaxItem.rate * itemPrice
				itemTaxItem.amount=amount
				currentItemTaxDataItem=currentItemTaxDataItem + amount
				if itemTaxItem.code=='service' then
					currentServerChrg=itemTaxItem.rate * itemPrice
				elseif itemTaxItem.code=='gst' then
					currentGst=itemTaxItem.rate * itemPrice
				end
			end
		end
	end

	tab={currentItemTaxDataItem=currentItemTaxDataItem,currentGst=currentGst,currentServerChrg=currentServerChrg}
	--print("DefaultCalculateRat--->>",JSON:encode(tab))
	return tab
end


-- CYJ 方式计算gst service
function TECalculate:CYJCalculateRat( itemTaxDataItem )
	-- body
	local  currentItemTaxDataItem=0 	--当前菜品所有的税率总和
	local  currentServerChrg=0			--当前server 税率
	local  currentGst=0					--当前get 税率

	if self.odDetail.type ~= 2 then
		for i=1,#(itemTaxDataItem) do
			itemTaxItem=itemTaxDataItem[i]
			if itemTaxItem.enable==true then
				amount=itemTaxItem.rate * basePrice
				itemTaxItem.amount=amount
				currentItemTaxDataItem=currentItemTaxDataItem + amount
				if itemTaxItem.code=='service' then
					currentServerChrg=itemTaxItem.rate * basePrice
				elseif itemTaxItem.code=='gst' then
					currentGst=itemTaxItem.rate * basePrice
				end
			end
		end
	end

	tab={currentItemTaxDataItem=currentItemTaxDataItem,currentGst=currentGst,currentServerChrg=currentServerChrg}
	--print("CYJCalculateRat--->>",JSON:encode(tab))
	return tab
end


 return TECalculate
