local TEResolveStr = {}
TEResolveStr._index = TEResolveStr

function TEResolveStr:new( orderDetail )
	-- body
	-- local self = {}
	setmetatable(self,TEResolveStr)
	odDetail = orderDetail
	return self
end

function TEResolveStr:resolver( )
	-- body
	if TE_Tag == 'print_report' then
    	return odDetail
	end

	if odDetail.orderDiscount == '' then
		odDetail.orderDiscount = 0
	end

	if odDetail.totalGst == '' then
		odDetail.totalGst = 0 
	end

	if odDetail.takeAwayNo == '' then
		odDetail.takeAwayNo = 0
	end

	if odDetail.payment.amount == '' then
		odDetail.payment.amount = 0
	end

	if odDetail.payment.change =='' then
		odDetail.payment.change = 0
	end

	if odDetail.payment.received =='' then
		odDetail.payment.received = 0
	end

	if odDetail.discountPrice == '' then
		odDetail.discountPrice = 0
	end

	if odDetail.addTax == '' then
		odDetail.addTax = 0 
	end

	if odDetail.totalService == '' then
		odDetail.totalService = 0
	end

	if odDetail.payPrice == '' then
		odDetail.payPrice = 0 
	end

	if odDetail.basePrice == '' then
		odDetail.basePrice = 0
	end

	if odDetail.discountAmount == '' then
		odDetail.discountAmount = 0
	end

	-- 循环处理 Item
	for i=1,#(odDetail.item) do
		item= odDetail.item[i]

		if item.basePrice == '' then
			item.basePrice = 0 
		end

		if item.addTaxAmount == '' then
			item.addTaxAmount = 0 
		end

 		-- 循环处理 itemTax
		for i=1,#(item.itemTax) do
			itemTax = item.itemTax[i]
			if itemTax.rate == '' then
				itemTax.rate = 0 
			end

			if itemTax.amount == '' then
				itemTax.amount = 0
			end
 		end

 		if item.discountAmount == '' then
 			item.discountAmount = 0 
 		end

 		if item.priceAdjustment == '' then
 			item.priceAdjustment = 0
 		end

 		if item.orderDiscount == '' then
 			item.orderDiscount = 0 
 		end

 		if item.modifierAmount == '' then
 			item.modifierAmount = 0
 		end

 		if item.itemPrice == '' then
 			item.itemPrice =0
 		end

 		if item.itemDiscount == '' then
 			item.itemDiscount = 0
 		end

 		if item.menuPrice == '' then
 			item.menuPrice = 0
 		end
	end
  
  	return odDetail
end

return TEResolveStr