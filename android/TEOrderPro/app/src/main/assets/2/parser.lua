-- #!/usr/bin/env lua

-- Init the environment

package.path = string.format("%s/?.lua;%s", TE_PWD, package.path)
print('\nPWD-------->>>>: \n' .. TE_PWD .. '\n')
print('\n TE_OrderDetail-->> \n '.. TE_OrderDetail..'\n')

--[[
	mode 取值说明： 

	过滤区分dishList 菜单	
	ALL  所有的菜品
	S 	 过滤itemCode S 开头的菜品
	NS	 过滤itemCode 不为 S 开头的菜品
	
	命名规则，前面一般用店铺首单词
	Kitchen_ALL     所有菜单
	Kitchen_A （厨房A 打印dishList 过滤）
	Kitchen_B （厨房B 打印dishList 过滤）
	Kitchen_C （厨房C 打印dishList 过滤）

	TE_PrinterKey 说明：
	kitchen 	 : kitchen 	对应的打印机
	cash 		 ： cash 	对应的打印机
	bar			 ： bar  	对应的打印机
	receipt		 :  receipt 对应的打印机

	TE_Tag 说明：
	Open_box	 ： 打开钱箱操作
	change_table ： 换桌操作
	print_order	 ： 打印订单操作
	payment		 ： 付款操作
	reprint	 	 ： 重印操作
	cancel_order ： 取消订单操作
	all_print	 ： 打印所有操作
	print_report ： 打印报表操作
]]
-- local TE_Tag			-- 响应动作 		（打印什么单子） 
-- local TE_PrinterKey		-- 对应的打印key （对应的打印机） 


-- 读取本地txt 文件内容方法  （本地测试用）
-- function readFiles( fileName )
-- 	local f = assert(io.open(fileName,'r'))
-- 	local content = f:read("*all")
-- 	f:close()
-- 	return content
-- end 


-- TE_OrderDetail=readFiles(TE_PWD..'/reportTest.txt')

-- 语言设置：英文 en_US  中文zh_CN


local JSON = require("JSON")

local orderDetail = JSON:decode(TE_OrderDetail)
local TEResolveStr = require("TEResolveStr"):new(orderDetail)
print('Lua_version: 3.1.0.0\n')
local localInfo = require("LocalInfo"):new('en_US')
print('Restaurant_name: '..localInfo.title..'\n')

-- open Cash Box 
if(TE_Tag=="Open_box") then
	PrintUtil=require("PrintUtil"):new(TE_PrinterKey) 
    PrintUtil:openCashbox()
end

-- Oven&Fried Chicken    炸鸡店
local printIndex = require("PrintIndex"):new(TE_PrinterKey, TEResolveStr:resolver())
if printIndex ~= nil then
	if TE_Tag=="change_table" then
		if TE_PrinterKey == 'kitchen' then
		    printIndex:KitchenDoPrint('en_US','Oven_A','singlePrint')
		end

		if TE_PrinterKey == 'bar' then
		    printIndex:KitchenDoPrint('en_US','Oven_B','singlePrint')
		end

		if TE_PrinterKey == 'cash' then
		    printIndex:KitchenDoPrint('en_US','Oven_C','allPrint')
		end

		if TE_PrinterKey == 'receipt' then
		    printIndex:KitchenDoPrint('en_US','Oven_ALL','allPrint')
		    printIndex:CashDoPrint('en_US')
		end
	end

	if TE_Tag=="print_order"  then
		if orderDetail.setNeedsPrint == true then 
			if TE_PrinterKey == 'kitchen' then
		    	printIndex:KitchenDoPrint('en_US','Oven_A','singlePrint')
			end

			if TE_PrinterKey == 'bar' then
		    	printIndex:KitchenDoPrint('en_US','Oven_B','singlePrint')
			end

			if TE_PrinterKey == 'cash' then
		    	printIndex:KitchenDoPrint('en_US','Oven_C','allPrint')
			end

			if TE_PrinterKey == 'receipt' then
		    	printIndex:KitchenDoPrint('en_US','Oven_ALL','allPrint')
			end
		else
			if TE_PrinterKey == 'receipt' then
		    	printIndex:CashDoPrint('en_US')
			end
		end
	end

	if TE_Tag=='payment'  then
		if TE_PrinterKey == 'receipt' then
	      	local isReprint = false
      		printIndex:ReceiptDoPrint('en_US', isReprint, 1)
      		printIndex:ReceiptDoPrint('en_US', isReprint, 2)
      	end
	end

	if TE_Tag == "reprint"  then
		if TE_PrinterKey == 'receipt' then
	      	local isReprint = true
	      	printIndex:ReceiptDoPrint('en_US', isReprint, 2)
      	end
	end


	if TE_Tag == "print_report" then
		if TE_PrinterKey == 'receipt' then
			printIndex:ReportDoPrint('en_US')
      	end
	end

	if TE_Tag=="cancel_order" or TE_Tag=="all_print" or TE_Tag == "cancel_order_pay" then
		if TE_PrinterKey == 'kitchen' then
			printIndex:KitchenDoPrint('en_US','Oven_A','singlePrint')
		end

		if TE_PrinterKey == 'bar' then
			printIndex:KitchenDoPrint('en_US','Oven_B','singlePrint')
		end

		if TE_PrinterKey == 'cash' then
			printIndex:KitchenDoPrint('en_US','Oven_C','allPrint')
		end

		if TE_PrinterKey == 'receipt' then
			printIndex:KitchenDoPrint('en_US','Oven_ALL','allPrint')
		end	

		if(tonumber(orderDetail.payment.status) == 1)then
          	--receipt
           	if TE_PrinterKey == 'receipt' then
		      	local isReprint = true
      			printIndex:ReceiptDoPrint('en_US', isReprint, 1)
      		end
        else
           	if TE_PrinterKey == 'receipt' then
			 	printIndex:CashDoPrint('en_US')
			end
       end

	end
 end





























