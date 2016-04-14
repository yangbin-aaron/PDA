local TECalculate = {}
TECalculate._index = TECalculate


local orderBasePrice = 0   -- 原始价格，所有item的 basePrice (menuPrice（菜品价格） + modifierAmount（加料总和） + priceAdjustment（加价总和）) * count 的总和
local orderDiscountPrice = 0  -- 所有菜品的折后价格 ＝a itemPrice＊count ＋ b itemPrice*count
local orderAddTax = 0                   -- 整单 所有税费 ＝＝ a addTaxAmount * count + b addTaxAmount * countlocal orderDiscountPrice=0         -- 所有菜品的折后价格 ＝a itemPrice＊count ＋ b itemPrice*count
local orderDiscountAmount = 0           -- 所有总的折扣价格 = A discountAmount*count + B discountAmount*count 
local round = 0                 -- round

local serviceAmount = 0 
local gstAmount = 0

function TECalculate:new( orderDetail )
    -- body
    -- local self = {}
    setmetatable(self,TECalculate)
    odDetail = orderDetail
    orderItem = orderDetail.item
    return self
end

function TECalculate:CalculateData()
    -- body
    TECalculate:menuItemData()
    odDetail.basePrice = string.format('%.2f',orderBasePrice)
    odDetail.dishcountPrice = string.format('%.2f',orderDiscountPrice)
    odDetail.discountAmount = string.format('%.2f', orderDiscountAmount)
    odDetail.addTax = string.format('%.2f', orderAddTax)
    odDetail.totalGst = string.format('%.2f',gstAmount)
    odDetail.totalService = string.format('%.2f',serviceAmount)
    -- 需要调整
    odDetail.payPrice = odDetail.dishcountPrice + odDetail.addTax
    odDetail.round = 0 
    return odDetail
end

function TECalculate:menuItemData()
    -- body
    for i=1,#(orderItem) do
        addTaxAmount = 0
        modifierAmount = 0 
        item=orderItem[i]
        -- 判断菜品是否为删除的菜品 如果为删除的菜品则 不需要计算价格
        if(orderItem.isDeleted ~= true) then
            -- 加料总价
            if item.modifier ~= nil then
                for j=1,#(item.modifier) do
                    itemModifier = item.modifier[j]
                    modifierAmount = modifierAmount + itemModifier.unitPrice * itemModifier.count
                end
            end
            
            item.modifierAmount = string.format('%.2f', modifierAmount)
            item.basePrice = string.format('%.2f',item.menuPrice + item.priceAdjustment + item.modifierAmount)

            -- 判断是否允许整单折扣
            if item.allowCustomDiscount == false then
                orderDiscount = 0
            else
                orderDiscount = odDetail.orderDiscount
            end
            -- 判断字符是否为‘’ 如果为‘’ 则赋值 0 
            if orderDiscount == '' then
                orderDiscount = 0 
            end

            item.discountAmount = string.format('%.2f',item.basePrice*(item.itemDiscount+orderDiscount-item.itemDiscount*orderDiscount))
            item.itemPrice = string.format('%.2f', item.basePrice - item.discountAmount)  -- 折后总价
            orderBasePrice = string.format('%.2f', orderBasePrice + item.basePrice * item.count)
            orderDiscountPrice = string.format('%.2f', orderDiscountPrice + item.itemPrice * item.count)
            orderDiscountAmount = string.format('%.2f' , orderDiscountAmount + item.discountAmount * item.count)
            TECalculate:menuItemTaxData(item.itemTax)


        end
    end
end


function TECalculate:menuItemTaxData(itemTaxs)
    -- body
    if itemTaxs == nil then
        return
    end
    -- 包含税 和 附加税 id 集合； 
    local includeTaxIds = {}
    local addTaxIds = {}
    for i=1,#(itemTaxs) do
        --- 由于lua 中没有continue 则需要一步一步去进行 if 判断
        tax = itemTaxs[i]
        --先判断该税率是否可用
        if tax.enable ~= false then
            -- 判断 是否为free 免税
            if tax.free ~= true then
                --判断 effectOrderTypes 是否可用  
                effTlb=TECalculate:split(tax.effectOrderTypes,',')
                -- print('aaa',TECalculate:isInTable(effTlb,odDetail.type))
                    if effTlb ~=nil then
                        print('aaa',TECalculate:isInTable(effTlb,odDetail.type))

                        if TECalculate:isInTable(effTlb,odDetail.type) ==false then
                            if tax.isIncluded == true then
                                table.insert(includeTaxIds,tax.taxId)
                            else
                            table.insert(addTaxIds,tax.taxId)
                        end
                    end
                end
            end
        end     
    end

        --除去包含税的订单项原价,没有包含税的情况下就等于 itemPrice (所有包含税只能算折后)
        priceWithoutTax = item.itemPrice;

        --存储每个包含税的直接税率
        local includeTaxMap = {}
        --所有包含税的综合税率, 没有包含税的情况下就是0
        uniteIncludeRate = 0
        --可用包含税的数量
        includeTaxCount = 0;

        --- 计算 uniteIncludeRate START ---
        --precondition 为空或者 precondition
        --是附加税(包含税的依赖不能是附加税,附加税的依赖可以是包含税)的包含税则算做是依赖关系的最底层
        print('bbb',effTlb)
        if effTlb ~= nil  then
            for i=1,#(itemTaxs) do
                    tax = itemTaxs[i]
                    if tax.isIncluded == true then
                        includeTaxCount = includeTaxCount + 1
                        if tax.precondition == nil or TECalculate:isInTable(addTaxIds,tax.precondition) then
                            temp = tax.rate
                            TECalculate:setTable(includeTaxMap,tax.taxId,temp)
                            uniteIncludeRate = uniteIncludeRate + temp
                        end
                    end
            end
            -- 由最底层往上找出每一个包含税的直接税率
            while (tonumber(includeTaxCount) >  # includeTaxMap )do
                    for i=1,#(itemTax) do
                        tax = itemTaxs[i]
                        if TECalculate:isInTable(includeTaxMap,tax.taxId) == true then
                            -- 继续执行
                        end

                        if tax.isIncluded == true and TECalculate:isInTable(includeTaxIds,tax.precondition) == true then
                            if TECalculate:isInTable(includeTaxMap,tax.taxId) == true then
                                temp1 = includeTaxMap.tax.precondition
                                temp2 = ( temp1 + 1 ) * tax.rate
                                setTable(includeTaxMap,tax.taxId,temp2)
                                uniteIncludeRate = uniteIncludeRate + temp2
                            end
                        end
                    end
            end
        end

        --- 计算 uniteIncludeRate END ---
        --所有包含税的综合税率大于0的时候,算出priceWithoutTax
        if uniteIncludeRate > 0 then
            priceWithoutTax =string.format('%.2f', priceWithoutTax / (uniteIncludeRate + 1 ))
        end

        --打折前的 unDiscountPriceWithoutTax
        unDiscountPriceWithoutTax = priceWithoutTax / (1 - item.itemDiscount)

        local finishedTaxMap = {}
        print('ccc',effTlb)
        if effTlb ~= nil then
            for i=1,#(itemTaxs) do
                    tax = itemTaxs[i]
                    if tax.isIncluded == true then
                        rate = includeTaxMap[tax.taxId]
                        tax.amount = string.format('%.2f',priceWithoutTax * rate)
                        TECalculate:setTable(finishedTaxMap,tax.taxId,tax.amount)
                    elseif tax.precondition == '' then
                        if tax.afterDiscount == true then
                            tax.amount = string.format('%.2f', priceWithoutTax * tax.rate)
                        else
                            tax.amount = string.format('%.2f', unDiscountPriceWithoutTax * tax.rate)
                        end
                        addTaxAmount = addTaxAmount + tax.amount
                        TECalculate:setTable(finishedTaxMap,tax.taxId,tax.amount)
                    end
                    if tax.code == 'service' then
                        serviceAmount = serviceAmount + tax.amount
                    elseif tax.code == 'gst' then
                        gstAmount = gstAmount + tax.amount
                    end
            end

            while #(itemTaxs) > #finishedTaxMap do
                    for i=1,#(itemTaxs) do
                        tax = itemTaxs[i]
                        if TECalculate:isInTable(finishedTaxMap,tax.taxId) then
                            --继续执行
                        end
                        if tax.isIncluded == false and TECalculate:isInTable(finishedTaxMap,tax.precondition) then
                            if tax.afterDiscount == true then
                                tax.amount = string.format('%.2f', (priceWithoutTax + finishedTaxMap:find(tax.precondition)) * tax.rate)
                            else 
                                tax.amount = string.format('%.2f',(unDiscountPriceWithoutTax + finishedTaxMap:find(tax.precondition)) * tax.rate)
                            end
                            addTaxAmount = addTaxAmount + taxId.amount
                            TECalculate:setTable(finishedTaxMap,tax.taxId,tax.amount)
                        end

                        if tax.code == 'service' then
                            serviceAmount = serviceAmount + tax.amount
                        elseif tax.code == 'gst' then
                            gstAmount = gstAmount + tax.amount
                        end
                    end
            end
        end

        item.addTaxAmount =string.format('%.2f', addTaxAmount)
        orderAddTax  = orderAddTax + item.addTaxAmount * item.count

end

 -- table 中是否包含某个数据
function TECalculate:isInTable(tbl,value)
    if tbl == nil then
        return true
    end
    for i=1,#tbl do
        if tonumber(value) ==tonumber(tbl[i]) then
            return true
        end
    end
    return false;
end


 -- 插入键值对 
function TECalculate:setTable(tlb, key, value)
    tlb[key] = value
    table.insert(tlb,key)
end

-- 分隔符 返回 table str ：字符串，delimiter : 分割符
function TECalculate:split(str, delimiter)
    local result = {}
    if str == nil or str =='' or delimiter==nil then
        return nil
    end
    
    for match in (str..delimiter):gmatch("(.-)"..delimiter) do
        table.insert(result, match)
    end
    return result
end


return TECalculate



