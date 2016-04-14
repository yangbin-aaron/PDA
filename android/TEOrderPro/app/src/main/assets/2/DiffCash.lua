
--[[
	cash 相关的print
]]

local DiffCash={}

DiffCash.__index=DiffCash
local printName= 'jprint' 
if printName == 'jprint' then
    printH1 = 70
    printH2 = 35
elseif printName == 'xprint' then
    printH1 = 120
    printH2 = 60
end



function DiffCash:new( key,language,orderDetail )
	-- body
	local self={}
	setmetatable(self,DiffCash)
	self.printer   = require("PrintUtil"):new(key) 
  self.odData    = orderDetail
  self.localInfo = require("LocalInfo"):new(language)
	return self
end

--[[
	打印头文件
]]
function DiffCash:printTopTitle(  )
	-- body
	-- 店铺名称
   	self.printer:initPrinter()
   	self.printer:setFontStyle(2)
   	self.printer:setFontBold(true)
   	self.printer:setAlignment(1)
   	self.printer:sendText(self.localInfo.title)
   	self.printer:goPaperPoints(printH1)

   	-- 店铺基本信息
   	self.printer:initPrinter()
   	self.printer:setAlignment(1)
   	self.printer:sendText(self.localInfo.subTitle)
   	self.printer:goPaperPoints(printH1)

   	local tableNO
  	local orderType
  	-- 桌号  订单类型
   	if(self.odData.didTableCode~='') then 
	    tableNO = self.localInfo:getTableNO(self.odData.didTableCode..'->'..self.odData.tableCode)
      orderType = self.localInfo:getOrderType(self.odData.type)
	    self.printer:initPrinter()
	    self.printer:setColorReversal(true)
	    self.printer:setFontStyle(2)
	    self.printer:setFontBold(true)
	    self.printer:justifyText(tableNO, orderType, 4)
	    self.printer:goPaperPoints(printH1)
  	elseif(self.odData.type == 2) then
     	orderType = self.localInfo:getOrderType(self.odData.type)..' '..self.odData.takeAwayNo
     	self.printer:initPrinter()
     	self.printer:setFontStyle(5)
     	self.printer:setAlignment(1)
     	self.printer:setFontBold(true)
     	self.printer:sendText(orderType)
     	--self.printer:justifyText(tableNO, orderType, 4)
     	self.printer:goPaperPoints(printH1)
 	else
    	tableNO = self.localInfo:getTableNO(self.odData.tableCode)
    	orderType = self.localInfo:getOrderType(self.odData.type)
     	self.printer:initPrinter()
     	self.printer:setFontStyle(2)
     	self.printer:setFontBold(true)
     	self.printer:justifyText(tableNO, orderType, 4)
     	self.printer:goPaperPoints(printH1)
 end
   tableNO   = nil
   orderType = nil


-- 订单号码  人数
   local orderNO  = self.localInfo:getOrderNO(self.odData.orderNo)
   local pax =  self.localInfo:getPax(self.odData.customerNum)
   self.printer:initPrinter()
   self.printer:setFontBold(true)
   self.printer:justifyText(orderNO, pax, 2)
   self.printer:goPaperPoints(10)
   orderNO = nil
   pax = nil
   
   -- 收据   时间日期
   -- local invoice  = self.localInfo:getInvoice(self.odData.payment.invoiceNo)
   -- local currTime = ''--os.date('%Y-%m-%d %H:%M:%S')
   -- self.printer:initPrinter()
   -- self.printer:setFontBold(true)
   -- self.printer:justifyText(invoice, currTime, 2)
   -- self.printer:goPaperPoints(10)
   -- invoice  = nil
   -- currTime = nil
   
   -- 收据名称(收银单)
   	local typeName
    self.printer:initPrinter()
    self.printer:goPaperPoints(printH2)
   	if TE_Tag=="cancel_order" or TE_Tag == "cancel_order_pay" then
      	typeName = self.localInfo:getReceiptType('cancel')
        self.printer:setColorReversal(true)
   	else
        typeName = self.localInfo:getReceiptType('cash')
   	end
   
    self.printer:setAlignment(1)
    self.printer:setFontStyle(2)
    self.printer:setFontBold(true)
    self.printer:sendText(typeName)
    self.printer:goPaperPoints(printH1)
    typeName = nil

   	-- 分割线
   	self.printer:initPrinter()
   	self.printer:setFontBold(true)
  
    local spliter = self.localInfo:getDefaultDetailTitle()
    self.printer:sendText(spliter)
    
   	self.printer:goPaperPoints(1)
   	self.printer:initPrinter()
   	self.printer:printLineSpliter()
   	self.printer:goPaperPoints(1)
end


-- 打印菜品
function DiffCash:printDishes(dishList)
   if dishList == nil then
      return
   end
   
   for i = 1, #(dishList) do
      local dishItem = dishList[i]
      if dishItem.isDeleted == nil or dishItem.isDeleted == false then
         self:printDishItem(dishList[i])
         self.printer:goPaperPoints(10)
      end
   end
end

-- 打印菜品详情
function DiffCash:printDishItem( item )
	-- body
	-- 打印菜品的数量，名称，计价
   	local itemCount = tostring(item.count)
   	local itemName  = '(' .. item.code .. ') ' .. item.name[self.localInfo.language]
   	local itemPrice = string.format("%.2f", item.count * item.menuPrice)

   	if itemCount:len() < 2 then
      	itemCount = string.rep(' ', 2-itemCount:len()) .. itemCount
   	end

   	if itemPrice:len() < 8 then
      	itemPrice = string.rep(' ', 8-itemPrice:len()) .. itemPrice
   	end

   	local ltxt = itemCount
   	local ctxt = itemName
   	local rtxt = itemPrice

   	ctxt = ctxt:gsub("\r", "")
   	ctxt = ctxt:gsub("\n", "")
   
   	self.printer:initPrinter()
   	self.printer:setAlignment(0)
   	self.printer:setFontBold(true)
   	self.printer:justifyThreeText(ltxt, ctxt, rtxt,{left = 6, right = 8}, 2)

   	--打印附加属性
   	self:printDishItemOptions(item.modifier, string.rep(' ', 6))
   
   	--打印加价
   	if (item.priceAdjustment ~= nil) and (tonumber(item.priceAdjustment) ~= 0) then
      local addPrice = self.localInfo:getAddPrice(string.format("%.2f",item.priceAdjustment * itemCount))    
      self.printer:goPaperPoints(1)
      self.printer:setAlignment(2)
      self.printer:sendText(addPrice)
   	end
   
   	--打印备注
   	if (item.remark ~= nil) and  (item.remark:len() > 0) then
      self.printer:goPaperPoints(1)
      local remark = self.localInfo:getRemark(item.remark)
      self.printer:setAlignment(2)
      self.printer:sendText(remark .. string.rep(' ', 5))
   	end
   	self.printer:goPaperPoints(1)
end

-- 菜品加料 打印
function DiffCash:printDishItemOptions(optionList, indent)
   	if #(optionList) == 0 then
      	return
   	end
   
   	for i = 1, #(optionList) do
      	self.printer:goPaperPoints(10)
      	self.printer:initPrinter()
      	local optItem = optionList[i]
      	local count = tostring(optItem.count)
      	local optName = '(' .. optItem.name[self.localInfo.language] .. ')'
      	if count:len() < 2 then
         	count = string.rep(' ', 2-count:len()) .. count
      	end
      	local price = optItem.unitPrice * optItem.count
      	local leftText = indent .. count .. '  ' .. optName
      	local rightText = string.format("%.2f", price)
      	self.printer:justifyText(leftText, rightText, 2)
   	end
end

--[[
	打印end title
]]
function DiffCash:printEndTitle()
	-- body
	-- 分割线
   self.printer:initPrinter()
   self.printer:printLineSpliter()
   self.printer:goPaperPoints(1)
   self.printer:initPrinter()
   self.printer:goPaperPoints(10)

   -- 根据不同的店铺配置不同的打印方式 
   self:Oven_Cash()			
   
   -- 二维码
   self.printer:initPrinter()
   self.printer:print2dCode(tostring(self.odData.orderNo))
   self.printer:initPrinter()
   self.printer:goPaperPoints(10)

   --[[ 结束语
   self.printer:initPrinter()
   self.printer:setAlignment(1)
   
   self.printer:sendText('Printed at ' .. os.date('%Y-%m-%d %H:%M:%S'))
   self.printer:goPaperLines(1)
   self.printer:sendText('Please present this QR code for payment')
   self.printer:goPaperPoints(10)
   self.printer:setFontBold(true)
   self.printer:sendText('Powered by Tough Egg')]]--
end

-- 记得吃 
function DiffCash:JDC_Cash()
	self.printer:initPrinter()
   	self.printer:setFontStyle(1)
   	self.printer:setFontBold(true)
   	l = self.localInfo:translate("Total Price:")
   	r = string.format("%.2f", self.odData.payPrice)
   	self.printer:justifyText(l, r)
   	self.printer:goPaperLines(2)
end

-- oven & Bean
function DiffCash:Oven_Cash( )
	-- body
	self.printer:initPrinter()
   	local l = self.localInfo:translate("Sub Total:")
   	local r = string.format("%.2f", self.odData.basePrice)
   	self.printer:justifyText(l, r)
   	self.printer:goPaperPoints(printH2)

    --l = self.localInfo:translate("Disc:")
    l=string.format("%s%s%s",(self.odData.orderDiscount* 100),"%",self.localInfo:translate(" Disc:"))
    r = string.format("-%.2f", self.odData.discountAmount)

   	self.printer:justifyText(l, r)
   	self.printer:goPaperPoints(printH2)
   
   	l = self.localInfo:translate("10% Srv Charge:")
   	r = string.format("%.2f", self.odData.totalService)
   	self.printer:justifyText(l, r)
   	self.printer:goPaperPoints(printH2)

   	l = self.localInfo:translate("7% GST:")
   	r = string.format("%.2f", self.odData.totalGst)
   	self.printer:justifyText(l, r)
   	self.printer:goPaperPoints(printH2)
   

   	l = self.localInfo:translate("Round:")
   	r = string.format("%.2f", self.odData.round)
   	self.printer:justifyText(l, r)
   	self.printer:setFontBold(true)
   	self.printer:goPaperLines(2)


    self.printer:initPrinter()
    self.printer:setFontStyle(1)
    self.printer:setFontBold(true)
    l = self.localInfo:translate("Total Due:")
    if TE_Tag=="cancel_order" or TE_Tag == "cancel_order_pay"then
      r = string.format("%.2f", 0.00)
    else
      r = string.format("%.2f", self.odData.payPrice)
    end
    self.printer:justifyText(l, r)
    self.printer:goPaperLines(2)

end


--  CYJ 
function DiffCash:CYJ_Cash()
  -- body
    self.printer:initPrinter()
    self.printer:setFontBold(true)
    local l = self.localInfo:translate("Sub Total:")
    local r = string.format("%.2f", self.odData.basePrice)
    self.printer:justifyText(l, r)
    self.printer:goPaperPoints(printH2)

    --l = self.localInfo:translate("Disc:")
    l=string.format("%s%s%s",(self.odData.orderDiscount* 100),"%",self.localInfo:translate(" Disc:"))
    r = string.format("-%.2f", self.odData.discountAmount)

    self.printer:justifyText(l, r)
    self.printer:goPaperPoints(printH2)
   
    l = self.localInfo:translate("10% Srv Charge:")
    r = string.format("%.2f", self.odData.totalService)
    self.printer:justifyText(l, r)
    self.printer:goPaperPoints(printH2)

    -- l = self.localInfo:translate("7% GST:")
    -- r = string.format("%.2f", self.odData.totalGst)
    -- self.printer:justifyText(l, r)
    -- self.printer:goPaperPoints(60)

    l = self.localInfo:translate("Round:")
    r = string.format("%.2f", self.odData.round)
    self.printer:justifyText(l, r)
    self.printer:setFontBold(true)
    self.printer:goPaperPoints(printH1)

    self.printer:initPrinter()
    self.printer:setFontStyle(1)
    self.printer:setFontBold(true)
    l = self.localInfo:translate("Total Price:")
    if TE_Tag=="cancel_order" or TE_Tag == "cancel_order_pay"  then
      r = string.format("%.2f", 0.00)
    else
      r = string.format("%.2f", self.odData.payPrice)
    end
    self.printer:justifyText(l, r)
    self.printer:goPaperLines(1)

end



return DiffCash