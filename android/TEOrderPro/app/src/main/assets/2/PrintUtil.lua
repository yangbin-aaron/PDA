local PrintUtil = {}

PrintUtil.__index = PrintUtil

function PrintUtil:new( printKey )
   -- body
   local self = {}
   setmetatable(self,PrintUtil)
   self.key = printKey
   self.lineLength = 48
   return self 
end

-- 传输text
function PrintUtil:sendText(text)
   TE_sendText(self.key, text)
end

-- 传输命令
function PrintUtil:sendCmd(cmd)
   TE_sendCmd(self.key, cmd)
end

-- 打印二维码
function PrintUtil:print2dCode(code)
   code = tostring(code)
   code = string.gsub(code, ' ', '')
   -- self:setAlignment(1)
   TE_print2dCode(self.key, code)
end


function PrintUtil:logTable(tab, deep, space)
   if space == nil then
      space = '  '
   end
   if deep == nil then
      deep = 0
   end
   deep = math.ceil(deep)
   if(type(tab) == 'table') then
      for k,v in pairs(tab) do
         if(type(v) == 'table') then
            print(space .. tostring(k) .. ":{")
            if deep == nil then
               self:logTable(v, nil, (space .. space))
            elseif deep > 0 then
               self:logTable(v, deep-1, (space .. space))
            else
               -- Do Not recurrence again
               print(space .. '... ...' ..  ";")
            end
            print(space .. "}")
         else
            print(space .. tostring(k) .. ":" .. tostring(v) .. ";")
         end
      end
   else
      print('Not a table ERROR:' .. tab)
   end
end

function PrintUtil:checkNumberInRange(num, min, max, def)
   --print(debug.traceback())
   if min == nil then
      min = 0
   end

   if max == nil then
      max = 255
   end

   if def == nil then
      def = min
   end

   if num < min or num > max then
      num = def
   end
   return num
end

function PrintUtil:getCountOfCharLength(text, zhChWidth, ascWidth)
   if zhChWidth == nil then
      zhChWidth = 2
   end

   if ascWidth == nil then
      ascWidth = math.ceil(zhChWidth / 2)
   end
   local oriLen = tostring(text):len()
   local ascLen = 0
   local getByte = string.byte
   for i = 1, oriLen do
      local byte = getByte(text, i)
      if byte >= 0 and byte < 128 then
         ascLen = ascLen + 1
      end
   end

   local charLen = (oriLen - ascLen)/ 3 * zhChWidth + ascLen * ascWidth
   local charLen = math.ceil(charLen)

   return charLen
end

function PrintUtil:justifyText(leftText, rightText, zhChWidth, spCh)
   if zhChWidth == nil then
      zhChWidth = 2
   end

   if spCh == nil then
      spCh = ' '
   end

   leftText  = tostring(leftText)
   rightText = tostring(rightText)
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)
   local spaceCount = (self.lineLength - l - r) * 2 / zhChWidth
   spaceCount=math.ceil(spaceCount)
   local spaces = string.rep(spCh, spaceCount)
   local result = leftText .. spaces .. rightText
   self:sendText(result)
end



function PrintUtil:justifyThreeText(leftText, centerText, rightText,sides, zhChWidth, spWidth,spCh, linePoints)

   if linePoints == nil then
      linePoints = 10
   end

   if spCh == nil then
      spCh = ' '
   end

   if zhChWidth == nil then
      zhChWidth = 2
   end

   if spWidth == nil then
      spWidth = math.ceil(zhChWidth / 2)
   end

   if leftText == nil then
      leftText = ""
   end

   if rightText == nil then
      rightText = ""
   end

   local tab = self:cutText(centerText, sides, zhChWidth, spWidth)

   local lc = self:getCountOfCharLength(leftText, zhChWidth)
   local rc = self:getCountOfCharLength(rightText, zhChWidth)

   lc = sides.left  - lc
   rc = sides.right - rc

   if lc > 0 then
      leftText = leftText .. string.rep(spCh, lc)
   end

   if rc > 0 then
      rightText = rightText .. string.rep(spCh, rc)
   end

   for i = 1, #(tab) do
      if i == 1 and i == #(tab) then
         self:sendText(leftText .. tab[i] .. rightText)
      elseif i == 1 then
         self:sendText(leftText .. tab[i] .. string.rep(spCh, sides.right))
      elseif i == #(tab) then
         self:sendText(string.rep(spCh, sides.left) .. tab[i] .. rightText)
      else

         self:sendText(string.rep(spCh, sides.left) .. tab[i] .. string.rep(spCh, sides.right))
      end
      self:goPaperPoints(linePoints)
   end
end



function PrintUtil:summaryItemText(leftText, rightText, zhChWidth, spCh)
   if zhChWidth == nil then
      zhChWidth = 2
   end

   if spCh == nil then
      spCh = ' '
   end

   leftText  = tostring(leftText)
   rightText = tostring(rightText)
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)
   spaceCount = (self.lineLength -l-10)*2/ zhChWidth
   spaceCount=math.abs(math.ceil(spaceCount))
   print('\n leftText-->>>',leftText,' spaceCount--->>>',spaceCount,'--l-->>',l)
   local spaces = string.rep(spCh, 9)
   local lspaces = string.rep(spCh, spaceCount)
   local result = lspaces..leftText .. spaces .. rightText
   self:sendText(result)
end



function PrintUtil:summaryItemTextCenter(leftText,centerText,rightText, zhChWidth, spCh)
   if zhChWidth == nil then
      zhChWidth = 2
   end

   if spCh == nil then
      spCh = ' '
   end

   leftText  = tostring(leftText)
   centerText = tostring(centerText)
   rightText = tostring(rightText)
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local c = self:getCountOfCharLength(centerText,zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)
   spaceCount = (self.lineLength -l-c-10)*2/ zhChWidth
   spaceCount=math.abs(math.ceil(spaceCount))
   print('\n leftText-->>>',leftText,' spaceCount--->>>',spaceCount,'--l-->>',l)
   local spaces = string.rep(spCh, 4)
   local lspaces = string.rep(spCh, spaceCount)
   local result = lspaces..leftText .. spaces ..centerText.. spaces .. rightText
   self:sendText(result)
end


function PrintUtil:summaryTwoText(leftText, rightText, zhChWidth, spCh)
   if zhChWidth == nil then
      zhChWidth = 2
   end

   if spCh == nil then
      spCh = ' '
   end

   leftText  = tostring(leftText)
   rightText = tostring(rightText)
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)
   local spaceCount = (self.lineLength/2-l)*2/ zhChWidth
   spaceCount=math.ceil(spaceCount)
   local spaces = string.rep(spCh, 9)
   local lspaces = string.rep(spCh, spaceCount)
   local result = leftText .. lspaces .. rightText
   self:sendText(result)
end




function PrintUtil:summaryDishText(leftText, centerText, rightText,sides,zhChWidth,spCh)
   if spCh == nil then
      spCh = ' '
   end
   if zhChWidth == nil then
      zhChWidth = 2
   end
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local c = self:getCountOfCharLength(centerText,zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)

   local spaceCount=(self.lineLength/2-l-20)*2/zhChWidth
   local spaceOne=string.rep(spCh,spaceCount);
   local spaceTwo=string.rep(spCh,3)

   local tab = self:cutTextWithOutRight(rightText, sides, zhChWidth, spWidth)
   for i , j in pairs(tab) do
      if i == 1 and i == #(tab) then
         local result=leftText..spaceOne..centerText..spaceTwo..j
         self:sendText(result)
      elseif i == 1 then
         local result=leftText..spaceOne..centerText..spaceTwo..j
         self:sendText(result)
      elseif i == #(tab) then
         self:sendText(string.rep(spCh, sides.left) .. j)
      else
         self:sendText(string.rep(spCh, sides.left) .. j)
      end
      self:goPaperPoints(1)
   end
end


function PrintUtil:summaryDishTextOven(leftText, centerText, rightText,sides,zhChWidth,spCh)
   if spCh == nil then
      spCh = ' '
   end
   if zhChWidth == nil then
      zhChWidth = 2
   end
   local l = self:getCountOfCharLength(leftText, zhChWidth)
   local c = self:getCountOfCharLength(centerText,zhChWidth)
   local r = self:getCountOfCharLength(rightText, zhChWidth)

   local spaceCount=(self.lineLength/2-l-20)*2/zhChWidth
   local spaceOne=string.rep(spCh,spaceCount);
   local spaceRightCount=(self.lineLength - l -r-c-spaceCount) * 2 / zhChWidth
   local spaceTwo=string.rep(spCh,spaceRightCount)

   local tab = self:cutTextWithOutRight(rightText, sides, zhChWidth, spWidth)
   for i , j in pairs(tab) do
      if i == 1 and i == #(tab) then
         local result=leftText..spaceOne..centerText..spaceTwo..j
         self:sendText(result)
      elseif i == 1 then
         local result=leftText..spaceOne..centerText..spaceTwo..j
         self:sendText(result)
      elseif i == #(tab) then
         self:sendText(string.rep(spCh, sides.left) .. j)
      else
         self:sendText(string.rep(spCh, sides.left) .. j)
      end
      self:goPaperPoints(1)
   end
end



function PrintUtil:cutText(text, sides, zhChWidth, spWidth)
   if spWidth == nil then
      spWidth = math.ceil(zhChWidth / 2)
   end

   local txtTB = { }
   local lineCapacity = self.lineLength - (sides.left + sides.right) * spWidth
   local oriLen = tostring (text):len()
   local ascLen = 0
   local getByte = string.byte
   local txtTBIndex = 1;
   local cutFlag = 0;
   local begIndex = 1
   local i = 1
   local charLen = 0
   while i <= oriLen do
      local byte = getByte(text, i)
      if byte >= 0 and byte < 128 then
         ascLen = ascLen + 1
         cutFlag = 0
      else
         cutFlag = cutFlag + 1
      end
      cutFlag = cutFlag % zhChWidth
      -- utf-8用三个字节表示一个字符，所以除以3
      -- charLen 表示当前字节长度
      charLen = (i - ascLen)/ 3 * zhChWidth + ascLen * spWidth
      if charLen % lineCapacity == 0 then
         txtTB[txtTBIndex] = string.sub(text, begIndex, i)
         txtTBIndex = txtTBIndex + 1
         begIndex = i + 1
      end
      i = i + 1
   end
   if begIndex < i then
      local moreSpaces  = math.ceil(lineCapacity - charLen%lineCapacity)
      txtTB[txtTBIndex] = string.sub(text, begIndex, i) .. string.rep(' ', moreSpaces)
      txtTBIndex = txtTBIndex + 1
   end
   return txtTB
end

function PrintUtil:cutTextWithOutRight(text, sides, zhChWidth, spWidth)
   if spWidth == nil then
      spWidth = math.ceil(zhChWidth / 2)
   end

   local txtTB = { }
   local lineCapacity = self.lineLength - (sides.left + sides.right) * spWidth
   local oriLen = tostring(text):len()
   local ascLen = 0
   local getByte = string.byte
   local txtTBIndex = 1;
   local cutFlag = 0;
   local begIndex = 1
   local i = 1
   local charLen = 0
   while i <= oriLen do
      local byte = getByte(text, i)
      if byte >= 0 and byte < 128 then
         ascLen = ascLen + 1
         cutFlag = 0
      else
         cutFlag = cutFlag + 1
      end
      cutFlag = cutFlag % zhChWidth
      -- utf-8用三个字节表示一个字符，所以除以3
      -- charLen 表示当前字节长度
      charLen = (i - ascLen)/ 3 * zhChWidth + ascLen * spWidth
      if charLen % lineCapacity == 0 then
         txtTB[txtTBIndex] = string.sub(text, begIndex, i)
         txtTBIndex = txtTBIndex + 1
         begIndex = i + 1
      end
      i = i + 1
   end
   if begIndex < i then
      txtTB[txtTBIndex] = string.sub(text, begIndex, i)
      txtTBIndex = txtTBIndex + 1
   end
   return txtTB
end




function PrintUtil:initPrinter()
   -- self:sendCmd("\27\82\0")
   self:sendCmd("\27\64")
end

function PrintUtil:bel(duration, interval, times)
   local cmd = string.format("\27\7%s%s%s",
                             tostring(duration),
                             tostring(interval),
                             tostring(times))
   self:sendCmd(cmd)
end

function PrintUtil:cut()
   self:sendCmd("\27\105\0\66")
end

function PrintUtil:printLineSpliter(ch, count)
   if ch == nil then
      ch = '-'
   end

   if count == nil then
      count = self.lineLength
   end
   self:sendText(string.rep(ch, count))
end

function PrintUtil:goPaperLines(lines)
   lines = self:checkNumberInRange(lines)
   self:sendCmd(string.format("\27\100%c",lines))
end

function PrintUtil:goPaperPoints(point)
   point = self:checkNumberInRange(point)
   self:sendCmd(string.format("\27\74%c",point))
end

function PrintUtil:setUnderline(lines)
   lines = self:checkNumberInRange(lines)
   self:sendCmd(string.format("\27\45%c",lines))
end

function PrintUtil:setLineSpace(point)
   point = self:checkNumberInRange(point)
   self:sendCmd(string.format("\27\51%c",point))
end

-- '\0':  Left
-- '\1':  Center
-- '\2':  Right
function PrintUtil:setAlignment(align)
  if align == 0 then 
      align = "\48"
   elseif align == 1 then
      align = "\1"
   elseif align == 2 then
      align = "\2"
   end

   self:sendCmd(string.format("\27\97%s",align))

end

function PrintUtil:setDefaultLineSpace()
   self:sendCmd("\27\50")
end

function PrintUtil:setLineSpace(lp)
   self:sendCmd(string.format("\27\51%c", lp))
end

function PrintUtil:openCashbox()
   self:sendCmd("\27\112\0\17\8")
end

function PrintUtil:setFontBold(enable)
   if enable == true then
      self:sendCmd("\27\69\1")
   else
      self:sendCmd("\27\69\10")
   end
end

function PrintUtil:setFontStyle(style)
   if(style == nil) then
      style = 0
   end

   if style == 0 then
      self:sendCmd("\27\33\0")
   elseif style == 1 then
      self:sendCmd("\29\33\1")
   elseif style == 2 then
      self:sendCmd("\29\33\17")
   elseif style == 3 then
      self:sendCmd("\27\33\32")
   elseif style == 4 then
      self:sendCmd("\29\33\0")
   elseif style == 5 then 
      self:sendCmd("\29\33\18")
   else
      self:sendCmd("\27\33\0")
   end
end

function PrintUtil:setFontSize(size)
   self:sendCmd(string.format("\27\33%d",size))
  --self:sendCmd("\29\33\1")
  --self:sendCmd("\29\33\16")
end

-- 响铃一声
function PrintUtil:setBel()
   -- body
   self:sendCmd('\7')
end

-- 自定义响铃
--n1 指定响铃的时间长度,n2 指定间歇时间长度,n3 响铃次数。n1,n2 单位为 100 毫秒。
function PrintUtil:setBelMySelf()
   -- body
   self:sendCmd(string.format('\27\7\15\5\3'))
end

function PrintUtil:print()
   self:sendCmd("\27\255")
end

function PrintUtil:setPageMode(enable)
   if enable == true then
      self:sendCmd("\27\76")
   else
      self:sendCmd("\255")
   end
end

function PrintUtil:setColorReversal(enable)
   if enable == true then
      self:sendCmd("\29\66\1")
   else
      self:sendCmd("\29\66\0")
   end
  
end

function PrintUtil:setChineseMode(enable)
   if enable == true then
      mode = "\28\38"
   else
      mode = "\28\46"
   end
   self:sendCmd(mode)
end

function PrintUtil:setBlackMark()
   self:sendCmd("\12")
end





return PrintUtil

