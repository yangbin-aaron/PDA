
--[[
	过滤区分dishList 菜单	 及相关print方法
	ALL  所有的菜品
	S 	 过滤itemCode S 开头的菜品
	NS	 过滤itemCode 不为 S 开头的菜品
	
	命名规则，前面一般用店铺首单词
	Oven_ALL    Oven&Fried Chicken  所有菜单
	Oven_A  	Oven&Fried Chicken 	kitchen A （厨房A 打印dishList 过滤）
	Oven_B 		Oven&Fried Chicken 	kitchen B （厨房B 打印dishList 过滤）
	Oven_C 		Oven&Fried Chicken 	kitchen C （厨房C 打印dishList 过滤）
]]

local DiffKitchenList = {}
local  dishCopies			-- 菜品分数


DiffKitchenList.__index = DiffKitchenList

local printName= 'jprint' 
if printName == 'jprint' then
    printH1 = 70
    printH2 = 35
elseif printName == 'xprint' then
    printH1 = 120
    printH2 = 60
end



function DiffKitchenList:new( key, language,orderDetail )
	-- body
	local self={}
	setmetatable(self,DiffKitchenList)
	self.printer=require("PrintUtil"):new(key) 
  self.odData    = orderDetail
  self.language = language
  self.localInfo = require("LocalInfo"):new(language)
  self.amount = #(self.odData.item)
	return self
end



-- 返回 过滤后的dishList
function DiffKitchenList:getValidDishList( mode )
	-- body	
	local dishList=self.odData.item
	local vDishList = {}
  local vIndex = 1
   	if #(dishList) == 0 or dishList == nil then
   		return
   	end

   	if mode == nil then
     	 mode = 'Oven_ALL'
   	end

   	if mode == "S" then
   		for i=1,#(dishList) do
   			if self:isItemCode_S(dishList[i]) == true then
	   			if TE_Tag == "cancel_order" or TE_Tag == "all_print" or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		               vDishList[vIndex] = dishList[i]
		               vIndex = vIndex + 1
	   			elseif dishList[i].setNeedsPrint == true then
		   				vDishList[vIndex] = dishList[i]
		               	vIndex = vIndex + 1
	   			end
   			end
   		end
   		return vDishList
   	end

   	if mode == "NS" then
   		for i = 1, #(dishList) do
   			if self:isItemCode_S(dishList[i]) == false then
	         	if TE_Tag == 'cancel_order' or TE_Tag == 'all_print' or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		               vDishList[vIndex] = dishList[i]
		               vIndex = vIndex + 1
	   			  elseif dishList[i].setNeedsPrint == true then
		   				vDishList[vIndex] = dishList[i]
		               	vIndex = vIndex + 1
	   			  end
       	end
      end
      return  vDishList
   	end

   	if mode == "Oven_ALL" then
   		for i=1,#(dishList) do
   			if self:isItemCode_Oven_ALL(dishList[i]) == true then
	   			if  TE_Tag == "cancel_order" or TE_Tag == "all_print" or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		   				vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			elseif dishList[i].setNeedsPrint == true then
	   					vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			end
   			end
   		end
   		return vDishList
   	end

   if mode == "Oven_A" then
   		for i=1,#(dishList) do
   			if self:isItemCode_Oven_A(dishList[i])==true then
   				if  TE_Tag == "cancel_order" or TE_Tag == "all_print" or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		   				vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			elseif dishList[i].setNeedsPrint == true then
	   					vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			end
   			end
   		end
   		return vDishList
   end


   if mode == "Oven_B" then
   		for i=1,#(dishList) do
   			if self:isItemCode_Oven_B(dishList[i])==true then
   				if  TE_Tag == "cancel_order" or TE_Tag == "all_print" or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		   				vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			elseif dishList[i].setNeedsPrint == true then
	   					vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			end
   			end
   		end
   		return vDishList
   end


   if mode == "Oven_C" then
   		for i=1,#(dishList) do
   			if self:isItemCode_Oven_C(dishList[i])==true then
   				if  TE_Tag == "cancel_order" or TE_Tag == "all_print" or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
		   				vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			elseif dishList[i].setNeedsPrint == true then
	   					vDishList[vIndex] = dishList[i]
	             		vIndex = vIndex + 1
	   			end
   			end
   		end
   		return vDishList
   end

    -- 川羊记 默认模式 cyj default 
   if mode == 'CYJ_Df' then
      for i=1,#(dishList) do
        if self:isItemCode_CYJ(dishList[i]) == true then
          if TE_Tag == 'cancel_order' or TE_Tag == 'all_print' or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
            vDishList[vIndex] = dishList[i]
            vIndex = vIndex + 1
          elseif dishList[i].setNeedsPrint == true then
            vDishList[vIndex] = dishList[i]
            vIndex = vIndex + 1
          end
        end
      end
      return vDishList
   end

   -- 食搁 BAB_JEON
    if mode == 'BAB_JEON' then
      for i=1,#(dishList) do
        if self:isItemCode_BAB_JEON(dishList[i]) == true then
          if TE_Tag == 'cancel_order' or TE_Tag == 'all_print' or TE_Tag == "cancel_order_pay" or TE_Tag == "change_table" then
            vDishList[vIndex] = dishList[i]
            vIndex = vIndex + 1
          elseif dishList[i].setNeedsPrint == true then
            vDishList[vIndex] = dishList[i]
            vIndex = vIndex + 1
          end
        end
      end
      return vDishList
   end

   return vDishList

end


--[[
	打印kitchen单的Top Title
	rid 	： 	店铺id
]]

function DiffKitchenList:printTopTitle() 
 	local tableNO
 	local orderTyp
 	if self.odData.didTableCode ~= '' then   -- 换桌
 		  tableNO = self.localInfo:getTableNO(self.odData.didTableCode..'->'..self.odData.tableCode)
    	orderType = self.localInfo:getOrderType(self.odData.type)
	    self.printer:initPrinter()
	    self.printer:setColorReversal(true)
	    self.printer:setFontStyle(2)
	    self.printer:setFontBold(true)
	    self.printer:justifyText(tableNO, orderType, 4)
	    self.printer:goPaperPoints(printH1)
 	elseif self.odData.type == 2 then	-- 打包
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

   	-- 订单号码  时间
   	local orderNO  = self.localInfo:getOrderNO(self.odData.orderNo)
   	local currTime = os.date('%H:%M:%S')
   	self.printer:initPrinter()
   	self.printer:setFontStyle(1)
   	self.printer:setFontBold(false)
   	self.printer:justifyText(orderNO, currTime, 2)
   	self.printer:goPaperPoints(20)

   	-- 分割线
   	self.printer:initPrinter()
   	self.printer:printLineSpliter()
   	self.printer:goPaperPoints(10)
end

--[[
	打印菜单dish 
	dishList  : 菜单列表
	printWay  	  :	模式 （singlePrint（每道菜品打印） allPrint （菜品全部打印））
]]

function DiffKitchenList:printDishes(dishList, printWay)
 	if dishList == nil then
      return
   	end

   	for i = 1, #(dishList) do
      	if printWay == "singlePrint" then
            if(TE_Tag=='all_print')then
                if(dishList[i].isDeleted == nil or dishList[i].isDeleted == false)then
                    self.printer:initPrinter()
                    self.printer:goPaperLines(2)
                    self:printTopTitle()
                    self:printDishItem(dishList[i])
                    self:printEndTitle()
                    self.printer:goPaperLines(6)
                    self.printer:cut()
                end
             elseif TE_Tag=="cancel_order" or TE_Tag == "cancel_order_pay" then
                if(dishList[i].isDeleted == nil or dishList[i].isDeleted == false)then
                  self.printer:initPrinter()
                  self.printer:goPaperLines(2)
                  self:printTopTitle()
                  self:printDishItem(dishList[i])
                  self:printEndTitle()
                  self.printer:goPaperLines(6)
                  self.printer:cut()
                end
             else
              self.printer:initPrinter()
              self.printer:goPaperLines(2)
              self:printTopTitle()
              self:printDishItem(dishList[i])
              self:printEndTitle()
              self.printer:goPaperLines(6)
              self.printer:cut()
            end
     	  elseif printWay == "allPrint" then
            if(TE_Tag=="all_print")then
              if(dishList[i].isDeleted == nil or dishList[i].isDeleted == false) then
                self:printDishItem(dishList[i])
              end
            else
              self:printDishItem(dishList[i])
            end
     	end
   end

 end

--[[
	打印详细菜谱
	item  : 菜单列表
]]

function DiffKitchenList:printDishItem( item )
	-- body
	if item ==nil then
		return
	end
	dishCopies= item.count
	if TE_Tag == "cancel_order" or TE_Tag == "cancel_order_pay" then
		if item.isDeleted ~= true then
      self:dishInfoPrint(item)
    end
	elseif TE_Tag == "all_print" then
		self:dishInfoPrint(item)
  elseif TE_Tag == "change_table" then
    self:dishInfoPrint(item)
	else
		if item.setNeedsPrint ~= false then
			self:dishInfoPrint(item)
  	end
	end
end





--[[ 打印kitchen 单的EndTitle 
	OperatorName  :  操作人名称
]]

function DiffKitchenList:printEndTitle()
   -- 分割线
   self.printer:initPrinter()
   self.printer:printLineSpliter()
   self.printer:goPaperPoints(10)

   --Print amount
   -- ctxt = self.localInfo:translate("Count:") .. self.amount
   self.printer:initPrinter()
   self.printer:setAlignment(1)
   self.printer:setFontBold(true)
   -- self.printer:sendText(ctxt)
   self.printer:goPaperLines(1)

   --Print operator and date "Date:"
   ltxt = self.localInfo:translate("Operator:") .. self.odData.operatorName

   rtxt = self.localInfo:translate("Date:") .. os.date('%y-%m-%d')
   self.printer:initPrinter()
   self.printer:setAlignment(0) --Align Left
   self.printer:justifyText(ltxt, rtxt, 2)
end




-- item itemCode S 模式
function DiffKitchenList:isItemCode_S(item)
   local itemCode = item.code
   if string.byte(itemCode, 1) == string.byte('S') then
      return true
   else
      return false
   end
end


-- Oven_ALL    Oven&Fried Chicken  所有菜单
function DiffKitchenList:isItemCode_Oven_ALL(item)
    local itemCode = item.code
    if tostring(itemCode)~='VR' then
      return true
    end
   return false
end


-- Oven_A  	Oven&Fried Chicken 	kitchen A （厨房A 打印dishList 过滤）

function DiffKitchenList:isItemCode_Oven_A(item)
   local itemCode = item.code
   tabA=self.localInfo:OvenFried_Oven_A()
   for j=1,#(tabA) do
      if tabA[j]==self:trim(itemCode) then
          return true
      end
   end
   return false
end

-- Oven_B 		Oven&Fried Chicken 	kitchen B （厨房B 打印dishList 过滤）

function DiffKitchenList:isItemCode_Oven_B(item)
   local itemCode = item.code
   tabB=self.localInfo:OvenFried_Oven_B()
   for j=1,#(tabB) do
      if tabB[j]==self:trim(itemCode) then
          return true
      end
   end
   return false
end

-- Oven_C 		Oven&Fried Chicken 	kitchen C （厨房C 打印dishList 过滤）

function DiffKitchenList:isItemCode_Oven_C(item)
   local itemCode = item.code
   tabC=self.localInfo:OvenFried_Oven_C()
   for j=1,#(tabC) do
      if tabC[j]==self:trim(itemCode) then
          return true
      end
   end
   return false
end


-- CYJ  川羊记 
function DiffKitchenList:isItemCode_CYJ( item )
  -- body
  local itemCode = item.code
  tabCYJ=self.localInfo:CYJ_Dish()
  for i=1,#(tabCYJ) do
    if tabCYJ[i] == self:trim(itemCode) then
        return true
    end
  end
  return false
end

-- BAB_JEON  
function DiffKitchenList:isItemCode_BAB_JEON( item )
  -- body
  local itemCode = item.code
  tabBabJeon=self.localInfo:BAB_JEON_Dish()
  for i=1,#(tabBabJeon) do
    if tabBabJeon[i] == self:trim(itemCode) then
        return true
    end
  end
  return false
end



-- 菜单详情打印

function DiffKitchenList:dishInfoPrint(item)
	-- body
	local left = tostring(item.count)
   	if left:len() < 2 then
      left = string.rep(' ', 2-left:len()) .. left
   	end
   	left = left .. ' (' .. tostring(item.code) .. ') '
   	local center = item.name[self.localInfo.language]
   	center = center:gsub("\r", "")
   	center = center:gsub("\n", "")


   	self.printer:initPrinter()
   	self.printer:setFontStyle(2)
   	self.printer:setAlignment(0)
    if TE_Tag == "cancel_order" or TE_Tag == "cancel_order_pay"  then
      self.printer:setColorReversal(true)
    elseif TE_Tag == "all_print" then
      self.printer:setColorReversal(false)
    else 
      if item.isDeleted == true then
          self.printer:setColorReversal(true)
      else
          self.printer:setColorReversal(false)
          left = ' ' .. left
      end
    end

   	self.printer:sendText(left .. center)
   	self:printDishItemOptions(item,item.modifier, string.rep(' ', 8))

   	--打印加价
   	if (item.priceAdjustment ~= nil) and (tonumber(item.priceAdjustment) ~= 0) then
      self.printer:goPaperPoints(printH1)
      local addPrice = self.localInfo:getAddPrice(item.priceAdjustment)
      self.printer:setAlignment(2)
      self.printer:sendText(addPrice .. '    ')
   	end
   
   	--打印备注
   	if (item.remark ~= nil) and  (item.remark:len() > 0) then
      self.printer:goPaperPoints(printH1)
      local remark = self.localInfo:getRemark(item.remark)
      self.printer:setAlignment(2)
      self.printer:sendText(remark .. '    ')
   	end
   	self.printer:goPaperPoints(printH1)
end


-- 加料打印

function DiffKitchenList:printDishItemOptions(item, optionList , indent )
	-- body
	if #(optionList) == 0 then
      return
   	end
   
   	for i = 1, #(optionList) do
      self.printer:goPaperPoints(120)
      self.printer:initPrinter()
      self.printer:setFontStyle(1)
      if TE_Tag == "cancel_order" or TE_Tag == "cancel_order_pay"  then
        self.printer:setColorReversal(true)
      elseif TE_Tag == "all_print" then
        self.printer:setColorReversal(false)
      else 
        if item.isDeleted == true then
            self.printer:setColorReversal(true)
        else
            self.printer:setColorReversal(false)
        end
      end
      local optItem = optionList[i]
      local leftText = indent .. optItem.name[self.localInfo.language]
      local rightText = ''
      if optItem.default_price ~= 0 then
        rightText = 'x' .. tostring(optItem.count*dishCopies) .. '  '
      end
      self.printer:justifyText(leftText, rightText, 2)
   	end
   -- self.printer:sendText()
end

-- clean blank 
function DiffKitchenList:trim (s) 
  return (string.gsub(s, "^%s*(.-)%s*$", "%1")) 
end

return DiffKitchenList