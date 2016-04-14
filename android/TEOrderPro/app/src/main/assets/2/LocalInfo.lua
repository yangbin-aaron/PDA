-- The Module name

local LocalInfo = {  }

LocalInfo.__index = LocalInfo

function LocalInfo:new(language)
   local self = {}
   setmetatable(self, LocalInfo)
   self.title = ""
   self.subTitle = ""
   self:setLanCode(language)
   self:getResRestaurantInfo()
   return self
end

function LocalInfo:setLanCode(language)
   self.language = language
   self.lanCode = 1
   if language == 'en_US' then
      self.lanCode = 1
   elseif language == 'zh_CN' then
      self.lanCode = 2
   else
      self.lanCode = 1      
   end
end

function LocalInfo:getResRestaurantInfo()
   local restaurant = self:returnRestaurantInfo()
   if restaurant == nil then
      self.title = nil
      self.subTitle = nil
      return 
   end
   self.title = restaurant[self.lanCode].name
   self.subTitle = restaurant[self.lanCode].information
end

function LocalInfo:returnRestaurantInfo()
    -- body
    local RestaurantList = {
        {
         name='OVen & Fried Chicken\n(Bukit Timah)',
         information = "OKKUDAK PTE LTD\n16 Chun Tin Rood S 599603      Tel: 64633505\nGST Reg No.: 201413381C"
        },
        {
         name='OVen & Fried Chicken\n(Bukit Timah)',
         information = "OKKUDAK PTE LTD\n16 Chun Tin Rood S 599603      Tel: 64633505\nGST Reg No.: 201413381C"
        }
    }
    return RestaurantList
end

function LocalInfo:returnLocalTable()
    -- body
    local LocalTable = {
        ["Total Price:"] = {"Total Price:", "应收: "},
        ["Sub Total:"]   = {"Sub Total:", "菜单总价: "},
        ["Disc:"]        = {"Disc:", "Disc:"},
        ["7% Disc:"]     = {"7% Disc:", "7% Disc:"}, 
        ["7% GST(Inc):"] = {"7% GST(Inc):", "7% GST(Inc)"}, 
        ["Round:"]       = {"Round:", "Round:"}, 
        ["Amount:"]  = {"Amount: ", "总数: "},
        ["Count:"]   = {"Count: ", "总数: "}, 
        ["Operator:"]   = {"Operator: ", "收银员: "}, 
        ["Date:"]    = {"Date: ", "日期:"}, 
        ["Remark:"]     = {"Remark:", "  备注: "}, 
        ["Cash Tendered:"] = {"Cash Tendered:", "实收: "}, 
        ["Change:"]        = {"Change:", "找零: "},
    }
    return LocalTable
end

function LocalInfo:getInvoice(invoiceNO)
   if invoiceNO == nil then
      invoiceNO = 'N/A'
   end
   local begTr = {"INVOICE : ", "收据编号: "}
   return begTr[self.lanCode] .. invoiceNO
end

function LocalInfo:getTableNO(tableNO)
   local begTr = {"Table:", "桌号:"}
   return begTr[self.lanCode] .. tableNO
end

function LocalInfo:getOrderType(odType)
   local orderType= {
      {"Dine-in","Take-away","Reserve"},
      {"堂食","打包","预订"},
   }
   return orderType[self.lanCode][odType]
end

function LocalInfo:getKitchenDetailTitle()
   local titles = {
      " Qty      Item                              ",
      " 数量     菜名                              ",
   }
   return titles[self.lanCode]
end

function LocalInfo:getDefaultDetailTitle()
   local titles = {
      " Qty      Item                          Price($)",
      " 数量     菜名                           价格($)",
   }
   return titles[self.lanCode]
end

function LocalInfo:getRemark(remark)
   local remarkTitle = {
      "Remark: ",
      "备注: ",
   }
   return remarkTitle[self.lanCode] .. tostring(remark)
end

function LocalInfo:getAddPrice(price)
   local addPrices = {
      "Add Price: ",
      "加价: ",
   }
   return addPrices[self.lanCode] .. "$" .. tostring(price)
end

function LocalInfo:getOrderNO(odNO)
   if odNO == nil then
      odNO = 'N/A'
   end
   local orderNOs = {
      "Order No: ",
      "订单编号: ",
   }
   odNO = string.gsub(odNO, ' ', '')
   return orderNOs[self.lanCode] .. odNO
end

function LocalInfo:getPax(pax)
   local begTr = {"",     "人数:"}
   local endTr = {" Pax", "人"}
   return begTr[self.lanCode] .. tostring(pax) .. endTr[self.lanCode]
end

function LocalInfo:getReceiptType(receiptType)
   local titles = {
      ['receipt'] = {" Receipt ", "收据"},
      ['receipt_rep'] = {" Receipt Reprint ", "收据重印"},
      ['cash'] = {"Cashier's Order", "收银单"},
      ['kitchen'] = {"Kitchen", "厨房单"},
      ['summary_closing']={"Summary Closing Report","报表单"},
      ['cancel'] = {" Cancel ", "取消单"},
      ['refund'] = {" Refund ", "退款单"}
   }
   
   local title = titles[receiptType]
   if title ~= nil then
      title = title[self.lanCode]
   end   
   return title
end

function LocalInfo:translate(str)
   local localTable = self:returnLocalTable()
   local tranStr = localTable[str];
   if tranStr ~= nil then
      tranStr = tranStr[self.lanCode]
   end
   return str;
end


---------------------print summary item -----------------------------

function LocalInfo:firstSummaryNameTable(orderDetail)
  local firstTable={}
    firstTable={
      "Cash",'AMEX','NETS','VISA','MASTER','JCB','YamiCoins','UnionPay','DinerClub','Voucher'
    }
  return firstTable
end

function LocalInfo:firstSummaryCountTable(orderDetail)
  local firstCountTable={}
  firstCountTable[1] = orderDetail.cash_count
  firstCountTable[2] = orderDetail.amex_count
  firstCountTable[3] = orderDetail.nets_count
  firstCountTable[4] = orderDetail.visa_count
  firstCountTable[5] = orderDetail.master_count
  firstCountTable[6] = orderDetail.jcb_count
  firstCountTable[7] = orderDetail.yami_coin_count
  firstCountTable[8] = orderDetail.unionpay_count
  firstCountTable[9] = orderDetail.diner_club_count
  firstCountTable[10] = orderDetail.voucher_count

  return firstCountTable
end

function LocalInfo:firstSummaryDataTable(orderDetail)
  local firstDataTable = {}
  firstDataTable[1]=orderDetail.cash
  firstDataTable[2]=orderDetail.amex
  firstDataTable[3]=orderDetail.nets
  firstDataTable[4]=orderDetail.visa
  firstDataTable[5]=orderDetail.master
  firstDataTable[6]=orderDetail.jcb
  firstDataTable[7]=orderDetail.yami_coin
  firstDataTable[8]=orderDetail.unionpay
  firstDataTable[9]=orderDetail.diner_club
  firstDataTable[10]=orderDetail.voucher
    
  return firstDataTable
end

function LocalInfo:secondSummaryNameTable(orderDetail)
  local secondTable={}

    secondTable={
      '**Sales AMT**','Cash in Drawer','Cash Difference','Total Dish Count','Total No.of Pax'
    }
  
  return secondTable
end

function LocalInfo:secondSummaryDataTable(orderDetail)
  local secondDataTable={
    }
 
    secondDataTable[1]=orderDetail.sales_amt
    secondDataTable[2]=orderDetail.cash_in_drawer
    secondDataTable[3]=orderDetail.cash_difference
    secondDataTable[4]=orderDetail.total_dish_count
    secondDataTable[5]=orderDetail.total_no_of_pax
  

  return secondDataTable
end

function LocalInfo:thirdSummaryNameTable(orderDetail)
  local thirdNameTable={}
  
    thirdNameTable={
      '**Total AMT**' , 'Net_Sales','Svc Chrg','Taxable AMT','GST'
    }
 

  return thirdNameTable
end

function LocalInfo:thirdSummaryDataTable(orderDetail)
  local thirdDataTable={
    }

     thirdDataTable[1]=orderDetail.total_amt
     thirdDataTable[2]=orderDetail.net_sales
     thirdDataTable[3]=orderDetail.svc_chrg
     thirdDataTable[4]=orderDetail.taxable_amt
     thirdDataTable[5]=orderDetail.gst

  return thirdDataTable
end

function LocalInfo:fourthSummaryNameTable(orderDetail)
  -- body
  local fourthNameTable={}

    fourthNameTable={
      '**Gross AMT**' , 'Discount','Rounding','**Net AMT**'
    }
  

  return fourthNameTable
end


function LocalInfo:fourthSummaryDataTable(orderDetail)
  local fourthDataTable={
    }

     fourthDataTable[1]=orderDetail.gross_amt
     fourthDataTable[2]=orderDetail.discount
     fourthDataTable[3]=orderDetail.rounding
     fourthDataTable[4]=orderDetail.net_amt
 
  return fourthDataTable
end

function LocalInfo:fifthSummaryNameTable(orderDetail)
  -- body
  local fifthNameTable={}

    fifthNameTable={
      'Yami Top up'
    }
  
  return fifthNameTable
end


function LocalInfo:fifthSummaryDataTable(orderDetail)
  local fifthDataTable={
    }

     fifthDataTable[1]=orderDetail.yami_top_up
  
  return fifthDataTable
end


function LocalInfo:sixthSummaryNameTable(orderDetail)
  -- body
  local sixthNameTable={}
 
    sixthNameTable={
      'Void Count','**Void Amt**','Refund Count','**Refund AMT**','Tips','Cancel Count','**Cancel AMT**'
    }
  
  return sixthNameTable
end


function LocalInfo:sixthSummaryDataTable(orderDetail)
  local sixthDataTable={
    }
  
     sixthDataTable[1]=orderDetail.void_count
     sixthDataTable[2]=orderDetail.void_amt
     sixthDataTable[3]=orderDetail.refund_count
     sixthDataTable[4]=orderDetail.refund_amt
     sixthDataTable[5]=orderDetail.tips
     sixthDataTable[6]=orderDetail.cancel_count
     sixthDataTable[7]=orderDetail.cancel_amt

  return sixthDataTable
end


function LocalInfo:seventhSummaryNameTable(orderDetail)
  -- body
  local seventhNameTable={}

    seventhNameTable={
      'Receipt','Start No','End No','Count'
    }

  return seventhNameTable
end


function LocalInfo:seventhSummaryDataTable(orderDetail)
  local seventhDataTable={
    }
 
     seventhDataTable[1]=orderDetail.receipt
     seventhDataTable[2]=orderDetail.start_no
     seventhDataTable[3]=orderDetail.end_no
     seventhDataTable[4]=orderDetail.count
  
  return seventhDataTable
end


function LocalInfo:eighthSummaryNameTable(orderDetail)
  -- body
  local eighthNameTable={}
 
    eighthNameTable={
      'Turnover'
    }
  return eighthNameTable
end


function LocalInfo:eighthSummaryDataTable(orderDetail)
  local eighthDataTable={
    }
     eighthDataTable[1]=orderDetail.turnover
  return eighthDataTable
end




function LocalInfo:payType()
local payTypeStr={
  'Cash:','AMEX:','Nets:','Visa:','Master:','JCB:','YamiCoins:','UnionPay:','DinesClub'
}
return payTypeStr
end


function LocalInfo:OvenFried_Oven_A()
local ovenFriedKitchenA={
  'RC','CC','FC','SC','LS','SM1','AM1'
}
return ovenFriedKitchenA
end

function LocalInfo:OvenFried_Oven_B()
local ovenFriedKitchenB={
  'SM','AM'
  --,'OM'
}
return ovenFriedKitchenB
end

function LocalInfo:OvenFried_Oven_C()
local ovenFriedKitchenC={
  'DB','BB','SO','SD'
}
return ovenFriedKitchenC
end




return LocalInfo






