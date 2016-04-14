
local DiffReport={}

DiffReport.__index=DiffReport

local printName= 'jprint' 
if printName == 'jprint' then
    printH1 = 70
    printH2 = 35
elseif printName == 'xprint' then
    printH1 = 120
    printH2 = 60
end

function DiffReport:new( key,language,orderDetail )
  -- body
  local self={}
  setmetatable(self,DiffReport)
  self.printer=require("PrintUtil"):new(key) 
    self.odData    = orderDetail
    self.goodsResources=self.odData.goodsResources
    self.localInfo = require("LocalInfo"):new(language)
    self.amount = #(self.goodsResources)
  return self
end


-- print title
function DiffReport:printTitle()
   --restaurant name
   self.printer:initPrinter()
   self.printer:setFontStyle(1)
   self.printer:setFontBold(true)
   self.printer:setAlignment(1)
   self.printer:sendText(self.localInfo.title)
   self.printer:goPaperLines(2)

   -- split line
   self.printer:initPrinter()
   self.printer:printLineSpliter()
   self.printer:goPaperPoints(1)
   self.printer:initPrinter()
   self.printer:goPaperLines(1)

   -- restaurant info
   self.printer:initPrinter()
   self.printer:setAlignment(1)
   self.printer:sendText(self.localInfo.subTitle)
   self.printer:goPaperLines(2)

   -- split line
   self.printer:initPrinter()
   self.printer:printLineSpliter()
   self.printer:goPaperPoints(1)
   self.printer:initPrinter()
   self.printer:goPaperLines(2)

   -- type name
   local typeName="summary_closing"
   typeName = self.localInfo:getReceiptType(typeName)
   self.printer:initPrinter()
   self.printer:setAlignment(1)
   self.printer:setFontStyle(2)
   self.printer:setFontBold(true)
   self.printer:sendText(typeName)
   self.printer:goPaperLines(3)
   typeName = nil

   --from time to time
   local fromTimeToTime='From '..self.odData.startDatetime..' to '..self.odData.endDatetime
   self.printer:initPrinter()
   self.printer:setAlignment(1)
   self.printer:sendText(fromTimeToTime)
   self.printer:goPaperLines(1)

   self:printTotalInfo()


end

-- print total info
function DiffReport:printTotalInfo()
   -- split line
   self.printer:initPrinter()
   self.printer:printLineSpliter()
   self.printer:goPaperPoints(1)
   self.printer:initPrinter()
   self.printer:setBlackMark()

   self:Default_Report_Top()

end

function DiffReport:printInfoItem(v,m)
   self.printer:initPrinter()
   self.printer:summaryItemText(v,m,4)
   self.printer:goPaperPoints(2)
end

function DiffReport:printInfoAndCountItem(v,c,m)
   self.printer:initPrinter()
   self.printer:summaryItemTextCenter(v,c,m,4)
   self.printer:goPaperPoints(2)
end


function DiffReport:printDishes()
  -- body
  if self.amount==nil then
    return
  end

  self:Oven_Report_Dish()

end



function DiffReport:Default_Report_Dish( )
  -- body
    --show dishMenu
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
            if( dishItemType~='Menu') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='Menu'
            end
        self.printer:initPrinter()
        self.printer:setAlignment(0)
        self.printer:summaryDishText(dishItem.Quantity, "X", dishItem.MenuTitle,{left = 8, right = 0}, 2)
        self.printer:goPaperPoints(10)

        elseif dishItem.Type=='AddOption' then
            if( dishItemType~='AddOption') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='AddOption'
            end
        self.printer:initPrinter()
        self.printer:setAlignment(0)
        self.printer:summaryDishText(dishItem.Quantity, "X", dishItem.MenuTitle,{left = 4, right = 0}, 2)
        self.printer:goPaperPoints(10)
        elseif dishItem.Type=='AddPrice' then
            if( dishItemType~='AddPrice') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='AddPrice'
            end
        self.printer:initPrinter()
        self.printer:setAlignment(0)
        self.printer:summaryTwoText('$'..string.format("%.2f", dishItem.TotalPrice * dishItem.Quantity),dishItem.MenuTitle,4)
        self.printer:goPaperPoints(10)
        end
    end

    -- split line
    self.printer:initPrinter()
    self.printer:printLineSpliter()
    self.printer:goPaperPoints(1)
    self.printer:initPrinter()
    self.printer:setBlackMark()

    -- end line
    self.printer:initPrinter()
    self.printer:setAlignment(1)
    self.printer:sendText('Printed at '..self.odData.currentDatetime)
    self.printer:goPaperLines(4)
end


function DiffReport:Default_Report_Top( )
  -- body
  -- total info print
   self.printer:goPaperPoints(10)
   firstSummaryName=self.localInfo:firstSummaryNameTable(self.odData)
   firstSummaryCount=self.localInfo:firstSummaryCountTable(self.odData)
   firstSummaryData=self.localInfo:firstSummaryDataTable(self.odData)
   for i,v in ipairs(firstSummaryName) do
      self:printInfoAndCountItem(v,firstSummaryCount[i],firstSummaryData[i])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

   -------------second summary-------------------
   secondSummaryName=self.localInfo:secondSummaryNameTable(self.odData)
   secondSummaryData=self.localInfo:secondSummaryDataTable(self.odData)
   for a,b in ipairs(secondSummaryName) do
      self:printInfoItem(b,secondSummaryData[a])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

   -------------------third summary------------------------------
   thirdSummaryName=self.localInfo:thirdSummaryNameTable(self.odData)
   thirdSummaryData=self.localInfo:thirdSummaryDataTable(self.odData)
   for c, d in ipairs(thirdSummaryName) do
      self:printInfoItem(d,thirdSummaryData[c])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

    -------------------fourth summary------------------------------
   fourthSummaryName=self.localInfo:fourthSummaryNameTable(self.odData)
   fourthSummaryData=self.localInfo:fourthSummaryDataTable(self.odData)
   for e, f in ipairs(fourthSummaryName) do
      self:printInfoItem(f,fourthSummaryData[e])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

 -------------------fifth summary------------------------------
   fifthSummaryName=self.localInfo:fifthSummaryNameTable(self.odData)
   fifthSummaryData=self.localInfo:fifthSummaryDataTable(self.odData)
   for g, h in ipairs(fifthSummaryName) do
      self:printInfoItem(h,fifthSummaryData[g])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

    -------------------sixth summary------------------------------
   sixthSummaryName=self.localInfo:sixthSummaryNameTable(self.odData)
   sixthSummaryData=self.localInfo:sixthSummaryDataTable(self.odData)
   for i, j in ipairs(sixthSummaryName) do
      self:printInfoItem(j,sixthSummaryData[i])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

    -------------------seventh summary------------------------------
   seventhSummaryName=self.localInfo:seventhSummaryNameTable(self.odData)
   seventhSummaryData=self.localInfo:seventhSummaryDataTable(self.odData)
   for k, l in ipairs(seventhSummaryName) do
      self:printInfoItem(l,seventhSummaryData[k])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)

  -------------------eighth summary------------------------------
   eighthSummaryName=self.localInfo:eighthSummaryNameTable(self.odData)
   eighthSummaryData=self.localInfo:eighthSummaryDataTable(self.odData)
   for m, n in ipairs(eighthSummaryName) do
      self:printInfoItem(n,eighthSummaryData[m])
      self.printer:goPaperPoints(10)
   end
   self.printer:goPaperLines(1)


end





function DiffReport:Oven_Report_Dish(  )
  -- body

   --show dishMenu  RC  CC
   local dishTotalPrice=0
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
          if(dishItem.Code=="RC" or dishItem.Code=="CC") then
            if( dishItemType~='RC') then
              -- split line
              self.printer:initPrinter()
              self.printer:printLineSpliter()
              self.printer:goPaperPoints(1)
              self.printer:initPrinter()
              self.printer:setBlackMark()
              dishItemType='RC'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="RC" or dishCode=="CC")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
    
     --show dishMenu  FC  SC
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then            
          if(dishItem.Code=="FC" or dishItem.Code=="SC") then
              if( dishItemType~='FC') then
                -- split line
                self.printer:initPrinter()
                self.printer:printLineSpliter()
                self.printer:goPaperPoints(1)
                self.printer:initPrinter()
                self.printer:setBlackMark()
                dishItemType='FC'
              end
              self.printer:initPrinter()
              self.printer:setAlignment(0)
              dishCode=dishItem.Code
              self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
              self.printer:goPaperPoints(20)
              dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    
    if(dishCode=="FC" or dishCode=="SC")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
        -- self.printer:initPrinter()
        -- self.printer:printLineSpliter()
        -- self.printer:goPaperPoints(1)
        -- self.printer:initPrinter()
        -- self.printer:setBlackMark()
    end
    
    
      --show dishMenu  SM  AM
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
          if(dishItem.Code=="SM" or dishItem.Code=="SM1" or dishItem.Code=="AM" or dishItem.Code=="AM1") then
            if( dishItemType~='SM') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='SM'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="SM" or dishCode=="SM1" or dishCode=="AM" or dishCode=="AM1")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
      --show dishMenu  SD
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
            if(dishItem.Code=="SD") then
              if( dishItemType~='SD') then
                -- split line
                self.printer:initPrinter()
                self.printer:printLineSpliter()
                self.printer:goPaperPoints(1)
                self.printer:initPrinter()
                self.printer:setBlackMark()
                dishItemType='SD'
              end
              self.printer:initPrinter()
              self.printer:setAlignment(0)
              dishCode=dishItem.Code
              self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
              self.printer:goPaperPoints(20)
              dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
            end
      end
    end
    if(dishCode=="SD")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
      --show dishMenu  DB  BB SO
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then 
          if(dishItem.Code=="DB" or dishItem.Code=="BB" or dishItem.Code=="SO") then
            if( dishItemType~='DB') then
              -- split line
              self.printer:initPrinter()
              self.printer:printLineSpliter()
              self.printer:goPaperPoints(1)
              self.printer:initPrinter()
              self.printer:setBlackMark()
              dishItemType='DB'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="DB" or dishCode=="BB" or dishCode=="SO")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
    
    
      --show dishMenu  LS
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
          if(dishItem.Code=="LS") then
            if( dishItemType~='LS') then
              -- split line
              self.printer:initPrinter()
              self.printer:printLineSpliter()
              self.printer:goPaperPoints(1)
              self.printer:initPrinter()
              self.printer:setBlackMark()
              dishItemType='LS'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="LS")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
       --show dishMenu  OM
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
          if(dishItem.Code=="OM") then
            if( dishItemType~='OM') then
              -- split line
              self.printer:initPrinter()
              self.printer:printLineSpliter()
              self.printer:goPaperPoints(1)
              self.printer:initPrinter()
              self.printer:setBlackMark()
              dishItemType='OM'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="OM")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end
    
    
        --show dishMenu  VR
    for i =1 ,#(self.goodsResources) do
        local dishItem=self.goodsResources[i]
        if dishItem.Type=='Menu' then
          if(dishItem.Code=="VR") then
            if( dishItemType~='VR') then
              -- split line
              self.printer:initPrinter()
              self.printer:printLineSpliter()
              self.printer:goPaperPoints(1)
              self.printer:initPrinter()
              self.printer:setBlackMark()
              dishItemType='VR'
            end
            self.printer:initPrinter()
            self.printer:setAlignment(0)
            dishCode=dishItem.Code
            self.printer:summaryDishTextOven(dishItem.Quantity, "X  "..dishItem.Code.."  "..dishItem.MenuTitle,dishItem.TotalPrice,{left = 8, right = 0}, 2)
            self.printer:goPaperPoints(20)
            dishTotalPrice=dishItem.TotalPrice+dishTotalPrice
          end
      end
    end
    if(dishCode=="VR")then
      self.printer:setFontStyle(1)
      self.printer:justifyText("Total Price",string.format("%.2f", dishTotalPrice) , 2)
      self.printer:goPaperPoints(20)
      dishTotalPrice=0
       -- split line
            -- self.printer:initPrinter()
            -- self.printer:printLineSpliter()
            -- self.printer:goPaperPoints(1)
            -- self.printer:initPrinter()
            -- self.printer:setBlackMark()
    end



  -- add price show 
  for i=1 , #(self.goodsResources)do
    local dishItem=self.goodsResources[i]
    if(dishItem.Type=='AddPrice')then
        if(dishItemType~='AddPrice') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='AddPrice'
        end
        self.printer:initPrinter()
        self.printer:setAlignment(0)
       self.printer:summaryTwoText('$'..string.format("%.2f", dishItem.TotalPrice * dishItem.Quantity),dishItem.MenuTitle,4)
        self.printer:goPaperPoints(20)
    end
  end
  
  
    -- add option AddOption
   for i=1 , #(self.goodsResources)do
    local dishItem=self.goodsResources[i]
    if dishItem.Type=='AddOption' then
        if(dishItemType~='AddOption') then
            -- split line
            self.printer:initPrinter()
            self.printer:printLineSpliter()
            self.printer:goPaperPoints(1)
            self.printer:initPrinter()
            self.printer:setBlackMark()
            dishItemType='AddOption'
        end
        self.printer:initPrinter()
        self.printer:setAlignment(0)
        self.printer:summaryDishTextOven(dishItem.Quantity, "X", dishItem.MenuTitle,{left = 4, right = 0}, 2)
        self.printer:goPaperPoints(20)
    end
  end


    -- split line
    self.printer:initPrinter()
    self.printer:printLineSpliter()
    self.printer:goPaperPoints(1)
    self.printer:initPrinter()
    self.printer:setBlackMark() 

    -- end line
    self.printer:initPrinter()
    self.printer:setAlignment(1)
    self.printer:sendText('Printed at '..self.odData.currentDatetime)
    self.printer:goPaperLines(4)
end
return DiffReport
