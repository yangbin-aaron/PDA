local PrintIndex = {}

PrintIndex.__index = PrintIndex

function PrintIndex:new(key, orderDetail)
  	-- body
  	local self = {}
  	setmetatable(self, PrintIndex)
  	self.printer   = require("PrintUtil"):new(key) 
    self.odData    = orderDetail
    self.key 	     = key
    return self
end

--[[
	mode		  : 过滤dishList 方式
	printWay  	  :	模式 （singlePrint（每道菜品打印） allPrint （菜品全部打印））
]]
function PrintIndex:KitchenDoPrint(language, mode, printWay)
  	local diffKitchenList = require("DiffKitchenList"):new(self.key, language, self.odData)
  	-- body
  	local dishList = diffKitchenList:getValidDishList(mode)
   	if dishList == nil or #(dishList) == 0 then
      	return 
   	end
    self.printer:setBelMySelf()
    if printWay == "singlePrint" then
      	diffKitchenList:printDishes(dishList,printWay)
    elseif printWay == "allPrint"  then
       	diffKitchenList:printTopTitle()
       	diffKitchenList:printDishes(dishList,printWay)
      	diffKitchenList:printEndTitle()
  	  	self.printer:goPaperLines(6)
       	self.printer:cut()
    end
end

function PrintIndex:CashDoPrint(language)
  	-- body
   	local diffCash = require("DiffCash"):new(self.key, language, self.odData)
  	self.printer:setBelMySelf()
  	diffCash:printTopTitle()
  	diffCash:printDishes(self.odData.item)
  	diffCash:printEndTitle()
  	self.printer:goPaperLines(3)
   	self.printer:cut()
end

function PrintIndex:ReceiptDoPrint(language, isReprint, times)
  	-- body
   	diffReceipt = require("DiffReceipt"):new(self.key, language, self.odData)
  	if isReprint == nil then
        self.isReprint = false
   	end
   	self.isReprint = isReprint
    if self.isReprint == false and times == 1 and self.odData.payment.paymentMethod == 1 then
        self.printer:initPrinter()
        self.printer:openCashbox()
    end
    self.printer:setBelMySelf()
  	diffReceipt:printTopTitle( self.isReprint )
  	diffReceipt:printDishes(self.odData.item)
  	diffReceipt:printEndTitle()
  	self.printer:goPaperLines(3)
    self.printer:cut()
end

function PrintIndex:ReportDoPrint(language)
  	-- body
   	diffReport = require("DiffReport"):new(self.key, language, self.odData)
  	self.printer:setBelMySelf()
  	diffReport:printTitle()
  	diffReport:printDishes()
  	self.printer:goPaperLines(3)
   	self.printer:cut()
end

return PrintIndex